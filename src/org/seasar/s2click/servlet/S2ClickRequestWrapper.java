/*
 * Copyright 2006-2009 the Seasar Foundation and the Others.
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
package org.seasar.s2click.servlet;

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
 * @since 0.4.0
 */
public class S2ClickRequestWrapper extends HttpServletRequestWrapper {
	
	protected S2ClickConfig config;
	
	public S2ClickRequestWrapper(HttpServletRequest request) {
		super(request);
		this.config = S2ClickUtils.getComponent(S2ClickConfig.class);
	}

	@Override
	public String getParameter(String name) {
		String value = super.getParameter(name);
		if(value != null && getMethod().toUpperCase().equals("GET")){
			try {
				value = new String(value.getBytes("iso-8859-1"), config.charset);
			} catch(UnsupportedEncodingException ex){
				throw new RuntimeException(ex);
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
					throw new RuntimeException(ex);
				}
			}
		}
		return values;
	}
	
}
