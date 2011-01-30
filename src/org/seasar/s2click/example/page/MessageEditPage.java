package org.seasar.s2click.example.page;

import org.seasar.s2click.EntityEditPage;
import org.seasar.s2click.annotation.Layout;
import org.seasar.s2click.example.entity.Message;

@Layout
public class MessageEditPage extends EntityEditPage {

	private static final long serialVersionUID = 1L;

	public MessageEditPage() {
		super(Message.class, MessageListPage.class);
	}

}
