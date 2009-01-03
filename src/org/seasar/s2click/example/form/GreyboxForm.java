package org.seasar.s2click.example.form;

import net.sf.click.control.TextField;

import org.seasar.s2click.control.GreyboxButton;
import org.seasar.s2click.control.S2ClickForm;
import org.seasar.s2click.example.page.GreyboxSelectButtonPage;
import org.seasar.s2click.example.page.GreyboxSelectLinkPage;

/**
 *
 * @author Naoki Takezoe
 * @since 0.4.0
 */
public class GreyboxForm extends S2ClickForm {

	private static final long serialVersionUID = 1L;

	public TextField userId = new TextField("userId", "ユーザID");

	public GreyboxButton button1 = new GreyboxButton("button1", "リンクで選択", "ユーザを選択", GreyboxSelectLinkPage.class);

	public GreyboxButton button2 = new GreyboxButton("button2", "ダイアログで手入力", "ユーザIDを入力", GreyboxSelectButtonPage.class);

	public GreyboxForm(String name){
		super(name);
		userId.setReadonly(true);
		setFieldAutoRegisteration(true);
	}

}
