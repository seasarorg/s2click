package org.seasar.s2click.example.service;

import java.text.SimpleDateFormat;
import java.util.List;

import org.seasar.s2click.example.entity.Message;
import org.seasar.s2click.test.Assert;
import org.seasar.s2click.test.S2ClickServiceTestCase;
import org.seasar.s2click.test.Assert.Table;

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
	public void testGetMessages2Tx() {
//		readXlsAllReplaceDb("MessageServiceTest_testGetMessages2Tx_data.xls");
		
		List<Message> messages = service.getMessages();
		
		assertBeanListEquals("データベース内のデータが期待値と一致しません。", 
				readXls("MessageServiceTest_testGetMessages2Tx_expect.xls"), messages);
	}
	
	/**
	 * メッセージが存在しない場合。
	 */
	public void testGetMessages3Tx() throws Exception {
		List<Message> messages = service.getMessages();
		assertTrue(messages.isEmpty());
	}
	
}
