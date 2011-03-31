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

import org.apache.click.control.Submit;
import org.apache.click.extras.control.IntegerField;
import org.seasar.s2click.control.S2ClickForm;

/**
 * 足し算アプリケーションのフォームクラス。
 * 
 * @author Naoki Takezoe
 */
public class AddForm extends S2ClickForm {
	
	private static final long serialVersionUID = 1L;
	
	public AddForm(String name){
		super(name);
		setFieldAutoRegisteration(true);
		setJavaScriptValidation(true);
	}
	
	public IntegerField num1 = new IntegerField("num1", "数値1", true);
	public IntegerField num2 = new IntegerField("num2", "数値2", true);
	public IntegerField result = new IntegerField("result", "結果");
	public Submit submit = new Submit("add", "足し算");

}
