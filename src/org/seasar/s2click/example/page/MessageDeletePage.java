package org.seasar.s2click.example.page;

import org.seasar.s2click.EntityDeletePage;
import org.seasar.s2click.annotation.Layout;

@Layout
public class MessageDeletePage extends EntityDeletePage {

	private static final long serialVersionUID = 1L;

	public MessageDeletePage() {
		super(new MessagePagesConfig());
	}

}
