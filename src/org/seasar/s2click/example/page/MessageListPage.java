package org.seasar.s2click.example.page;

import org.seasar.s2click.annotation.Layout;
import org.seasar.s2click.jdbc.EntityListPage;

@Layout
public class MessageListPage extends EntityListPage {

	private static final long serialVersionUID = 1L;

	public MessageListPage() {
		super(new MessagePagesConfig());
	}

}
