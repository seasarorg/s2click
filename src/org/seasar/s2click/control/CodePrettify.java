/*
 * Copyright 2006-2011 the Seasar Foundation and the Others.
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

import javax.servlet.ServletContext;

import org.apache.click.control.AbstractControl;
import org.apache.click.element.CssImport;
import org.apache.click.element.Element;
import org.apache.click.element.JsImport;
import org.apache.click.util.ClickUtils;
import org.apache.click.util.HtmlStringBuffer;
import org.apache.commons.lang.StringUtils;

/**
 * <a href="http://code.google.com/p/google-code-prettify/">google-code-prettify</a>
 * を使ってソースコードをハイライト表示するコントロールです。
 *
 * @author Naoki Takezoe
 * @since 0.4.0
 */
public class CodePrettify extends AbstractControl {

	private static final long serialVersionUID = 1L;

//	public static final String HTML_IMPORTS =
//		"<script type=\"text/javascript\" src=\"{0}/resources/prettify/prettify.js\"></script>\n" +
//	    "<link href=\"{0}/resources/prettify/prettify.css\" rel=\"stylesheet\" type=\"text/css\"/>\n";

	public static final String LANG_C = "c";
	public static final String LANG_CC = "cc";
	public static final String LANG_CPP = "cpp";
	public static final String LANG_CS = "cs";
	public static final String LANG_CYC= "cyc";
	public static final String LANG_JAVA = "java";
	public static final String LANG_BSH = "bsh";
	public static final String LANG_CSH = "csh";
	public static final String LANG_SH = "sh";
	public static final String LANG_CV = "cv";
	public static final String LANG_PY = "py";
	public static final String LANG_PERL = "perl";
	public static final String LANG_PL = "pl";
	public static final String LANG_PM = "pm";
	public static final String LANG_RB = "rb";
	public static final String LANG_JS = "js";
	public static final String LANG_HTML = "html";
	public static final String LANG_XHTML = "xhtml";
	public static final String LANG_XML = "xml";
	public static final String LANG_XSL = "xsl";

	protected String code;

	protected String lang;

	public CodePrettify(){
	}

	public CodePrettify(String name){
		setName(name);
	}

	/**
	 * ハイライト表示するプログラムコードをセットします。
	 *
	 * @param code プログラムコード
	 */
	public void setCode(String code){
		this.code = code;
	}

	/**
	 * プログラムコードを取得します。
	 *
	 * @return プログラムコード
	 */
	public String getCode(){
		return this.code;
	}

	public void setLang(String lang){
		this.lang = lang;
	}

	public String getLang(){
		return this.lang;
	}

//	public String getHtmlImports() {
//        return MessageFormat.format(HTML_IMPORTS,
//        		new Object[]{ getContext().getRequest().getContextPath() }
//        );
//	}

	@Override
	public List<Element> getHeadElements() {
		if (headElements == null) {
			headElements = super.getHeadElements();
			headElements.add(new JsImport("/resources/prettify/prettify.js"));
			headElements.add(new CssImport("/resources/prettify/prettify.css"));
		}
		return headElements;
	}


	public void onDeploy(ServletContext servletContext) {
	}

	public void onDestroy() {
	}

	public void onInit() {
	}

	public boolean onProcess() {
		return true;
	}

	public void onRender() {
	}

	public void setListener(Object listener, String method) {
	}

	@Override
	public String toString() {
		HtmlStringBuffer buffer = new HtmlStringBuffer(2250);
		render(buffer);
		return buffer.toString();
	}

	@Override
	public void render(HtmlStringBuffer buffer) {
		StringBuilder sb = new StringBuilder();
		sb.append("<pre class=\"prettyprint");
		if(StringUtils.isNotEmpty(this.lang)){
			sb.append(" lang-").append(this.lang);
		}
		sb.append("\">");
		sb.append(ClickUtils.escapeHtml(code));
		sb.append("</pre>");

		@SuppressWarnings("rawtypes")
		List controls = getPage().getControls();

		int count = 0;
		for(Object control: controls){
			if(control instanceof CodePrettify){
				if(control == this){
					break;
				}
				count++;
			}
		}
		if(count == 0){
			sb.append("<script type=\"text/javascript\">\n");
			sb.append("window.onload = function(){");
			sb.append(" prettyPrint(); ");
			sb.append("}\n");
			sb.append("</script>\n");
		}

		buffer.append(sb.toString());
	}

}
