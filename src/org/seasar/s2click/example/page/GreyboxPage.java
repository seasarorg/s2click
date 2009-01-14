package org.seasar.s2click.example.page;

import org.seasar.s2click.control.GreyboxButton;
import org.seasar.s2click.control.GreyboxLink;
import org.seasar.s2click.example.form.GreyboxForm;

/**
 * {@link GreyboxButton}と{@link GreyboxLink}のサンプルです。
 * <p>
 * Greyboxを使用して値を選択するためのモーダルダイアログを表示します。
 * 
 * @author Naoki Takezoe
 * @since 0.4.0
 */
public class GreyboxPage extends LayoutPage {

	public String title = "GreyBox";
	
	public GreyboxLink link = new GreyboxLink("link", "参照...", "ユーザを選択", GreyboxSelectPage.class);

	public GreyboxForm form = new GreyboxForm("form");
	
}
