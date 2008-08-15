package org.seasar.s2click.control;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sf.click.control.ActionButton;
import net.sf.click.util.ClickUtils;

import org.seasar.s2click.util.AjaxUtils;

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
	
    public String getHtmlImports() {
        Object[] args = {
            getContext().getRequest().getContextPath(),
            ClickUtils.getResourceVersionIndicator(getContext()),
        };
        
        return MessageFormat.format(HTML_IMPORTS, args);
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

	Pattern pattern = Pattern.compile("'(.+?)'");
	
	@Override public String getOnClick() {
		// URLを切り出す
		String onclick = super.getOnClick();
		Matcher matcher = pattern.matcher(onclick);
		if(matcher.find()){
			onclick = matcher.group(1);
		}
		
		return AjaxUtils.createAjaxRequest(onclick, handlers);
	}

}
