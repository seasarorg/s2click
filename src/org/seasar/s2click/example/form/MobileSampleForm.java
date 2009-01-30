package org.seasar.s2click.example.form;

import net.sf.click.control.PasswordField;
import net.sf.click.control.Submit;
import net.sf.click.control.TextField;

import org.seasar.s2click.control.MobileForm;

public class MobileSampleForm extends MobileForm {
	
	private static final long serialVersionUID = 1L;
	
	public TextField userId = new TextField("userId", "ユーザID", true);
	public PasswordField password = new PasswordField("password", "パスワード", true);
	public Submit submit = new Submit("login", "ログイン");
	
	public MobileSampleForm(String name){
		super(name);
		setFieldAutoRegisteration(true);
	}
	
}
