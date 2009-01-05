package org.seasar.s2click.example.page;

import org.seasar.s2click.annotation.Path;
import org.seasar.s2click.example.form.ControlsForm;
import org.seasar.s2click.util.S2ClickUtils;

@Path("/controls.htm")
public class ControlsPage extends LayoutPage {
	
	public String title = "Click標準のコントロール";
	public String template = "/form.htm";
	public ControlsForm form = new ControlsForm("form");
	
	public ControlsPage(){
		form.submit.setListener(this, "doSubmit");
	}
	
	public boolean doSubmit(){
		if(form.isValid()){
			S2ClickUtils.convertToHidden(form);
			
			// 送信ボタンを削除して戻るボタンを追加
			form.submit.setLabel("戻る");
			form.submit.setListener(this, "doBack");
			
			template = "/controls-confirm.htm";
			return false;
		}
		return true;
	}
	
	public boolean doBack(){
		return true;
	}
	
}
