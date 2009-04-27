package org.seasar.s2click.example.page;

import org.apache.click.ActionListener;
import org.apache.click.Control;
import org.seasar.s2click.S2ClickPage;
import org.seasar.s2click.example.form.IndexForm;

/**
 * 足し算アプリケーションのページクラス。
 * 
 * @see IndexForm
 * @author Naoki Takezoe
 */
public class IndexPage extends S2ClickPage {

	public IndexForm form = new IndexForm("form");

	public IndexPage(){
		form.submit.setActionListener(new ActionListener(){
			public boolean onAction(Control source) {
				if(form.isValid()){
					form.result.setValue(String.valueOf(
							form.num1.getInteger() + form.num2.getInteger()));
				}
				return true;
			}
		});
	}

}
