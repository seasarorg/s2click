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
package org.seasar.s2click.example.page;

import org.seasar.s2click.annotation.Path;
import org.seasar.s2click.example.form.ControlsForm;

@Path("/controls.htm")
public class ControlsPage extends LayoutPage {
	
	public String title = "Click標準のコントロール";
	public String template = "/form.htm";
	public ControlsForm form = new ControlsForm("form");
	
	public ControlsPage(){
		form.submit.setListener(this, "doSubmit");
	}
	
	public boolean doSubmit(){
		if(form.isValid()){
			form.toHidden();
			
			// 送信ボタンを削除して戻るボタンを追加
			form.submit.setLabel("戻る");
			form.submit.setListener(this, "doBack");
			
			template = "/controls-confirm.htm";
			return false;
		}
		return true;
	}
	
	public boolean doBack(){
		return true;
	}
	
}
