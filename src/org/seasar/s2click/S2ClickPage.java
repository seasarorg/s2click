package org.seasar.s2click;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import net.arnx.jsonic.JSON;
import net.sf.click.Page;

import org.apache.commons.io.IOUtils;
import org.seasar.s2click.control.AjaxLink;

/**
 * 
 * @author Naoki Takezoe
 * @since 0.4.0
 */
public class S2ClickPage extends Page {
	
	public static final String SKIP_RENDERING = S2ClickPage.class.getName() + "_skipRendering";
	
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
			renderResponse("text/html; cahrset=UTF-8", 
					new ByteArrayInputStream(html.getBytes("UTF-8")));
		} catch(UnsupportedEncodingException ex){
			// あり得ない
		}
	}
	
	/**
	 * レスポンスにファイルをレンダリングします。ファイルダウンロード時に使用します。
	 * 
	 * @param fileName ファイル名
	 * @param file ファイルの内容
	 */
	protected void renderFile(String fileName, InputStream file){
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
			
			renderResponse("application/octet-stream", file);
			
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
