package net.sf.click;

import java.io.UnsupportedEncodingException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.seasar.s2click.S2ClickConfig;
import org.seasar.s2click.util.S2ClickUtils;

/**
 * GETリクエスト時にURLのクエリ文字列に含まれる日本語が文字化けする問題に対処するためのラッパークラスです.
 * <p>
 * クエリ文字列として受け取ったパラメータを{@link S2ClickConfig#charset}で指定された文字コードでデコードします。
 * 
 * @author Naoki Takezoe
 * @since 0.3.1
 */
public class S2ClickRequestWrapper extends HttpServletRequestWrapper {
	
	protected S2ClickConfig config;
	
	public S2ClickRequestWrapper(HttpServletRequest request) {
		super(request);
		config = S2ClickUtils.getComponent(S2ClickConfig.class);
	}

	@Override
	public String getParameter(String name) {
		String value = super.getParameter(name);
		if(value != null && getMethod().toUpperCase().equals("GET")){
			try {
				value = new String(value.getBytes("iso-8859-1"), config.charset);
			} catch(UnsupportedEncodingException ex){
				// TODO ログ出す
			}
		}
		return value;
	}

	@Override
	public String[] getParameterValues(String name) {
		String[] values = super.getParameterValues(name);
		if(values != null && getMethod().toUpperCase().equals("GET")){
			for(int i=0;i<values.length;i++){
				try {
					values[i] = new String(values[i].getBytes("iso-8859-1"), config.charset);
				} catch(UnsupportedEncodingException ex){
					// TODO ログ出す
				}
			}
		}
		return values;
	}
	
}
