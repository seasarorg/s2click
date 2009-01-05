package org.seasar.s2click.example.form;

import net.sf.click.control.TextField;

import org.seasar.s2click.control.GreyboxButton;
import org.seasar.s2click.control.S2ClickForm;
import org.seasar.s2click.example.page.GreyboxSelectPage;

/**
 *
 * @author Naoki Takezoe
 * @since 0.4.0
 */
public class GreyboxForm extends S2ClickForm {

	private static final long serialVersionUID = 1L;

	public TextField userId = new TextField("userId", "ユーザID");

	public GreyboxButton button = new GreyboxButton("button", "参照...", "ユーザを選択", GreyboxSelectPage.class);

	public GreyboxForm(String name){
		super(name);
		userId.setReadonly(true);
		setFieldAutoRegisteration(true);
	}

}
