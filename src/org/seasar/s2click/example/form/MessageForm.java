package org.seasar.s2click.example.form;

import net.sf.click.control.Submit;
import net.sf.click.control.TextField;

import org.seasar.s2click.control.S2ClickForm;

public class MessageForm extends S2ClickForm {

	private static final long serialVersionUID = 1L;
	
	public TextField name = new TextField("name", true);
	public TextField message = new TextField("message", true);
	public Submit submit = new Submit("add");
	
	public MessageForm(String name){
		super(name);
		message.setSize(40);
		addConfirmMessage(submit.getName(), "メッセージを送信します。よろしいですか？");
		setFieldAutoRegisteration(true);
		setJavaScriptValidation(true);
	}

}
