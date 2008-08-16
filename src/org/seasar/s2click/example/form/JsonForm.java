package org.seasar.s2click.example.form;

import net.sf.click.control.Button;
import net.sf.click.control.TextField;

import org.seasar.s2click.control.AjaxButton;
import org.seasar.s2click.control.AjaxSubmit;
import org.seasar.s2click.control.S2ClickForm;
import org.seasar.s2click.util.AjaxUtils;

public class JsonForm extends S2ClickForm {
	
	private static final long serialVersionUID = 1L;
	
	public TextField keyword = new TextField("keyword", "書籍名");
	public AjaxSubmit search = new AjaxSubmit("submit", "検索");
	public AjaxButton searchAll = new AjaxButton("searchAll", "全件検索");
	public Button clear = new Button("clear", "クリア");
	
	public JsonForm(String name){
		super(name);
		setFieldAutoRegisteration(true);
		
		search.addAjaxHandler(AjaxUtils.ON_COMPLETE, "displayResult");
		searchAll.addAjaxHandler(AjaxUtils.ON_COMPLETE, "displayResult");
		clear.setAttribute("onclick", "clearResult()");
	}

}
