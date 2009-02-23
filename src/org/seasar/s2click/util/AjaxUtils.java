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
package org.seasar.s2click.util;

import java.util.Map;

import org.apache.commons.lang.StringEscapeUtils;

/**
 * Ajax関連のユーティリティメソッドを提供します。
 * 
 * @author Naoki Takezoe
 * @since 0.4.0
 */
public class AjaxUtils {
	
	public static final String ON_CREATE = "onCreate";
	
	public static final String ON_COMPLETE = "onComplete";
	
	public static final String ON_EXCEPTION = "onException";
	
	public static final String ON_FAILURE = "onFailure";
	
	public static final String ON_SUCCESS = "onSuccess";
	

	// public static final String HTML_IMPORTS =
	// "<script type=\"text/javascript\"
	// src=\"{0}/click/prototype/prototype{1}.js\"></script>\n";
	// TODO Click付属のprototype.jsはバージョンが古い…
	public static final String HTML_IMPORTS = 
		"<script type=\"text/javascript\" src=\"{0}/resources/prototype.js\"></script>\n";

	/**
	 * <tt>prototype.js</tt>の<code>Ajax.Request</code>を呼び出すJavaScriptを生成します。
	 * 
	 * @param url URL
	 * @param options オプション
	 * @param parameters パラメータ
	 * @return <code>Ajax.Request</code>を呼び出すJavaScript
	 */
	public static String createAjaxRequest(String url, Map<String, String> options,
			Map<String, String> parameters){
		StringBuilder sb = new StringBuilder();
		sb.append("new Ajax.Request(");
		sb.append("'").append(url).append("', ");
		sb.append("{");
		sb.append("method: 'post'");
		if(!parameters.isEmpty()){
			sb.append(", parameters: ").append(encodeParameters(parameters));
		}
		if(!options.isEmpty()){
			sb.append(", ").append(getOptions(options));
		}
		sb.append("})");
		
		return sb.toString();
	}
	
	/**
	 * <tt>prototype.js</tt>の<code>Ajax.Updater</code>を呼び出すJavaScriptを生成します。
	 * 
	 * @param id 置換するHTML要素のid属性
	 * @param url URL
	 * @param options オプション
	 * @param parameters パラメータ
	 * @return <code>Ajax.Updater</code>を呼び出すJavaScript
	 */
	public static String createAjaxUpdater(String id, String url, Map<String, String> options,
			Map<String, String> parameters){
		StringBuilder sb = new StringBuilder();
		sb.append("new Ajax.Updater(");
		sb.append("'").append(id).append("', ");
		sb.append("'").append(url).append("', ");
		sb.append("{");
		sb.append("method: 'post'");
		if(!parameters.isEmpty()){
			sb.append(", parameters: ").append(encodeParameters(parameters));
		}
		if(!options.isEmpty()){
			sb.append(", ").append(getOptions(options));
		}
		sb.append("})");
		
		return sb.toString();
	}
	
	private static String encodeParameters(Map<String, String> parameters){
		StringBuilder sb = new StringBuilder();
		for(Map.Entry<String, String> entry: parameters.entrySet()){
			if(sb.length() != 0){
				sb.append(", ");
			}
			sb.append("'");
			sb.append(StringEscapeUtils.escapeJavaScript(entry.getKey()));
			sb.append("': '");
			sb.append(StringEscapeUtils.escapeJavaScript(entry.getValue()));
			sb.append("'");
		}
		return "{" + sb.toString() + "}";
	}
	
	public static String getOptions(Map<String, String> options){
		StringBuilder sb = new StringBuilder();
		for(Map.Entry<String, String> entry: options.entrySet()){
			if(sb.length() != 0){
				sb.append(", ");
			}
			sb.append(entry.getKey()).append(": ").append(entry.getValue());
		}
		return sb.toString();
	}

	
}
