package org.seasar.s2click.example.page;

import java.util.ArrayList;
import java.util.List;

import net.sf.click.control.PageLink;

import org.seasar.s2click.S2ClickPage;
import org.seasar.s2click.control.PaginateList;
import org.seasar.s2click.example.form.MobileSampleForm;

/**
 * モバイル向け機能のサンプルページです。
 * 
 * @author Naoki Takezoe
 */
public class MobileSamplePage extends S2ClickPage {
	
	public MobileSampleForm form = new MobileSampleForm("form");
	public PageLink backLink = new PageLink("backLink", "戻る", IndexPage.class);
	
	public List<String> list = new ArrayList<String>();
	public PaginateList paginateList = new PaginateList("paginateList");
	
	public MobileSamplePage(){
		form.setListener(this, "doLogin");
		
		list.add("Java");
		list.add("C#");
		list.add("Perl");
		list.add("Ruby");
		list.add("Python");
		
		paginateList.setRowList(list);
		paginateList.setPageSize(3);
		paginateList.setTemplatePath("/paginateList.htm");
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
