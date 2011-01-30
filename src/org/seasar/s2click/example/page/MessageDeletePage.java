package org.seasar.s2click.example.page;

import org.seasar.s2click.annotation.Layout;
import org.seasar.s2click.jdbc.EntityDeletePage;

@Layout
public class MessageDeletePage extends EntityDeletePage {

	private static final long serialVersionUID = 1L;

	public MessageDeletePage() {
		super(new MessagePagesConfig());
	}

}
