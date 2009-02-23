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

import java.util.Date;

import javax.annotation.Resource;

import org.seasar.extension.jdbc.JdbcManager;
import org.seasar.s2click.example.entity.Message;
import org.seasar.s2click.example.form.MessageForm;

/**
 * S2Click + S2JDBCのテストページ。
 * 
 * @author Naoki Takezoe
 */
public class JdbcPage extends LayoutPage {
	
	@Resource
	protected JdbcManager jdbcManager;
	
	public String title = "S2JDBC";
	public MessageForm form = new MessageForm("form");

	public JdbcPage(){
		form.submit.setListener(this, "doAdd");
	}
	
	@Override public void onRender() {
		addModel("messageList", jdbcManager.from(
				Message.class).orderBy("messageId desc").getResultList());
	}
	
	public boolean doAdd(){
		if(form.isValid()){
			Message message = new Message();
			message.name = form.name.getValue();
			message.message = form.message.getValue();
			message.date = new Date();
			jdbcManager.insert(message).execute();
			
			setRedirect(JdbcPage.class);
			return false;
		}
		return true;
	}
	
}
