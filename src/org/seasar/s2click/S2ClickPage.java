/*
 * Copyright 2006-2011 the Seasar Foundation and the Others.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
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
import ognl.Ognl;
import ognl.OgnlException;
import ognl.TypeConverter;

import org.apache.click.Page;
import org.apache.click.element.JsImport;
import org.apache.click.element.JsScript;
import org.apache.click.service.ConfigService;
import org.apache.click.util.PropertyUtils;
import org.apache.click.util.RequestTypeConverter;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.seasar.framework.container.SingletonS2Container;
import org.seasar.framework.util.tiger.ReflectionUtil;
import org.seasar.s2click.annotation.Layout;
import org.seasar.s2click.annotation.Request;
import org.seasar.s2click.control.AjaxLink;
import org.seasar.s2click.exception.RequestConversionException;
import org.seasar.s2click.exception.RequestRequiredException;
import org.seasar.s2click.servlet.S2ClickServlet;
import org.seasar.s2click.util.AjaxUtils;
import org.seasar.s2click.util.S2ClickUtils;

/**
 *
 * @author Naoki Takezoe
 * @since 0.4.0
 */
public abstract class S2ClickPage extends Page {

	private static final long serialVersionUID = 1L;

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

		// リクエストパラメータをフィールドにバインド
		bindPageFields();

		// @Ajaxアノテーションを付与したメソッドを呼び出すためのJavaScript関数を作成
		String ajaxJavaScript = AjaxUtils.createAjaxJavaScript(this);
		if(StringUtils.isNotEmpty(ajaxJavaScript)){
			getHeadElements().add(new JsImport("/resources/prototype.js"));
			getHeadElements().add(new JsScript(ajaxJavaScript));
		}
	}

	/**
	 * OGNLの型コンバータを取得します
	 * @return OGNLの型コンバータ
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
	private Field getRequestBindField(ConfigService configService, String name){
		for(Field field: configService.getPageFieldArray(getClass())){
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
	@SuppressWarnings("rawtypes")
	protected void bindPageFields() throws RequestConversionException, RequestRequiredException {
		ConfigService configService = S2ClickUtils.getConfigService();

        if (configService.getPageFields(getClass()).isEmpty()) {
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

                Field field = getRequestBindField(configService, name);

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
		for(Field field: configService.getPageFieldArray(getClass())){
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
			renderResponse(AjaxUtils.CONTENT_TYPE_JSON,
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
			renderResponse(AjaxUtils.CONTENT_TYPE_HTML,
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

	@Override
	public String getTemplate() {
		Layout layout = getClass().getAnnotation(Layout.class);
		if(layout == null){
			return super.getTemplate();
		}

		String path = layout.value();
		if(StringUtils.isNotEmpty(path)){
			return path;
		}

		S2ClickConfig config = SingletonS2Container.getComponent(S2ClickConfig.class);
		if(StringUtils.isNotEmpty(config.layoutTemplatePath)){
			return config.layoutTemplatePath;
		}

		throw new RuntimeException("layoutTemplatePath is not configured.");
	}



}
