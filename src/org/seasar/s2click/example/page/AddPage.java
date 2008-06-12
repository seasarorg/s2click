package org.seasar.s2click.example.page;

import org.seasar.s2click.example.form.AddForm;

/**
 * �����Z�A�v���P�[�V�����̃y�[�W�N���X�B
 * 
 * @see AddForm
 * @author Naoki Takezoe
 */
public class AddPage extends LayoutPage {
	
	public AddForm form = new AddForm("form");
	
	public AddPage(){
		form.submit.setListener(this, "doAdd");
	}
	
	public boolean doAdd(){
		if(form.isValid()){
			form.result.setValue(String.valueOf(
					form.num1.getInteger() + form.num2.getInteger()));
		}
		return true;
	}
	
}
