package org.seasar.s2click.example.page;

import net.sf.click.Page;
import net.sf.click.control.Form;
import net.sf.click.control.Submit;
import net.sf.click.extras.control.IntegerField;

public class AddPage extends Page {
	
	public Form form = new Form("form");
	private IntegerField num1 = new IntegerField("num1", true);
	private IntegerField num2 = new IntegerField("num2", true);
	private IntegerField result = new IntegerField("result");
	private Submit submit = new Submit("Add", this, "doAdd");
	
	public AddPage(){
		form.add(num1);
		form.add(num2);
		form.add(result);
		form.add(submit);
	}
	
	public boolean doAdd(){
		if(form.isValid()){
			System.out.println(num1.getInteger() + num2.getInteger());
			result.setValue(String.valueOf(num1.getInteger() + num2.getInteger()));
		}
		return true;
	}
	
}
