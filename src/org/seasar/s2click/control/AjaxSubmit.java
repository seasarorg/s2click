package org.seasar.s2click.control;

import java.util.HashMap;
import java.util.Map;

import org.seasar.s2click.util.AjaxUtils;

/**
 * 
 * @author Naoki Takezoe
 * @sibce 0.3.1
 */
public class AjaxSubmit extends ConfirmSubmit implements AjaxControl {

	private static final long serialVersionUID = 1L;
	
	protected Map<String, String> handlers = new HashMap<String, String>();
	
	public AjaxSubmit() {
		super();
	}

	public AjaxSubmit(String name, Object listener, String method) {
		super(name, listener, method);
	}

	public AjaxSubmit(String name, String label, Object listener,
			String method, String confirmMessage) {
		super(name, label, listener, method, confirmMessage);
	}

	public AjaxSubmit(String name, String label, Object listener, String method) {
		super(name, label, listener, method);
	}

	public AjaxSubmit(String name, String label) {
		super(name, label);
	}

	public AjaxSubmit(String name) {
		super(name);
	}

	public void addAjaxHandler(String event, String handler){
		this.handlers.put(event, handler);
	}
	
	public Map<String, String> getAjaxHandlers(){
		return this.handlers;
	}
	
	@Override public String toString() {
		String formId = getForm().getId();
		StringBuilder sb = new StringBuilder();
		sb.append(formId).append(".request(");
		sb.append("{");
		sb.append("method: 'post', ");
		sb.append(AjaxUtils.getOptions(handlers));
		sb.append("}); return false;");
		
		setAttribute("onclick", sb.toString());
		return super.toString();
	}
	
}
