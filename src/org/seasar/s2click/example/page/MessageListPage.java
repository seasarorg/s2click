package org.seasar.s2click.example.page;

import org.seasar.s2click.EntityListPage;
import org.seasar.s2click.annotation.Layout;

@Layout
public class MessageListPage extends EntityListPage {

	private static final long serialVersionUID = 1L;

	public MessageListPage() {
		super(new MessagePagesConfig());
	}

}
