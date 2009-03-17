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
package org.seasar.s2click.example.form;

import org.apache.click.control.PasswordField;
import org.apache.click.control.Submit;
import org.apache.click.control.TextField;
import org.seasar.s2click.control.MobileForm;

public class MobileSampleForm extends MobileForm {
	
	private static final long serialVersionUID = 1L;
	
	public TextField userId = new TextField("userId", "ユーザID", true);
	public PasswordField password = new PasswordField("password", "パスワード", true);
	public Submit submit = new Submit("login", "ログイン");
	
	public MobileSampleForm(String name){
		super(name);
		setFieldAutoRegisteration(true);
	}
	
}
