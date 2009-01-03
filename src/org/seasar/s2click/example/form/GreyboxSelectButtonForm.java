package org.seasar.s2click.example.form;

import net.sf.click.control.TextField;

import org.seasar.s2click.control.GreyboxResultButton;
import org.seasar.s2click.control.S2ClickForm;

/**
 *
 * @author Naoki Takezoe
 * @since 0.4.0
 */
public class GreyboxSelectButtonForm extends S2ClickForm {

	private static final long serialVersionUID = 1L;

	public TextField userId = new TextField("userId", "ユーザID");

	public GreyboxResultButton button = new GreyboxResultButton("button", "決定", "form_userId", "form_userId");

	public GreyboxSelectButtonForm(String name){
		super(name);
		setFieldAutoRegisteration(true);
	}

}
