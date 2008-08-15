package org.seasar.s2click.example.form;

import net.sf.click.control.Button;
import net.sf.click.control.TextField;

import org.seasar.s2click.control.AjaxRequestButton;
import org.seasar.s2click.control.AjaxSubmit;
import org.seasar.s2click.control.S2ClickForm;

public class JsonForm extends S2ClickForm {
	
	private static final long serialVersionUID = 1L;
	
	public TextField keyword = new TextField("keyword", "書籍名");
	public AjaxSubmit search = new AjaxSubmit("submit", "検索");
	public AjaxRequestButton searchAll = new AjaxRequestButton("searchAll", "全件検索");
	public Button clear = new Button("clear", "クリア");
	
	public JsonForm(String name){
		super(name);
		setFieldAutoRegisteration(true);
		
		search.addAjaxHandler(AjaxRequestButton.ON_COMPLETE, "displayResult");
		searchAll.addAjaxHandler(AjaxRequestButton.ON_COMPLETE, "displayResult");
		clear.setAttribute("onclick", "clearResult()");
	}

}
