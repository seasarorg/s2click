package org.seasar.s2click.example.service;

import java.util.Date;
import java.util.List;

import org.seasar.s2click.S2ClickTestCase;
import org.seasar.s2click.example.entity.Message;

/**
 * サービスクラスのテストのサンプルです。
 * <p>
 * TODO サービスクラスのテスト方法についてはもう少し検討の余地がありそう…。
 * 
 * @author Naoki Takezoe
 */
public class MessageServiceTest extends S2ClickTestCase {
	
	private MessageService messageService;
	
	/**
	 * メッセージ1件のみ存在する場合。
	 */
	public void testGetMessages1Tx() {
		Message message = new Message();
		message.message = "テスト";
		message.name = "たけぞう";
		message.date = new Date();
		
		messageService.insert(message);
		
		List<Message> messages = messageService.getMessages();
		assertEquals(1, messages.size());
		assertEquals("たけぞう", messages.get(0).name);
		assertEquals("テスト", messages.get(0).message);
	}

	/**
	 * メッセージが複数件存在する場合。
	 */
	public void testGetMessages2Tx() {
		{
			Message message = new Message();
			message.message = "テスト1";
			message.name = "たけぞう";
			message.date = new Date();
			messageService.insert(message);
		}
		{
			Message message = new Message();
			message.message = "テスト2";
			message.name = "たけぞう";
			message.date = new Date();
			messageService.insert(message);
		}
		{
			Message message = new Message();
			message.message = "テスト3";
			message.name = "たけぞう";
			message.date = new Date();
			messageService.insert(message);
		}
		
		
		List<Message> messages = messageService.getMessages();
		assertEquals(3, messages.size());
		assertEquals("たけぞう", messages.get(0).name);
		assertEquals("テスト3", messages.get(0).message);
		assertEquals("たけぞう", messages.get(1).name);
		assertEquals("テスト2", messages.get(1).message);
		assertEquals("たけぞう", messages.get(2).name);
		assertEquals("テスト1", messages.get(2).message);
	}
	
	/**
	 * メッセージが存在しない場合。
	 */
	public void testGetMessages3Tx() {
		List<Message> messages = messageService.getMessages();
		assertTrue(messages.isEmpty());
	}
	
}
