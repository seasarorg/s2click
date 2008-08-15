package org.seasar.s2click.control;

import org.seasar.s2click.util.AjaxUtils;

/**
 * <tt>prototype.js</tt>の<code>Ajax.Updater</code>を使用してHTMLの一部を更新するためのアクションリンクです。
 * 
 * @author Naoki Takezoe
 */
public class AjaxUpdaterLink extends AbstractAjaxLink {

	private static final long serialVersionUID = 4140627004690591851L;

	protected String elementId;
	
	public AjaxUpdaterLink() {
		super();
	}

	public AjaxUpdaterLink(Object listener, String method) {
		super(listener, method);
	}

	public AjaxUpdaterLink(String name, Object listener, String method) {
		super(name, listener, method);
	}

	public AjaxUpdaterLink(String name, String label, Object listener,
			String method) {
		super(name, label, listener, method);
	}

	public AjaxUpdaterLink(String name, String label) {
		super(name, label);
	}

	public AjaxUpdaterLink(String name) {
		super(name);
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
	@Override public String toString(){
		setAttribute("onclick", AjaxUtils.createAjaxUpdater(
				getElementId(), getHref(getValue()), handlers, getParameters()));
		return super.toString();
	}
	
}
