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
package org.seasar.s2click.example.form;

import org.apache.click.control.Button;
import org.apache.click.control.TextField;
import org.seasar.s2click.annotation.AjaxHandler;
import org.seasar.s2click.annotation.Attribute;
import org.seasar.s2click.annotation.Properties;
import org.seasar.s2click.control.AjaxButton;
import org.seasar.s2click.control.AjaxSubmit;
import org.seasar.s2click.control.S2ClickForm;

public class JsonForm extends S2ClickForm {

	private static final long serialVersionUID = 1L;

	public TextField keyword = new TextField("keyword", "書籍名");

	@AjaxHandler(onCreate="startProgress", onComplete="displayResult")
	public AjaxSubmit search = new AjaxSubmit("submit", "検索");

	@AjaxHandler(onCreate="startProgress", onComplete="displayResult")
	public AjaxButton searchAll = new AjaxButton("searchAll", "全件検索");

	@Properties({@Attribute(name="onclick", value="clearResult()")})
	public Button clear = new Button("clear", "クリア");

	public JsonForm(String name){
		super(name);
		setFieldAutoRegisteration(true);
	}

}
