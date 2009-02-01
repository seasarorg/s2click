package org.seasar.s2click.example.page;

import org.seasar.s2click.example.form.AddForm;

/**
 * JSPのサンプルページ。
 * 
 * @author Naoki Takezoe
 */
public class JspExamplePage extends JspLayoutPage {
	
	public String title = "JSPのサンプル";
	
	public AddForm form = new AddForm("form");
	
	public JspExamplePage(){
		form.submit.setListener(this, "doAdd");
	}
	
	public boolean doAdd(){
		if(form.isValid()){
			form.result.setInteger(form.num1.getInteger() + form.num2.getInteger());
		}
		return true;
	}
	
}
