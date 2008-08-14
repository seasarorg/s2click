package org.seasar.s2click.control;

import org.seasar.s2click.util.AjaxUtils;


/**
 * <tt>prototype.js</tt>の<code>Ajax.Request</code>を使用してAjaxを実現するためのアクションリンクです。
 * 
 * @author Naoki Takezoe
 * @since 0.3.1
 */
public class AjaxRequestLink extends AbstractAjaxLink {
	
	private static final long serialVersionUID = 1L;
	
	public AjaxRequestLink() {
		super();
	}

	public AjaxRequestLink(Object listener, String method) {
		super(listener, method);
	}

	public AjaxRequestLink(String name, Object listener, String method) {
		super(name, listener, method);
	}

	public AjaxRequestLink(String name, String label, Object listener, String method) {
		super(name, label, listener, method);
	}

	public AjaxRequestLink(String name, String label) {
		super(name, label);
	}

	public AjaxRequestLink(String name) {
		super(name);
	}
	
	@Override public String toString(){
		setAttribute("onclick", AjaxUtils.createAjaxRequest(getHref(getValue()), handlers));
		return super.toString();
	}
	
}
