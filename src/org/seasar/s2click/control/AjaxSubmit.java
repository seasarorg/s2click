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
package org.seasar.s2click.control;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.click.control.Submit;
import org.apache.click.element.Element;
import org.apache.click.util.HtmlStringBuffer;
import org.seasar.s2click.util.AjaxUtils;

/**
 * フォームの内容を<tt>prototype.js</tt>の<code>Ajax.Request</code>を使って
 * 送信するための<code>Submit</code>コントロール。
 *
 * @author Naoki Takezoe
 * @since 0.4.0
 */
public class AjaxSubmit extends Submit implements AjaxControl {

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

	@Override
	public List<Element> getHeadElements() {
		if (headElements == null) {
			headElements = super.getHeadElements();
			headElements.add(AjaxUtils.getPrototypeJsImport());
		}
		return headElements;
	}

	public void addAjaxHandler(String event, String handler){
		this.handlers.put(event, handler);
	}

	public Map<String, String> getAjaxHandlers(){
		return this.handlers;
	}

	@Override
	public void render(HtmlStringBuffer buffer) {
		super.render(buffer);
		buffer.append("<script type=\"text/javascript\">\n");
		buffer.append("$('").append(getForm().getId()).append("').onsubmit = ");
		buffer.append("function(){\n");
		buffer.append("  this.request({ method: 'post', ");
		buffer.append(AjaxUtils.getOptions(handlers));
		buffer.append("}); return false;");
		buffer.append("}\n");
		buffer.append("</script>\n");
	}

}
