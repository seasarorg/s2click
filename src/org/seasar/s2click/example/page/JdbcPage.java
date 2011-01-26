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

import java.util.Date;

import javax.annotation.Resource;

import org.apache.click.ActionListener;
import org.apache.click.Control;
import org.seasar.s2click.S2ClickPage;
import org.seasar.s2click.annotation.Layout;
import org.seasar.s2click.example.entity.Message;
import org.seasar.s2click.example.form.MessageForm;
import org.seasar.s2click.example.service.MessageService;

/**
 * S2Click + S2JDBCのテストページ。
 *
 * @author Naoki Takezoe
 */
@Layout
public class JdbcPage extends S2ClickPage {

	private static final long serialVersionUID = 1L;

	@Resource
	protected MessageService messageService;

	public String title = "S2JDBC";
	public MessageForm form = new MessageForm("form");

	public JdbcPage(){
		form.submit.setActionListener(new ActionListener(){
			private static final long serialVersionUID = 1L;

			public boolean onAction(Control source) {
				return doAdd();
			}
		});
	}

	@Override
	public void onRender() {
		addModel("messageList", messageService.getMessages());
	}

	protected boolean doAdd(){
		if(form.isValid()){
			Message message = new Message();
			message.name = form.name.getValue();
			message.message = form.message.getValue();
			message.date = new Date();
			messageService.insert(message);

			setRedirect(JdbcPage.class);
			return false;
		}
		return true;
	}

}
