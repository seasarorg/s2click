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
		return "javascript:void(0)";
	}

}
