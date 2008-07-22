package org.seasar.s2click.page;

import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.click.Page;

import org.apache.commons.io.IOUtils;
import org.seasar.s2click.annotation.Path;

/**
 * ファイルをダウンロードするページのための抽象基底クラスです。
 * <p>
 * 以下に実装例を示します：
 * <pre>
 * <span class="kw">public</span> SampleDownloadPage <span class="kw">extends</span> AbstractDownloadPage {
 *   <span class="kw">public</span> SampleDownloadPage(){
 *     setFileName(<span class="st">"sample.txt"</span>);
 *     setContents(SampleDownloadPage.class.getResourceAsStream(<span class="st">"sample.txt"</span>));
 *   }
 * }</pre>
 * ファイルダウンロードページはダウンロードするコンテンツを自分自身でレスポンスに書き込むため、HTMLテンプレートは不要です。
 * ただし、ClickはHTMLテンプレートが存在しないページクラスは自動マッピングの対象外となるため、
 * クラスに{@link Path}アノテーションを付与することでパスを明示するとよいでしょう。
 * 
 * @author Naoki Takezoe
 */
public abstract class AbstractDownloadPage extends Page {
	
	protected String contentType = "application/octet-stream";
	protected String contentDisposition = "attachment";
	protected String fileName;
	protected InputStream contents;
	
	/**
	 * Content-Typeヘッダの値を設定します。
	 * 
	 * @param contentType コンテンツタイプ。
	 *   デフォルト値は<code>"application/octet-stream"</code>です。
	 */
	protected void setContentType(String contentType){
		if(contentType == null){
			throw new IllegalArgumentException("contentType shouldn't be null.");
		}
		this.contentType = contentType;
	}
	
	/**
	 * ダウンロードファイル名をセットします。
	 * 
	 * @param fileName ファイル名
	 */
	protected void setFileName(String fileName){
		this.fileName = fileName;
	}
	
	/**
	 * ダウンロードするコンテンツの<code>InputStream</code>をセットします。
	 * 
	 * @param contents ダウンロードするコンテンツ
	 */
	protected void setContents(InputStream contents){
		if(contents == null){
			throw new IllegalArgumentException("contents shouldn't be null.");
		}
		this.contents = contents;
	}
	
	/**
	 * Content-Dispositionヘッダの値をセットします。
	 * 
	 * @param contentDisposition <code>"attachment"</code>もしくは<code>"inline"</code>。
	 *    デフォルト値は<code>"attachment"</code>です。
	 */
	protected void setContentDisposition(String contentDisposition){
		this.contentDisposition = contentDisposition;
	}

	/**
	 * ファイルダウンロードページではHTMLテンプレートが不要であるため、
	 * このメソッドは常に<code>null</code>を返すよう実装されています。
	 */
	@Override public String getPath() {
		return null;
	}

	/**
	 * このメソッド内でダウンロードコンテンツをレスポンスに書き出します。
	 */
	@Override public void onRender() {
		if(this.contents == null){
			throw new IllegalStateException("download contents is not specified.");
		}
		
		HttpServletRequest req = getContext().getRequest();
		HttpServletResponse res = getContext().getResponse();
		OutputStream out = null;
		String fileName = this.fileName;
		String contentDisposition = this.contentDisposition;
		
		try {
			res.setContentType(this.contentType);
			
			if(this.contentType != null){
				String userAgent = req.getHeader("USER-AGENT");
				if(userAgent.indexOf("MSIE") >= 0 && userAgent.indexOf("Opera") < 0){
					fileName = new String(fileName.getBytes("Windows-31J"), "ISO8859_1");
				} else {
					fileName = new String(fileName.getBytes("UTF-8"), "ISO8859_1");
				}
				contentDisposition = contentDisposition + "; filename=\"" + fileName + "\"";
			}
			res.setHeader("Content-Disposition", contentDisposition);
			
			out = res.getOutputStream();
			byte[] buf = new byte[1024 * 8];
			int length = 0;
			while((length = this.contents.read(buf)) >= 0){
				out.write(buf, 0, length);
			}
			
			res.flushBuffer();
			
		} catch(Exception ex){
			throw new RuntimeException(ex);
		} finally {
			IOUtils.closeQuietly(this.contents);
			IOUtils.closeQuietly(out);
		}
	}
	
}
