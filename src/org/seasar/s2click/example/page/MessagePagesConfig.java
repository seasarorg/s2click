package org.seasar.s2click.example.page;

import org.seasar.s2click.example.entity.Message;
import org.seasar.s2click.jdbc.EntityPagesConfig;

public class MessagePagesConfig extends EntityPagesConfig {

	public MessagePagesConfig() {
		super(Message.class,
				MessageListPage.class,
				MessageRegisterPage.class,
				MessageEditPage.class,
				MessageDeletePage.class);

		// 表示用ラベルの設定
		putLabel("messageId", "ID");
		putLabel("name", "名前");
		putLabel("message", "メッセージ");
		putLabel("date", "登録日");
	}

}
