package org.seasar.s2click.control;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

import org.seasar.s2click.util.AjaxUtils;

import net.sf.click.control.ActionLink;
import net.sf.click.util.ClickUtils;

/**
 * Ajax用のアクションリンクの抽象基底クラスです。
 * 
 * @author Naoki Takezoe
 * @since 0.4.0
 */
public abstract class AbstractAjaxLink extends ActionLink {
	
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
	
    public String getHtmlImports() {
        Object[] args = {
            getContext().getRequest().getContextPath(),
            ClickUtils.getResourceVersionIndicator(getContext()),
        };
        
        return MessageFormat.format(AjaxUtils.HTML_IMPORTS, args);
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
