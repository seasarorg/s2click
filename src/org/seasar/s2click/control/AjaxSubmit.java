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

import net.sf.click.control.Submit;
import net.sf.click.util.ClickUtils;

import org.seasar.s2click.util.AjaxUtils;

/**
 * フォームの内容を<tt>prototype.js</tt>の<code>Ajax.Request</code>を使って
 * 送信するための<code>Submit</code>コントロール。
 * 
 * @author Naoki Takezoe
 * @since 0.4.0
 */
public class AjaxSubmit extends Submit {

	private static final long serialVersionUID = 1L;
	
	protected Map<String, String> handlers = new HashMap<String, String>();
	
	public AjaxSubmit() {
		super();
	}

	public AjaxSubmit(String name, Object listener, String method) {
		super(name, listener, method);
	}

//	public AjaxSubmit(String name, String label, Object listener,
//			String method, String confirmMessage) {
//		super(name, label, listener, method, confirmMessage);
//	}

	public AjaxSubmit(String name, String label, Object listener, String method) {
		super(name, label, listener, method);
	}

	public AjaxSubmit(String name, String label) {
		super(name, label);
	}

	public AjaxSubmit(String name) {
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
	
	@Override public String toString() {
		String formId = getForm().getId();
//		StringBuilder sb = new StringBuilder();
//		sb.append(formId).append(".request(");
//		sb.append("{");
//		sb.append("method: 'post', ");
//		sb.append(AjaxUtils.getOptions(handlers));
//		sb.append("}); return false;");
//		
//		setAttribute("onclick", sb.toString());
		
		StringBuilder sb = new StringBuilder();
		sb.append("<script type=\"text/javascript\">\n");
		sb.append("$('").append(formId).append("').onsubmit = ");
		sb.append("function(){\n");
		sb.append("  this.request({ method: 'post', ");
		sb.append(AjaxUtils.getOptions(handlers));
		sb.append("}); return false;");
		sb.append("}\n");
		sb.append("</script>\n");
		
		return super.toString() + sb.toString();
	}
	
}
