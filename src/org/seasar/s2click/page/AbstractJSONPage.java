package org.seasar.s2click.page;

import java.io.OutputStream;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.seasar.s2click.annotation.Path;

import net.arnx.jsonic.JSON;
import net.sf.click.Page;

/**
 * JSONを返却するページクラスのための抽象基底クラスです。
 * <p>
 * {@link #setContents(Object)}にPOJO、配列、<code>java.util.List</code>、<code>java.util.Map</code>をセットすることで、
 * <a href="http://jsonic.sourceforge.jp/">JSONIC</a>によってJSONに変換したテキストをレスポンスとして返却することができます。
 * <p>
 * 以下に実装例を示します：
 * <pre>
 * <span class="kw">public</span> SampleJsonPage <span class="kw">extends</span> AbstractJSONPage {
 *   <span class="kw">public</span> SampleJSONPage(){
 *     List&lt;Employee&gt; list = new ArrayList&lt;Employee&gt;();
 *     ...
 *     setContents(list);
 *   }
 * }</pre>
 * <code>AbstractJSONPage</code>のサブクラスでは、ページクラス内でJSONがレスポンスに書き出されるため、HTMLテンプレートは不要です。
 * ただし、ClickはHTMLテンプレートが存在しないページクラスは自動マッピングの対象外となるため、
 * クラスに{@link Path}アノテーションを付与することでパスを明示するとよいでしょう。
 * 
 * @author Naoki Takezoe
 */
public class AbstractJSONPage extends Page {
	
	protected String contentType = "application/x-javascript; charset=utf-8";
	protected Object contents;
	
	/**
	 * JSONとして返却するJavaBeanをセットします。
	 * 
	 * @param contents JSONとして返却するJavaBean
	 */
	protected void setContents(Object contents){
		if(contents == null){
			throw new IllegalArgumentException("contents shouldn't be null.");
		}
		this.contents = contents;
	}
	
	/**
	 * Content-Typeヘッダをセットします。
	 * 
	 * @param contentType Content-Typeヘッダの値。
	 *   デフォルト値は<code>"application/x-javascript; charset=utf-8"</code>です。
	 */
	protected void setContentType(String contentType){
		if(contentType == null){
			throw new IllegalArgumentException("contentType shouldn't be null.");
		}
		this.contentType = contentType;
	}
	
	/**
	 * JSONを返却するページではHTMLテンプレートが不要であるため、
	 * このメソッドは常に<code>null</code>を返すよう実装されています。
	 */
	@Override public String getPath() {
		return null;
	}
	
	/**
	 * このメソッド内でJSONレスポンスを出力します。
	 */
	@Override public void onRender() {
		if(this.contents == null){
			throw new IllegalStateException("JSON contents is not specified.");
		}
		
		HttpServletResponse res = getContext().getResponse();
		OutputStream out = null;
		
		try {
			res.setContentType(this.contentType);
			out = res.getOutputStream();
			out.write(JSON.encode(this.contents).getBytes("UTF-8"));
			
			res.flushBuffer();
			
		} catch(Exception ex){
			throw new RuntimeException(ex);
		} finally {
			IOUtils.closeQuietly(out);
		}
	}

}
