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

import java.util.ArrayList;
import java.util.List;

import org.seasar.s2click.example.entity.Message;
import org.seasar.s2click.example.service.MessageService;
import org.seasar.s2click.test.S2ClickPageTestCase;

public class JdbcPageTest extends S2ClickPageTestCase<JdbcPage> {

	public void testOnRender() {
		// サービスクラスのモックを設定
		page.messageService = new MessageService(){
			@Override
			public List<Message> getMessages() {
				List<Message> messages = new ArrayList<Message>();
				{
					Message message = new Message();
					message.messageId = 1;
					messages.add(message);
				}
				{
					Message message = new Message();
					message.messageId = 2;
					messages.add(message);
				}
				{
					Message message = new Message();
					message.messageId = 3;
					messages.add(message);
				}
				return messages;
			}
		};
		
		// テスト対象メソッドの呼び出し
		page.onRender();
		
		// サービスの戻り値がモデルとしてセットされていること
		@SuppressWarnings("unchecked")
		List<Message> messages = (List<Message>) page.getModel().get("messageList");
		
		// 件数を確認
		assertEquals(3, messages.size());
		
		// 返却されたレコードを確認
		assertEquals(1, messages.get(0).messageId.intValue());
		assertEquals(2, messages.get(1).messageId.intValue());
		assertEquals(3, messages.get(2).messageId.intValue());
	}

//	public void testJdbcPage() {
//		ActionListenerAdaptor adaptor = (ActionListenerAdaptor) page.form.submit.getActionListener();
//		assertSame(page, getField(adaptor, "listener"));
//		assertEquals("doAdd", getField(adaptor, "method"));
//	}

	public void testDoAdd() {
		final Message result = new Message();
		
		// サービスクラスのモックを設定
		page.messageService = new MessageService(){
			@Override
			public int insert(Message entity) {
				result.name = entity.name;
				result.message = entity.message;
				return 1;
			}
		};
		
		// フォームに入力値をセット
		page.form.name.setValue("たけぞう");
		page.form.message.setValue("メッセージ");
		
		// テスト対象のメソッドを呼び出し
		assertFalse(page.doAdd());
		
		// フォームの入力値がエンティティに反映されてサービスに渡されてくること
		assertEquals("たけぞう", result.name);
		assertEquals("メッセージ", result.message);
	}

}
