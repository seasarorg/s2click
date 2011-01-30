package org.seasar.s2click.example.page;

import org.seasar.s2click.EntityRegisterPage;
import org.seasar.s2click.annotation.Layout;

@Layout
public class MessageRegisterPage extends EntityRegisterPage {

	private static final long serialVersionUID = 1L;

	public MessageRegisterPage() {
		super(new MessagePagesConfig());
	}

}
