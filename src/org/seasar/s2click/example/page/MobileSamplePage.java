package org.seasar.s2click.example.page;

import net.sf.click.control.PageLink;

import org.seasar.s2click.S2ClickPage;
import org.seasar.s2click.example.form.MobileSampleForm;

public class MobileSamplePage extends S2ClickPage {
	
	public MobileSampleForm form = new MobileSampleForm("form");
	public PageLink backLink = new PageLink("backLink", "戻る", IndexPage.class);
	
	public MobileSamplePage(){
		form.setListener(this, "doLogin");
	}
	
	public boolean doLogin(){
		if(form.isValid()){
			if(form.userId.getValue().equals("test") 
					&& form.password.getValue().equals("test")){
				addModel("result", "ログイン成功");
				
			} else {
				addModel("result", "ログイン失敗");
			}
		}
		return true;
	}
	
}
