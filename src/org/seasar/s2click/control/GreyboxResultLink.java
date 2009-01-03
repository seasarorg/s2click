package org.seasar.s2click.control;

import net.sf.click.control.AbstractLink;

import org.apache.commons.lang.StringEscapeUtils;

/**
 * {@link GreyboxButton}コントロールを使用して表示したモーダルダイアログから
 * 親画面に値を表示する際に使用可能なリンクです。
 *
 * @author Naoki Takezoe
 * @since 0.4.0
 */
public class GreyboxResultLink extends AbstractLink {

	private static final long serialVersionUID = 1L;

	public static final String HTML_IMPORTS =
		"<script type=\"text/javascript\">var S2CLICK_GB_PARENT = parent.parent;</script>\n";

	protected String targetId;
	protected String value;

	/**
	 * コンストラクタ。
	 */
	public GreyboxResultLink() {
		super();
	}

	/**
	 * コンストラクタ。
	 *
	 * @param name コンポーネント名
	 */
	public GreyboxResultLink(String name) {
		super(name);
	}

	/**
	 * コンストラクタ。
	 *
	 * @param name コンポーネント名
	 * @param targetId 値を反映する親画面の要素のid属性
	 */
	public GreyboxResultLink(String name, String targetId) {
		super(name);
		setTargetId(targetId);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getHtmlImports() {
		return HTML_IMPORTS;
	}

	public void setValue(String value){
		this.value = value;
	}

	public String getValue(){
		return this.value;
	}

	@Override
	public String getLabel(){
		if(this.label == null){
			if(this.value != null){
				return getValue();
			}
		}
		return super.getLabel();
	}

	public void setTargetId(String targetId){
		this.targetId = targetId;
	}

	public String getTargetId(){
		return this.targetId;
	}

	@Override
	public String getHref() {
		return "javascript:S2CLICK_GB_PARENT.S2Click_GB_SetResult('"
			+ StringEscapeUtils.escapeJavaScript(getValue())
			+ "', '"
			+ StringEscapeUtils.escapeJavaScript(getTargetId())
			+ "'); S2CLICK_GB_PARENT.GB_hide();";
	}

	public boolean onProcess() {
		return true;
	}

	/**
	 * このメソッドは<code>UnsupportedOperationException</code>をスローします。
	 */
	public void setListener(Object listener, String method) {
		throw new UnsupportedOperationException();
	}

}
