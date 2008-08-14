package org.seasar.s2click.control;

import java.util.HashMap;
import java.util.Map;

import org.seasar.s2click.util.AjaxUtils;

import net.sf.click.control.ActionButton;

/**
 * <tt>prototype.js</tt>の<code>Ajax.Request</code>を使用してAjaxを実現するためのアクションボタンです。
 * 
 * @author Naoki Takezoe
 * @since 0.4.0
 */
public class AjaxRequestButton extends ActionButton implements AjaxControl {

	private static final long serialVersionUID = 1L;

	protected Map<String, String> handlers = new HashMap<String, String>();
	
	public AjaxRequestButton() {
		super();
	}

	public AjaxRequestButton(Object listener, String method) {
		super(listener, method);
	}

	public AjaxRequestButton(String name, Object listener, String method) {
		super(name, listener, method);
	}

	public AjaxRequestButton(String name, String label, Object listener,
			String method) {
		super(name, label, listener, method);
	}

	public AjaxRequestButton(String name, String label) {
		super(name, label);
	}

	public AjaxRequestButton(String name) {
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

	@Override public String getOnClick() {
		// URLを切り出す
		String onclick = super.getOnClick();
		onclick = onclick.replaceFirst("^javascript:document\\.location\\.href='", "");
		onclick = onclick.replaceFirst("';$", "");
		
		return AjaxUtils.createAjaxRequest(onclick, handlers);
	}

}
