package org.seasar.s2click.example.page;

import org.seasar.s2click.EntityRegisterPage;
import org.seasar.s2click.annotation.Layout;
import org.seasar.s2click.example.entity.Message;

@Layout
public class MessageRegisterPage extends EntityRegisterPage {

	private static final long serialVersionUID = 1L;

	public MessageRegisterPage() {
		super(Message.class, MessageListPage.class);
	}

}
