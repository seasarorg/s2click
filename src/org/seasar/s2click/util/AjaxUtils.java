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
package org.seasar.s2click.util;

import java.lang.reflect.Method;
import java.util.Map;

import org.apache.click.Page;
import org.apache.click.element.JsImport;
import org.apache.commons.lang.StringEscapeUtils;
import org.seasar.s2click.annotation.Ajax;

/**
 * Ajax関連のユーティリティメソッドを提供します。
 *
 * @author Naoki Takezoe
 * @since 0.4.0
 */
public class AjaxUtils {

	public static final String CONTENT_TYPE_JSON = "application/x-javascript; charset=utf-8";
	public static final String CONTENT_TYPE_HTML = "text/html; charset=utf-8";


	public static final String ON_CREATE = "onCreate";

	public static final String ON_COMPLETE = "onComplete";

	public static final String ON_EXCEPTION = "onException";

	public static final String ON_FAILURE = "onFailure";

	public static final String ON_SUCCESS = "onSuccess";

	/**
	 * <tt>prototype.js</tt>の<code>Ajax.Request</code>を呼び出すJavaScriptを生成します。
	 *
	 * @param url URL
	 * @param options オプション
	 * @param parameters パラメータ
	 * @return <code>Ajax.Request</code>を呼び出すJavaScript
	 */
	public static String createAjaxRequest(String url, Map<String, String> options,
			Map<String, Object> parameters){
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
			Map<String, Object> parameters){
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

	private static String encodeParameters(Map<String, Object> parameters){
		StringBuilder sb = new StringBuilder();
		for(Map.Entry<String, Object> entry: parameters.entrySet()){
			if(sb.length() != 0){
				sb.append(", ");
			}
			sb.append("'");
			sb.append(StringEscapeUtils.escapeJavaScript(entry.getKey()));
			sb.append("': '");
			sb.append(StringEscapeUtils.escapeJavaScript(entry.getValue().toString())); // TODO nullチェック？
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

	/**
	 * {@link Ajax}アノテーションを指定したpublicメソッドを呼び出すためのJavaScriptを生成します。
	 * <p>
	 * メソッド毎に以下のシグネチャを持つJavaScript関数を生成します。
	 * この関数はprototype.jsのAjax.Requestを使用してリモートメソッドの呼び出しを行います。
	 * <pre>
	 * function メソッド名(resultHandler, arg0, arg1 ...)
	 * </pre>
	 * 第一引数にはAjax呼び出しの結果を処理する関数を指定します。
	 * 第二引数以降はサーバ側のメソッドに渡す引数を指定します。
	 * <p>
	 * なお、エラー発生時のハンドラには以下の実装が使用されます。
	 * このエラーハンドラは本メソッドが返却するJavaScriptに含まれています。
	 * <pre>
	 * function ajaxDefaultErrorHandler(transport){
	 *   alert('通信に失敗しました。');
	 * }
	 * </pre>
	 *
	 * @param page 対象のページクラス
	 * @return {@link Ajax}アノテーションを指定したpublicメソッドを呼び出すためのJavaScript
	 */
	public static String createAjaxJavaScript(Page page){
		// @Ajaxアノテーションを付与したメソッドを呼び出すためのJavaScript関数を作成
		StringBuilder sb = new StringBuilder();

		Method[] methods = page.getClass().getMethods();
		for(Method method: methods){
			if(method.getAnnotation(Ajax.class) != null){
				sb.append("function ").append(method.getName()).append("(");
				sb.append("resultHandler");
				int parameterLength = method.getParameterTypes().length;
				for(int i=0; i < parameterLength; i++){
					sb.append(", arg").append(i);
				}
				sb.append("){");

				sb.append("new Ajax.Request('").append(page.getContext().getRequest().getContextPath()).append(page.getPath()).append("', {");
				sb.append("method: 'post',");
				sb.append("onSuccess: resultHandler,");
				sb.append("onFailure: ajaxDefaultErrorHandler,");
				sb.append("parameters: {");
				for(int i=0; i < parameterLength; i++){
					sb.append("arg").append(i).append(": arg").append(i).append(",");
				}
				sb.append("ajax: '").append(method.getName()).append("'");
				sb.append("}");
				sb.append("});");

				sb.append("}");
			}
		}

		if(sb.length() > 0){
			sb.append("function ajaxDefaultErrorHandler(transport){");
			sb.append("alert('通信に失敗しました。');");
			sb.append("}");
		}

		return sb.toString();
	}

	/**
	 * <tt>prototype.js</tt>をインポートするための&lt;script&gt;タグを生成します。
	 *
	 * @return <tt>prototype.js</tt>をインポートするためのタグ
	 */
	public static JsImport getPrototypeJsImport(){
//		Context context = Context.getThreadLocalContext();
//
//		Object[] args = {
//        	context.getRequest().getContextPath(),
//			ClickUtils.getResourceVersionIndicator(context)
//		};

		return new JsImport("/resources/prototype.js");
	}


}
