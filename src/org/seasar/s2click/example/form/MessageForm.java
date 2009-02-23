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

import net.sf.click.control.Submit;
import net.sf.click.control.TextField;

import org.seasar.s2click.control.S2ClickForm;

public class MessageForm extends S2ClickForm {

	private static final long serialVersionUID = 1L;
	
	public TextField name = new TextField("name", true);
	public TextField message = new TextField("message", true);
	public Submit submit = new Submit("add");
	
	public MessageForm(String name){
		super(name);
		message.setSize(40);
		addConfirmMessage(submit.getName(), "メッセージを送信します。よろしいですか？");
		setFieldAutoRegisteration(true);
		setJavaScriptValidation(true);
	}

}
