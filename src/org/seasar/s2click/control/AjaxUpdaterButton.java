package org.seasar.s2click.control;

import org.seasar.s2click.util.AjaxUtils;

public class AjaxUpdaterButton extends AbstractAjaxButton {

	private static final long serialVersionUID = 1L;

	protected String elementId;
	
	public AjaxUpdaterButton() {
		super();
	}

	public AjaxUpdaterButton(Object listener, String method) {
		super(listener, method);
	}

	public AjaxUpdaterButton(String name, Object listener, String method) {
		super(name, listener, method);
	}

	public AjaxUpdaterButton(String name, String label, Object listener,
			String method) {
		super(name, label, listener, method);
	}

	public AjaxUpdaterButton(String name, String label) {
		super(name, label);
	}

	public AjaxUpdaterButton(String name) {
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
	@Override public String getOnClick() {
		return AjaxUtils.createAjaxUpdater(
				getElementId(), getUrl(), handlers, getParameters());
	}

}
