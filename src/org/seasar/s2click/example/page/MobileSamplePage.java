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

import java.util.ArrayList;
import java.util.List;

import org.apache.click.ActionListener;
import org.apache.click.Control;
import org.apache.click.control.PageLink;
import org.seasar.s2click.S2ClickPage;
import org.seasar.s2click.control.PaginateList;
import org.seasar.s2click.example.form.MobileSampleForm;

/**
 * モバイル向け機能のサンプルページです。
 * 
 * @author Naoki Takezoe
 */
public class MobileSamplePage extends S2ClickPage {
	
	public MobileSampleForm form = new MobileSampleForm("form");
	public PageLink backLink = new PageLink("backLink", "戻る", IndexPage.class);
	
	public List<String> list = new ArrayList<String>();
	public PaginateList paginateList = new PaginateList("paginateList");
	
	public MobileSamplePage(){
		form.setActionListener(new ActionListener(){
			public boolean onAction(Control source) {
				return doLogin();
			}
		});
		
		list.add("Java");
		list.add("C#");
		list.add("Perl");
		list.add("Ruby");
		list.add("Python");
		
		paginateList.setRowList(list);
		paginateList.setPageSize(3);
		paginateList.setTemplatePath("/paginateList.htm");
	}
	
	protected boolean doLogin(){
		if(form.isValid()){
			if(form.userId.getValue().equals("test") 
					&& form.password.getValue().equals("test")){
				addModel("result", "ログイン成功");
				
			} else {
				addModel("result", "ログイン失敗");
			}
		}
		return true;
	}
	
}
