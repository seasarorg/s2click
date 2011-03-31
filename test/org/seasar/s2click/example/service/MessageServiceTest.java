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
package org.seasar.s2click.example.service;

import java.text.SimpleDateFormat;
import java.util.List;

import org.seasar.s2click.example.entity.Message;
import org.seasar.s2click.test.Assert;
import org.seasar.s2click.test.GenerateExcel;
import org.seasar.s2click.test.S2ClickServiceTestCase;
import org.seasar.s2click.test.Table;

/**
 * サービスクラスのテストのサンプルです。
 * 
 * @author Naoki Takezoe
 */
public class MessageServiceTest extends S2ClickServiceTestCase<MessageService> {
	
    private SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    
    @Assert(tables = @Table(name="MESSAGE", excludeColumns={"MESSAGE_ID", "DATE"}))
    public void testInsertTx() throws Exception {
		Message message = new Message();
		message.name = "たけぞう";
		message.message = "テスト";
		message.date = formatter.parse("2009/01/01 12:34:56");
		service.insert(message);
    }
    
	/**
	 * メッセージ1件のみ存在する場合。
	 * <p>
	 * Javaコードを使ったテストケースのサンプルです。
	 */
	public void testGetMessages1Tx() throws Exception {
		Message message = new Message();
		message.name = "たけぞう";
		message.message = "テスト";
		message.date = formatter.parse("2009/01/01 12:34:56");
		service.insert(message);
		
		List<Message> messages = service.getMessages();
		
		assertEquals(1, messages.size());
		
		assertEquals("たけぞう", messages.get(0).name);
		assertEquals("テスト", messages.get(0).message);
		assertEquals("2009/01/01 12:34:56", formatter.format(messages.get(0).date));
	}

	/**
	 * メッセージが複数件存在する場合。
	 * <p>
	 * Excelを使ったユニットテストのサンプルです。
	 */
    @GenerateExcel(tables = @Table(name="MESSAGE", orderBy="MESSAGE_ID DESC",  excludeColumns={"DATE"}))
	public void testGetMessages2Tx() {
		List<Message> messages = service.getMessages();
		
		assertBeanListEquals("データベース内のデータが期待値と一致しません。", 
				getExpectDataSet(), messages);
	}
	
	/**
	 * メッセージが存在しない場合。
	 */
	public void testGetMessages3Tx() throws Exception {
		List<Message> messages = service.getMessages();
		assertTrue(messages.isEmpty());
	}
	
}
