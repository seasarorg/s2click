package org.seasar.s2click.example.page;

import net.sf.click.Page;
import org.seasar.s2click.example.form.IndexForm;

/**
 * 足し算アプリケーションのページクラス。
 * 
 * @see IndexForm
 * @author Naoki Takezoe
 */
public class IndexPage extends Page {
	
	public IndexForm form = new IndexForm("form");
	
	public IndexPage(){
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
