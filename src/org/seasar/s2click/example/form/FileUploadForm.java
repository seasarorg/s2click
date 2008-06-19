package org.seasar.s2click.example.form;

import net.sf.click.control.FileField;
import net.sf.click.control.Submit;

import org.seasar.s2click.control.AutoForm;
import org.seasar.s2click.example.page.FileUploadPage;

/**
 * �t�@�C���A�b�v���[�h�̃T���v���y�[�W�Ŏg�p����t�H�[���N���X�B
 * 
 * @see FileUploadPage
 * @author Naoki Takezoe
 */
public class FileUploadForm extends AutoForm {
	
	private static final long serialVersionUID = 1L;
	
	public FileUploadForm(String name){
		super(name);
		setFieldAutoRegisteration(true);
		setJavaScriptValidation(true);
	}

	public FileField file = new FileField("file", "�t�@�C��", true);
	public Submit submit = new Submit("submit", "�A�b�v���[�h");
}
