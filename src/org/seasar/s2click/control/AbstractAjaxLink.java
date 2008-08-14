package org.seasar.s2click.control;

import java.util.HashMap;
import java.util.Map;

import net.sf.click.control.ActionLink;

/**
 * Ajax用のアクションリンクの抽象基底クラスです。
 * 
 * @author Naoki Takezoe
 * @since 0.3.1
 */
public abstract class AbstractAjaxLink extends ActionLink implements AjaxControl {
	
	protected Map<String, String> handlers = new HashMap<String, String>();
	
	public AbstractAjaxLink() {
		super();
	}

	public AbstractAjaxLink(Object listener, String method) {
		super(listener, method);
	}

	public AbstractAjaxLink(String name, Object listener, String method) {
		super(name, listener, method);
	}

	public AbstractAjaxLink(String name, String label, Object listener,
			String method) {
		super(name, label, listener, method);
	}

	public AbstractAjaxLink(String name, String label) {
		super(name, label);
	}

	public AbstractAjaxLink(String name) {
		super(name);
	}

	/*
	 * (non-Javadoc)
	 * @see org.seasar.s2click.control.AjaxControl#addAjaxHandler(java.lang.String, java.lang.String)
	 */
	public void addAjaxHandler(String event, String handler){
		this.handlers.put(event, handler);
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.seasar.s2click.control.AjaxControl#getAjaxHandlers()
	 */
	public Map<String, String> getAjaxHandlers(){
		return this.handlers;
	}
	
	@Override public String getHref() {
		// TODO void(0);
		return "#";
	}

//	@Override public String getHref(Object value) {
//		// TODO void(0);
//		return "#";
//	}

	protected String getHandlersOption(){
		StringBuilder sb = new StringBuilder();
		for(Map.Entry<String, String> entry: handlers.entrySet()){
			if(sb.length() != 0){
				sb.append(", ");
			}
			sb.append(entry.getKey()).append(": ").append(entry.getValue());
		}
		return sb.toString();
	}

}
