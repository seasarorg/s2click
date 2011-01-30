package org.seasar.s2click.example.page;

import org.seasar.s2click.EntityPagesConfig;
import org.seasar.s2click.example.entity.Message;

public class MessagePagesConfig extends EntityPagesConfig {

	public MessagePagesConfig() {
		super(Message.class,
				MessageListPage.class,
				MessageRegisterPage.class,
				MessageEditPage.class,
				MessageDeletePage.class);
	}

}
