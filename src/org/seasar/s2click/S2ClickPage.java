package org.seasar.s2click;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.util.Date;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.arnx.jsonic.JSON;
import net.sf.click.ClickApp;
import net.sf.click.Page;
import net.sf.click.S2ClickServlet;
import net.sf.click.util.PropertyUtils;
import net.sf.click.util.RequestTypeConverter;
import ognl.Ognl;
import ognl.OgnlException;
import ognl.TypeConverter;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.seasar.framework.util.tiger.ReflectionUtil;
import org.seasar.s2click.annotation.Request;
import org.seasar.s2click.control.AjaxLink;
import org.seasar.s2click.exception.RequestConversionException;
import org.seasar.s2click.exception.RequestRequiredException;
import org.seasar.s2click.util.S2ClickUtils;

/**
 * 
 * @author Naoki Takezoe
 * @since 0.4.0
 */
public class S2ClickPage extends Page {
	
	/**
	 * テンプレートのレンダリングをスキップする際にリクエストの属性にセットするフラグのキーです。
	 * <p>
	 * <code>renderResponse()</code>メソッドによってページクラス内でレスポンスを書き出すと、
	 * このキーでリクエストの属性にフラグがセットされます。
	 * {@link S2ClickServlet}はリクエストの属性にこのキーでフラグがセットされている場合、
	 * テンプレートのレンダリングをスキップします。
	 */
	public static final String SKIP_RENDERING = S2ClickPage.class.getName() + "_skipRendering";
	
	protected static TypeConverter typeConverter = new RequestTypeConverter();
	
	@Override
	public void onInit() {
		super.onInit();
		bindPageFields();
	}
	
	/**
	 * OGNLの型コンバータを取得します
	 * @return
	 */
	protected static TypeConverter getTypeConverter() {
        return typeConverter;
    }

	
	/**
	 * リクエストパラメータをバインドするページクラスのフィールドを取得します。
	 *
	 * @param clazz ページクラスの<code>Class</code>オブジェクト
	 * @param name リクエストパラメータ名
	 * @return フィールド（該当のフィールドが存在しな場合は<code>null</code>）
	 */
	private Field getRequestBindField(ClickApp clickApp, String name){
		for(Field field: clickApp.getPageFieldArray(getClass())){
			Request req = field.getAnnotation(Request.class);
			if(req != null){
				if(StringUtils.isEmpty(req.name())){
					if(field.getName().equals(name)){
						return field;
					}
				} else {
					if(req.name().equals(name)){
						return field;
					}
				}
			}
		}
		return null;
	}
	
	/**
	 * {@link Request}アノテーションでリクエストパラメータをフィールドにバインドします。
	 * 
	 * @throws RequestConversionException リクエストパラメータの型変換に失敗した場合
	 * @throws RequestRequiredException 必須のリクエストパラメータが指定されていなかった場合
	 */
	@SuppressWarnings("unchecked")
	protected void bindPageFields() throws RequestConversionException, RequestRequiredException {
		ClickApp clickApp = S2ClickUtils.getClickApp();
		
        if (clickApp.getPageFields(getClass()).isEmpty()) {
            return;
        }

        Map ognlContext = null;

        boolean customConverter =
            ! getTypeConverter().getClass().equals(RequestTypeConverter.class);

        HttpServletRequest request = getContext().getRequest();

        for (Enumeration e = request.getParameterNames(); e.hasMoreElements();) {
            String name = e.nextElement().toString();
            String value = request.getParameter(name);

            if (StringUtils.isNotEmpty(value)) {

                Field field = getRequestBindField(clickApp, name);

                if (field != null) {
                    Class type = field.getType();

                    if (customConverter
                        || (type.isPrimitive()
                            || String.class.isAssignableFrom(type)
                            || Number.class.isAssignableFrom(type)
                            || Boolean.class.isAssignableFrom(type))) {

                        if (ognlContext == null) {
                            ognlContext = Ognl.createDefaultContext(this, null, getTypeConverter());
                        }
                        try {
                        	PropertyUtils.setValueOgnl(this, field.getName(), value, ognlContext);
                        } catch(OgnlException ex){
                        	throw new RequestConversionException(ex);
                        }

//                        if (logger.isTraceEnabled()) {
//                            logger.trace("   auto bound variable: " + name + "=" + value);
//                        }
                    }
                }
                
                
            }
        }
		
		// 必須パラメータのチェック
		for(Field field: clickApp.getPageFieldArray(getClass())){
			Request ann = field.getAnnotation(Request.class);
			if(ann != null){
				if(ann.required() == true){
					Object value = ReflectionUtil.getValue(field, this);
					if(value == null || (value instanceof String && StringUtils.isEmpty((String) value))){
						throw new RequestRequiredException(field.getName());
					}
				}
			}
		}
	}

	/**
	 * レスポンスにJSONをレンダリングします。{@link AjaxLink}などと組み合わせて使用します。
	 * 
	 * @param obj JSONとしてレスポンスするオブジェクト
	 */
	protected void renderJSON(Object obj){
		try {
			byte[] json = JSON.encode(obj).getBytes("UTF-8");
			renderResponse("application/x-javascript; charset=utf-8", 
					new ByteArrayInputStream(json));
			
		} catch(UnsupportedEncodingException ex){
			// あり得ない
		}
	}
	
	/**
	 * レスポンスにHTMLをレンダリングします。{@link AjaxLink}などと組み合わせて使用します。
	 * 
	 * @param html 返却するHTML
	 */
	protected void renderHTML(String html){
		try {
			renderResponse("text/html; charset=UTF-8", 
					new ByteArrayInputStream(html.getBytes("UTF-8")));
		} catch(UnsupportedEncodingException ex){
			// あり得ない
		}
	}
	
	/**
	 * レスポンスにファイルをレンダリングします。ファイルダウンロード時に使用します。
	 * コンテンツタイプには<code>application/octet-stream</code>が用いられます。
	 * 
	 * @param fileName ファイル名
	 * @param file ファイルの内容
	 */
	protected void renderFile(String fileName, InputStream file){
		renderFile(null, fileName, file);
	}
	
	
	/**
	 * レスポンスにファイルをレンダリングします。ファイルダウンロード時に使用します。
	 * 
	 * @param contentType コンテンツタイプ（nullの場合は<code>application/octet-stream</code>が用いられます）
	 * @param fileName ファイル名
	 * @param file ファイルの内容
	 */
	protected void renderFile(String contentType, String fileName, InputStream file){
		try {
			String contentDisposition = "attachment";
			String userAgent = getContext().getRequest().getHeader("USER-AGENT");
			
			if(userAgent.indexOf("MSIE") >= 0 && userAgent.indexOf("Opera") < 0){
				fileName = new String(fileName.getBytes("Windows-31J"), "ISO8859_1");
			} else {
				fileName = new String(fileName.getBytes("UTF-8"), "ISO8859_1");
			}
			contentDisposition = contentDisposition + "; filename=\"" + fileName + "\"";
			
			getContext().getResponse().setHeader("Content-Disposition", contentDisposition);
			
			if(contentType == null){
				renderResponse("application/octet-stream", file);
			} else {
				renderResponse(contentType, file);
			}
			
		} catch(UnsupportedEncodingException ex){
			// あり得ない
		}
	}
	
	/**
	 * レスポンスをレンダリングします。
	 * 
	 * @param contentType コンテンツタイプ
	 * @param contents レスポンスの内容
	 */
	protected void renderResponse(String contentType, InputStream contents) {
		HttpServletResponse res = getContext().getResponse();
		
		// ヘッダもここで書き出します。
		@SuppressWarnings("unchecked")
		Map<String, Object> headers = getHeaders();
		
        for (Iterator<Map.Entry<String, Object>> i = headers.entrySet().iterator(); i.hasNext();) {
            Map.Entry<String, Object> entry = i.next();
            String name = entry.getKey().toString();
            Object value = entry.getValue();

            if (value instanceof String) {
                String strValue = (String) value;
                if (!strValue.equalsIgnoreCase("Content-Encoding")) {
                    res.setHeader(name, strValue);
                }

            } else if (value instanceof Date) {
                long time = ((Date) value).getTime();
                res.setDateHeader(name, time);

            } else {
                int intValue = ((Integer) value).intValue();
                res.setIntHeader(name, intValue);
            }
        }
        
		OutputStream out = null;
		
		try {
			res.setContentLength(contents.available());
			res.setContentType(contentType);
			out = res.getOutputStream();
			IOUtils.copy(contents, out);
			res.flushBuffer();
			
		} catch(Exception ex){
			throw new RuntimeException(ex);
			
		} finally {
			IOUtils.closeQuietly(contents);
			IOUtils.closeQuietly(out);
			getContext().setRequestAttribute(SKIP_RENDERING, "true");
		}
	}
	
}
