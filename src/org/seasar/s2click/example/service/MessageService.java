package org.seasar.s2click.example.service;

import java.util.List;

import org.seasar.extension.jdbc.service.S2AbstractService;
import org.seasar.s2click.example.entity.Message;

public class MessageService extends S2AbstractService<Message> {
	
	public List<Message> getMessages(){
		return jdbcManager.from(Message.class).orderBy("messageId desc").getResultList();
	}
	
}
