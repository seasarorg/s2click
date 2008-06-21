package org.seasar.s2click.example.form;

import net.sf.click.control.Submit;
import net.sf.click.extras.control.IntegerField;

import org.seasar.s2click.control.AutoForm;

/**
 * �����Z�A�v���P�[�V�����̃t�H�[���N���X�B
 * 
 * @author Naoki Takezoe
 */
public class AddForm extends AutoForm {
	
	private static final long serialVersionUID = 1L;
	
	public AddForm(String name){
		super(name);
		setFieldAutoRegisteration(true);
		setJavaScriptValidation(true);
	}
	
	public IntegerField num1 = new IntegerField("num1", "���l1", true);
	public IntegerField num2 = new IntegerField("num2", "���l2", true);
	public IntegerField result = new IntegerField("result", "����");
	public Submit submit = new Submit("add", "�����Z");

}
