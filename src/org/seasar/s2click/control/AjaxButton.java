package org.seasar.s2click.control;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sf.click.control.ActionButton;
import net.sf.click.util.ClickUtils;

import org.apache.commons.lang.StringUtils;
import org.seasar.s2click.util.AjaxUtils;

/**
 * <tt>prototype.js</tt>の<code>Ajax.Request</code>、<code>Ajax.Updater</code>を使用して
 * Ajaxを実現するためのアクションボタンです。
 * 
 * @author Naoki Takezoe
 * @since 0.4.0
 */
public class AjaxButton extends ActionButton {

	private static final long serialVersionUID = 1L;
	
	protected Map<String, String> handlers = new HashMap<String, String>();

	protected static Pattern pattern = Pattern.compile("'(.+?)'");	

	protected String elementId;
	
	public AjaxButton() {
		super();
	}

	public AjaxButton(Object listener, String method) {
		super(listener, method);
	}

	public AjaxButton(String name, Object listener, String method) {
		super(name, listener, method);
	}

	public AjaxButton(String name, String label, Object listener,
			String method) {
		super(name, label, listener, method);
	}

	public AjaxButton(String name, String label) {
		super(name, label);
	}

	public AjaxButton(String name) {
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
	
	/**
	 * 更新するHTML要素のidを取得します。
	 * @return 更新するHTML要素のid
	 */
	public String getElementId() {
		return elementId;
	}

	/**
	 * 更新するHTML要素のidを設定します。
	 * @param elementId 更新するHTML要素のid
	 */
	public void setElementId(String elementId) {
		this.elementId = elementId;
	}
	
	@SuppressWarnings("unchecked")
	@Override public String getOnClick() {
		if(StringUtils.isEmpty(getElementId())){
			return AjaxUtils.createAjaxRequest(getUrl(), handlers, getParameters());
		} else {
			return AjaxUtils.createAjaxUpdater(
					getElementId(), getUrl(), handlers, getParameters());
		}
	}

}
