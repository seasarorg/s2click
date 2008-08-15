package org.seasar.s2click.control;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.seasar.s2click.util.AjaxUtils;

import net.sf.click.control.ActionButton;
import net.sf.click.util.ClickUtils;

public abstract class AbstractAjaxButton extends ActionButton {

	private static final long serialVersionUID = 1L;
	
	protected Map<String, String> handlers = new HashMap<String, String>();

	protected static Pattern pattern = Pattern.compile("'(.+?)'");	
	
	public AbstractAjaxButton() {
		super();
	}

	public AbstractAjaxButton(Object listener, String method) {
		super(listener, method);
	}

	public AbstractAjaxButton(String name, Object listener, String method) {
		super(name, listener, method);
	}

	public AbstractAjaxButton(String name, String label, Object listener,
			String method) {
		super(name, label, listener, method);
	}

	public AbstractAjaxButton(String name, String label) {
		super(name, label);
	}

	public AbstractAjaxButton(String name) {
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
	
	/**
	 * ボタンをクリックした際に呼び出すURLを取得します。
	 * 
	 * @return　ボタンをクリックした際に呼び出すURL
	 */
	protected String getUrl(){
		// URLを切り出す
		String onclick = super.getOnClick();
		Matcher matcher = pattern.matcher(onclick);
		if(matcher.find()){
			onclick = matcher.group(1);
		}
		return onclick;
	}
	
}
