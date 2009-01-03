package org.seasar.s2click.control;

import net.sf.click.control.Button;

/**
 * {@link GreyboxButton}コントロールを使用して表示したモーダルダイアログから
 * 親画面に値を表示する際に使用可能なボタンです。
 * <p>
 * 以下の2通りの方法で値を親画面に反映することができます。
 * <ol>
 *   <li>
 *     {@link #setApplyFunction(String)}で値を反映するための関数を指定する
 *     <p>
 *       ボタンのクリック時に任意のJavaScript関数を呼び出すことができます。
 *       指定した関数内で親画面への値の反映処理を実装します。
 *     </p>
 *   </li>
 *   <li>
 *     {@link #setSourceId(String)}、{@link #setTargetId(String)}で値の反映元、反映先要素を指定する
 *     <p>
 *       値の取得元、反映先要素のid属性を指定することで、ボタンのクリック時に値を反映することができます。
 *       ただし、上述の<code>setApplyFunction()</code>が設定されている場合はそちらが優先されます。
 *     </p>
 *   </li>
 * </ol>
 *
 * @author Naoki Takezoe
 * @since 0.4.0
 */
public class GreyboxResultButton extends Button {

	private static final long serialVersionUID = 1L;

	public static final String HTML_IMPORTS =
		"<script type=\"text/javascript\">var S2CLICK_GB_PARENT = parent.parent;</script>\n";

	protected String targetId;
	protected String sourceId;
	protected String applyFunction;

	public GreyboxResultButton() {
		super();
	}

	public GreyboxResultButton(String name, String label) {
		super(name, label);
	}

	public GreyboxResultButton(String name) {
		super(name);
	}

	public GreyboxResultButton(String name, String label, String applyFunction) {
		super(name, label);
		setApplyFunction(applyFunction);
	}

	public GreyboxResultButton(String name, String label, String sourceId, String targetId) {
		super(name, label);
		setSourceId(sourceId);
		setTargetId(targetId);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getHtmlImports() {
		return HTML_IMPORTS;
	}

	/**
	 * 結果を親画面に反映するためのJavaScript関数を取得します。
	 *
	 * @return JavaScript関数名
	 */
	public String getApplyFunction() {
		return applyFunction;
	}

	/**
	 * 結果を親画面に反映するためのJavaScript関数を設定します。
	 * <p>
	 * このメソッドで指定したJavaScript関数で親画面への値の反映処理を実装します。
	 * 関数の実行後、ダイアログは自動的に閉じるため、
	 * このメソッドで指定した関数内にダイアログを閉じるためのコードを記述する必要はありません。
	 *
	 * @param applyFunction JavaScript関数名
	 */
	public void setApplyFunction(String applyFunction) {
		this.applyFunction = applyFunction;
	}

	/**
	 * 反映先要素（親画面）のid属性の値を取得します。
	 *
	 * @return 反映先要素のid属性の値
	 */
	public String getTargetId() {
		return targetId;
	}

	/**
	 * 反映先要素（親画面）のid属性の値を設定します。
	 * <p>
	 * ボタンをクリックすると反映元要素の値を反映先用にコピーし、ダイアログが閉じます。
	 * ただし、{@link #setApplyFunction(String)}で反映用の関数が設定されている場合はそちらが優先され、
	 * このメソッドで設定した内容は無視されます。
	 *
	 * @param targetId  反映先要素のid属性の値
	 */
	public void setTargetId(String targetId) {
		this.targetId = targetId;
	}

	/**
	 * 反映元要素のid属性の値を取得します。
	 *
	 * @return 反映元要素のid属性の値
	 */
	public String getSourceId() {
		return sourceId;
	}

	/**
	 * 反映元要素のid属性の値を設定します。
	 * <p>
	 * ボタンをクリックすると反映元要素の値を反映先用にコピーし、ダイアログが閉じます。
	 * ただし、{@link #setApplyFunction(String)}で反映用の関数が設定されている場合はそちらが優先され、
	 * このメソッドで設定した内容は無視されます。
	 *
	 * @param sourceId 反映元要素のid属性の値
	 */
	public void setSourceId(String sourceId) {
		this.sourceId = sourceId;
	}

	@Override
	public String toString(){
		// onClick属性を設定
		StringBuilder sb = new StringBuilder();

		if(getApplyFunction() != null){
			sb.append(getApplyFunction()).append("(); ");

		} else {
			sb.append("S2CLICK_GB_PARENT.S2Click_GB_SetResult(");
			sb.append("document.getElementById('" + getSourceId()).append("').value, ");
			sb.append("'").append(getTargetId()).append("'); ");
		}
		sb.append("S2CLICK_GB_PARENT.GB_hide();");

		setOnClick(sb.toString());

		return super.toString();
	}


}
