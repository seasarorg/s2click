package org.seasar.s2click.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.seasar.s2click.S2ClickConfig;
import org.seasar.s2click.util.S2ClickUtils;

/**
 * <tt>s2click.dicon</tt>で設定した文字コードをリクエストに設定するフィルタです。
 * <p>
 * 機能的にはS2Containerで提供されている<code>EncodingFilter</code>と同等ですが、
 * 文字コードの指定を<tt>s2click.dicon</code>に統一できるので、
 * S2Clickではこのフィルタを使用することを推奨します。
 * 
 * @author Naoki Takezoe
 * @since 0.5.0
 */
public class S2ClickEncodingFilter implements Filter {
	
	public void destroy() {
	}

	public void doFilter(ServletRequest req, ServletResponse res,
			FilterChain chain) throws IOException, ServletException {
		if(req.getCharacterEncoding() == null){
			S2ClickConfig config = S2ClickUtils.getComponent(S2ClickConfig.class);
			req.setCharacterEncoding(config.charset);
		}
		chain.doFilter(req, res);
	}

	public void init(FilterConfig config) throws ServletException {
	}

}
