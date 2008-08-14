package org.seasar.s2click.control;

import java.util.HashMap;
import java.util.Map;

import org.seasar.s2click.util.AjaxUtils;

import net.sf.click.control.ActionButton;

/**
 * 
 * @author Naoki Takezoe
 * @since 0.3.1
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

	public void addAjaxHandler(String event, String handler){
		this.handlers.put(event, handler);
	}
	
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
