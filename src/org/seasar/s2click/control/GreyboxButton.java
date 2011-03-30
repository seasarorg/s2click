/*
 * Copyright 2006-2010 the Seasar Foundation and the Others.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package org.seasar.s2click.control;

import java.util.List;

import org.apache.click.Page;
import org.apache.click.control.Button;
import org.apache.click.element.CssImport;
import org.apache.click.element.Element;
import org.apache.click.element.JsImport;
import org.apache.click.element.JsScript;
import org.apache.click.util.HtmlStringBuffer;
import org.apache.commons.lang.StringEscapeUtils;

/**
 * <a href="http://orangoo.com/labs/GreyBox/">GreyBox</a>を使用して指定したページを
 * モーダルダイアログとして表示するボタンです。
 *
 * @author Naoki Takezoe
 * @since 0.4.0
 */
public class GreyboxButton extends Button {

	private static final long serialVersionUID = 1L;

	public static final String HTML_IMPORTS =
//		"<script type=\"text/javascript\">var GB_ROOT_DIR = \"{0}/js/greybox/\";</script>\n" +
		"<script type=\"text/javascript\" src=\"{0}/resources/greybox/AJS.js\"></script>\n" +
		"<script type=\"text/javascript\" src=\"{0}/resources/greybox/AJS_fx.js\"></script>\n" +
		"<script type=\"text/javascript\" src=\"{0}/resources/greybox/gb_scripts.js\"></script>\n" +
		"<link href=\"{0}/resources/greybox/gb_styles.css\" rel=\"stylesheet\" type=\"text/css\" media=\"all\" />\n" +
		"<script type=\"text/javascript\">function S2Click_GB_SetResult(data, id)'{ AJS.$(id).value = data; }'</script>\n";

	protected String dialogTitle = "";
	protected int width = 400;
	protected int height = 300;
	protected Class<? extends Page> pageClass;

	public GreyboxButton() {
		super();
	}

	public GreyboxButton(String name) {
		super(name);
	}

	public GreyboxButton(String name, String label) {
		super(name, label);
	}

	public GreyboxButton(String name, String label, String dialogTitle, Class<? extends Page> pageClass) {
		super(name, label);
		setPageClass(pageClass);
		setDialogTitle(dialogTitle);
	}

	@Override
	public List<Element> getHeadElements() {
		if (headElements == null) {
			headElements = super.getHeadElements();
			headElements.add(new JsImport("/resources/greybox/AJS.js"));
			headElements.add(new JsImport("/resources/greybox/AJS_fx.js"));
			headElements.add(new JsImport("/resources/greybox/gb_scripts.js"));
			headElements.add(new CssImport("/click/greybox/gb_styles.css"));
			headElements.add(new JsScript("function S2Click_GB_SetResult(data, id)'{ AJS.$(id).value = data; }'"));
		}
		return headElements;
	}

	/**
	 * greyboxによるモーダルダイアログとして表示するページクラスを設定します。
	 *
	 * @param pageClass 表示するページクラス
	 */
	public void setPageClass(Class<? extends Page> pageClass){
		this.pageClass = pageClass;
	}

	/**
	 * greyboxによるモーダルダイアログとして表示するページクラスを設定します。
	 *
	 * @return 表示するページクラス
	 */
	public Class<? extends Page> getPageClass(){
		return this.pageClass;
	}

	/**
	 * greyboxによるモーダルダイアログのタイトルを設定します。
	 *
	 * @param dialogTitle ダイアログのタイトル
	 */
	public void setDialogTitle(String dialogTitle){
		this.dialogTitle = dialogTitle;
	}

	/**
	 * greyboxによるモーダルダイアログのタイトルを取得します。
	 *
	 * @return ダイアログのタイトル
	 */
	public String getDialogTitle(){
		if(this.dialogTitle == null){
			return "";
		}
		return this.dialogTitle;
	}

	/**
	 * ダイアログの幅を設定します。
	 *
	 * @param width ダイアログの幅
	 */
	public void setDialogWidth(int width){
		this.width = width;
	}

	/**
	 * ダイアログの幅を取得します。
	 *
	 * @return ダイアログの幅
	 */
	public int getDialogWidth(){
		return this.width;
	}

	/**
	 * ダイアログの高さを設定します。
	 *
	 * @param height ダイアログの高さ
	 */
	public void setDialogHeight(int height){
		this.height = height;
	}

	/**
	 * ダイアログの高さを取得します。
	 *
	 * @return ダイアログの高さ
	 */
	public int getDialogHeight(){
		return this.height;
	}

	@Override
	public void render(HtmlStringBuffer buffer){
		// ページクラスのパスを取得
		Class<? extends Page> pageClass = getPageClass();
		String pagePath = getContext().getPagePath(pageClass);

		// onClick属性を設定
		StringBuilder sb = new StringBuilder();

		sb.append("GB_showCenter('").append(StringEscapeUtils.escapeJavaScript(getDialogTitle()));
		sb.append("'").append(", '../..").append(pagePath).append("'");

		if(getDialogHeight() > 0){
			sb.append(", ").append(getDialogHeight());
		}

		if(getDialogWidth() > 0){
			sb.append(", ").append(getDialogWidth());
		}

		sb.append(")");

		setOnClick(sb.toString());

		super.render(buffer);
	}

}
