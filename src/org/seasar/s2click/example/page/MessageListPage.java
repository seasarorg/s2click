package org.seasar.s2click.example.page;

import org.seasar.s2click.EntityListPage;
import org.seasar.s2click.annotation.Layout;
import org.seasar.s2click.example.entity.Message;

@Layout
public class MessageListPage extends EntityListPage {

	private static final long serialVersionUID = 1L;

	public MessageListPage() {
		super(Message.class,
				MessageRegisterPage.class,
				MessageEditPage.class,
				MessageDeletePage.class);
	}

}
