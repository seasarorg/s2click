package org.seasar.s2click.control;

import org.seasar.s2click.S2ClickUtils;

import net.sf.click.control.Submit;

/**
 * �m�F�_�C�A���O��\������@�\�������<code>Submit</code>�̊g���R���g���[���B
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
				+ S2ClickUtils.escapeJavaScript(message) + "');");
	}
	
}
