package org.seasar.s2click.example.page;

import org.seasar.s2click.EntityDeletePage;
import org.seasar.s2click.annotation.Layout;
import org.seasar.s2click.example.entity.Message;

@Layout
public class MessageDeletePage extends EntityDeletePage {

	private static final long serialVersionUID = 1L;

	public MessageDeletePage() {
		super(Message.class, MessageListPage.class);
	}

}
