package org.seasar.s2click.example.page;

import java.util.Date;

import javax.annotation.Resource;

import org.seasar.extension.jdbc.JdbcManager;
import org.seasar.s2click.example.entity.Message;
import org.seasar.s2click.example.form.MessageForm;

/**
 * S2Click + S2JDBCのテストページ。
 * 
 * @author Naoki Takezoe
 */
public class JdbcPage extends LayoutPage {
	
	@Resource
	protected JdbcManager jdbcManager;
	
	public String title = "S2JDBC";
	public MessageForm form = new MessageForm("form");

	public JdbcPage(){
		form.submit.setListener(this, "doAdd");
	}
	
	@Override public void onRender() {
		addModel("messageList", jdbcManager.from(
				Message.class).orderBy("messageId desc").getResultList());
	}
	
	public boolean doAdd(){
		if(form.isValid()){
			Message message = new Message();
			message.name = form.name.getValue();
			message.message = form.message.getValue();
			message.date = new Date();
			jdbcManager.insert(message).execute();
			
			setRedirect(JdbcPage.class);
			return false;
		}
		return true;
	}
	
}
