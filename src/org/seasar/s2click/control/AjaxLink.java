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
package org.seasar.s2click.control;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

import org.apache.click.control.ActionLink;
import org.apache.click.util.ClickUtils;
import org.apache.commons.lang.StringUtils;
import org.seasar.s2click.util.AjaxUtils;


/**
 * <tt>prototype.js</tt>の<code>Ajax.Request</code>、<code>Ajax.Updater</code>を使用して
 * Ajaxを実現するためのアクションリンクです。
 * 
 * <h2>Ajax.Requestを使用する場合</h2>
 * 以下のコードは<code>Ajax.Request</code>を使ってページクラスの<code>onClickメソッドを呼び出します。
 * また、正常にレスポンスが戻るとJavaScriptの<code>displayResult()</code>関数が呼び出されます。
 * <pre>
 * AjaxLink link = new AjaxLink("link", this, "onClick");
 * link.addAjaxHandler(AjaxUtils.ON_COMPLETE, "displayResult");
 * addControl(link);
 * </pre>
 * 
 * <h2>Ajax.Updaterを使用する場合</h2>
 * 以下のコードは<code>Ajax.Updater</code>を使ってページクラスの<code>onClickメソッドを呼び出し、
 * レスポンスの内容でHTMLのid属性が<code>result</code>の要素を内容を置き換えます。
 * <pre>
 * AjaxLink link = new AjaxLink("link", this, "onClick");
 * link.setElementId("result");
 * addControl(link);
 * </pre>
 * 
 * @author Naoki Takezoe
 * @since 0.4.0
 */
public class AjaxLink extends ActionLink {
	
	protected Map<String, String> handlers = new HashMap<String, String>();
	
	private static final long serialVersionUID = 1L;
	
	protected String elementId;
	
	public AjaxLink() {
		super();
	}

	public AjaxLink(Object listener, String method) {
		super(listener, method);
	}

	public AjaxLink(String name, Object listener, String method) {
		super(name, listener, method);
	}

	public AjaxLink(String name, String label, Object listener, String method) {
		super(name, label, listener, method);
	}

	public AjaxLink(String name, String label) {
		super(name, label);
	}

	public AjaxLink(String name) {
		super(name);
	}
	
    public String getHtmlImports() {
        Object[] args = {
            getContext().getRequest().getContextPath(),
            ClickUtils.getResourceVersionIndicator(getContext()),
        };
        
        return MessageFormat.format(AjaxUtils.HTML_IMPORTS, args);
    }

	public void addAjaxHandler(String event, String handler){
		this.handlers.put(event, handler);
	}
	
	public Map<String, String> getAjaxHandlers(){
		return this.handlers;
	}
	
	@Override public String getHref() {
		return "javascript:void(0)";
	}	
	
	/**
	 * 更新するHTML要素のidを取得します。
	 * @return 更新するHTML要素のid
	 */
	public String getElementId() {
		return elementId;
	}

	/**
	 * 更新するHTML要素のidを設定します。
	 * @param elementId 更新するHTML要素のid
	 */
	public void setElementId(String elementId) {
		this.elementId = elementId;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public String toString(){
		if(StringUtils.isEmpty(getElementId())){
			setAttribute("onclick", AjaxUtils.createAjaxRequest(
					getHref(getValue()), handlers, getParameters()));
		} else {
			setAttribute("onclick", AjaxUtils.createAjaxUpdater(
					getElementId(), getHref(getValue()), handlers, getParameters()));
		}
		return super.toString();
	}
	
}
