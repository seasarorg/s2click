/*
 * Copyright 2006-2010 the Seasar Foundation and the Others.
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
package org.seasar.s2click.example.page;

import org.apache.click.ActionListener;
import org.apache.click.Control;
import org.seasar.s2click.example.form.AddForm;

/**
 * JSPのサンプルページ。
 * 
 * @author Naoki Takezoe
 */
public class JspExamplePage extends JspLayoutPage {
	
	private static final long serialVersionUID = 1L;

	public String title = "JSPのサンプル";
	
	public AddForm form = new AddForm("form");
	
	public JspExamplePage(){
		form.submit.setActionListener(new ActionListener(){
			private static final long serialVersionUID = 1L;

			public boolean onAction(Control source) {
				if(form.isValid()){
					form.result.setInteger(form.num1.getInteger() + form.num2.getInteger());
				}
				return true;
			}
		});
	}
	
}
