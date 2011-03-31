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
