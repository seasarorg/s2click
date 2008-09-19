package org.seasar.s2click.example.page;

import java.util.Date;

import org.seasar.extension.jdbc.JdbcManager;
import org.seasar.framework.container.annotation.tiger.Binding;
import org.seasar.s2click.example.entity.Message;
import org.seasar.s2click.example.form.MessageForm;

/**
 * S2Click + S2JDBCのテストページ。
 * 
 * @author Naoki Takezoe
 */
public class JdbcPage extends LayoutPage {
	
	@Binding
	protected JdbcManager jdbcManager;
	
	public String title = "S2JDBC";
	public MessageForm messageForm = new MessageForm("messageForm");

	public JdbcPage(){
		messageForm.submit.setListener(this, "doAdd");
	}
	
	@Override public void onRender() {
		addModel("messageList", jdbcManager.from(
				Message.class).orderBy("messageId desc").getResultList());
	}
	
	public boolean doAdd(){
		if(messageForm.isValid()){
			Message message = new Message();
			message.name = messageForm.name.getValue();
			message.message = messageForm.message.getValue();
			message.date = new Date();
			jdbcManager.insert(message).execute();
			
			setRedirect(JdbcPage.class);
			return false;
		}
		return true;
	}
	
}
