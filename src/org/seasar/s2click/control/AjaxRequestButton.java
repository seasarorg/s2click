package org.seasar.s2click.control;

import org.seasar.s2click.util.AjaxUtils;

/**
 * <tt>prototype.js</tt>の<code>Ajax.Request</code>を使用してAjaxを実現するためのアクションボタンです。
 * 
 * @author Naoki Takezoe
 * @since 0.4.0
 */
public class AjaxRequestButton extends AbstractAjaxButton {

	private static final long serialVersionUID = 1L;

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
	
	@SuppressWarnings("unchecked")
	@Override public String getOnClick() {
		return AjaxUtils.createAjaxRequest(getUrl(), handlers, getParameters());
	}

}
