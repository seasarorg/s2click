package org.seasar.s2click.example.form;

import net.sf.click.control.FileField;
import net.sf.click.control.Submit;

import org.seasar.s2click.control.AutoForm;
import org.seasar.s2click.example.page.FileUploadPage;

/**
 * ファイルアップロードのサンプルページで使用するフォームクラス。
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

	public FileField file = new FileField("file", "ファイル", true);
	public Submit submit = new Submit("submit", "アップロード");
}
