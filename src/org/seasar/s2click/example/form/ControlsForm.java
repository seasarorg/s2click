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
package org.seasar.s2click.example.form;

import org.apache.click.control.Option;
import org.apache.click.control.Submit;
import org.apache.click.extras.control.CheckList;
import org.apache.click.extras.control.HiddenList;
import org.apache.click.extras.control.PickList;
import org.seasar.s2click.control.DateFieldYYYYMMDD;
import org.seasar.s2click.control.S2ClickForm;

public class ControlsForm extends S2ClickForm {
	
	private static final long serialVersionUID = 1L;
	
	public ControlsForm(String name){
		super(name);
		setFieldAutoRegisteration(true);
		
		checkList.add("Click Framework");
		checkList.add("Wicket");
		checkList.add("Grails");
		
		pickList.add(new Option("Java"));
		pickList.add(new Option("C#"));
		pickList.add(new Option("Ruby"));
		pickList.add(new Option("Perl"));
		pickList.add(new Option("Python"));
		
		hiddenList.addValue("Eclipse");
		hiddenList.addValue("NetBeans");
	}
	
	public DateFieldYYYYMMDD dateField = new DateFieldYYYYMMDD("date", "日付");
	public CheckList checkList = new CheckList("checkList", "チェックリスト");
	public PickList pickList = new PickList("pickList", "ピックリスト");
	public HiddenList hiddenList = new HiddenList("hiddenList");
	public Submit submit = new Submit("submit", "送信");
	
//	public AutoCompleteTextField completion 
//		= new AutoCompleteTextField("completion", "入力補完"){
//
//		private static final long serialVersionUID = 1L;
//		
//		@SuppressWarnings("unchecked")
//		@Override public List getAutoCompleteList(String criteria) {
//			List<String> list = new ArrayList<String>();
//			list.add("Java");
//			list.add("JavaScript");
//			list.add("Perl");
//			list.add("Python");
//			list.add("Ruby");
//			return list;
//		}
//	};
	
}
