package org.seasar.s2click.example.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class Message {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	public Integer messageId;
	
	public String name;
	
	public String message;
	
	@Temporal(TemporalType.TIMESTAMP)
	public Date date;
	
}
