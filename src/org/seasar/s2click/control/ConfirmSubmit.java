package org.seasar.s2click.control;

import net.sf.click.control.Submit;

import org.apache.commons.lang.StringEscapeUtils;

/**
 * 確認ダイアログを表示する機能を備えた<code>Submit</code>の拡張コントロール。
 * 
 * @author Naoki Takezoe
 */
public class ConfirmSubmit extends Submit {

	private static final long serialVersionUID = 1L;

	public ConfirmSubmit() {
		super();
	}

	public ConfirmSubmit(String name, Object listener, String method) {
		super(name, listener, method);
	}

	public ConfirmSubmit(String name, String label, Object listener,
			String method) {
		super(name, label, listener, method);
	}

	public ConfirmSubmit(String name, String label, Object listener,
			String method, String confirmMessage) {
		super(name, label, listener, method);
		setConfirmMessage(confirmMessage);
	}
	
	public ConfirmSubmit(String name, String label) {
		super(name, label);
	}

	public ConfirmSubmit(String name) {
		super(name);
	}
	
	public void setConfirmMessage(String message){
		setAttribute("onclick", "return confirm('" 
				+ StringEscapeUtils.escapeJavaScript(message) + "');");
	}
	
}
