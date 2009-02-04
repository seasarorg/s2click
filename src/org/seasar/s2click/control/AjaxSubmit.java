package org.seasar.s2click.control;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

import net.sf.click.control.Submit;
import net.sf.click.util.ClickUtils;

import org.seasar.s2click.util.AjaxUtils;

/**
 * フォームの内容を<tt>prototype.js</tt>の<code>Ajax.Request</code>を使って
 * 送信するための<code>Submit</code>コントロール。
 * 
 * @author Naoki Takezoe
 * @since 0.4.0
 */
public class AjaxSubmit extends Submit {

	private static final long serialVersionUID = 1L;
	
	protected Map<String, String> handlers = new HashMap<String, String>();
	
	public AjaxSubmit() {
		super();
	}

	public AjaxSubmit(String name, Object listener, String method) {
		super(name, listener, method);
	}

//	public AjaxSubmit(String name, String label, Object listener,
//			String method, String confirmMessage) {
//		super(name, label, listener, method, confirmMessage);
//	}

	public AjaxSubmit(String name, String label, Object listener, String method) {
		super(name, label, listener, method);
	}

	public AjaxSubmit(String name, String label) {
		super(name, label);
	}

	public AjaxSubmit(String name) {
		super(name);
	}

    public String getHtmlImports() {
        Object[] args = {
            getContext().getRequest().getContextPath(),
            ClickUtils.getResourceVersionIndicator(getContext()),
        };
        
        return MessageFormat.format(AjaxUtils.HTML_IMPORTS, args);
    }
	
	public void addAjaxHandler(String event, String handler){
		this.handlers.put(event, handler);
	}
	
	public Map<String, String> getAjaxHandlers(){
		return this.handlers;
	}
	
	@Override public String toString() {
		String formId = getForm().getId();
//		StringBuilder sb = new StringBuilder();
//		sb.append(formId).append(".request(");
//		sb.append("{");
//		sb.append("method: 'post', ");
//		sb.append(AjaxUtils.getOptions(handlers));
//		sb.append("}); return false;");
//		
//		setAttribute("onclick", sb.toString());
		
		StringBuilder sb = new StringBuilder();
		sb.append("<script type=\"text/javascript\">\n");
		sb.append("$('").append(formId).append("').onsubmit = ");
		sb.append("function(){\n");
		sb.append("  this.request({ method: 'post', ");
		sb.append(AjaxUtils.getOptions(handlers));
		sb.append("}); return false;");
		sb.append("}\n");
		sb.append("</script>\n");
		
		return super.toString() + sb.toString();
	}
	
}
