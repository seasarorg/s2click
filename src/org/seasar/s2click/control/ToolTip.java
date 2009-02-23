/*
 * Copyright 2006-2009 the Seasar Foundation and the Others.
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

import java.text.MessageFormat;

import javax.servlet.ServletContext;

import net.sf.click.control.AbstractControl;
import net.sf.click.util.ClickUtils;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;

/**
 * <a href="http://www.bosrup.com/web/overlib/">overLIB</a>を使用してツールチップを表示するためのコントロールです。
 * 
 * @author Naoki Takezoe
 */
public class ToolTip extends AbstractControl {
	
	private static final long serialVersionUID = 1L;
	
	/** overLIBのリソース（click/overlibにデプロイされます） */
    public static final String[] OVERLIB_RESOURCES = {
        "/org/seasar/s2click/control/overlib/overlib.js",
        "/org/seasar/s2click/control/overlib/overlib_hideform.js",
        "/org/seasar/s2click/control/overlib/help.png",
    };
    
	/** HTMLのhead要素内に出力するインポートステートメント */
    public static final String HTML_IMPORTS =
        "<script type=\"text/javascript\" src=\"{0}/resources/overlib/overlib.js\"></script>\n"
        + "<script type=\"text/javascript\" src=\"{0}/resources/overlib/overlib_hideform.js\"></script>\n";
    
	private String contents;
	private int width = 300;
	private String title;
	private String label;
	
	public ToolTip() {
		super();
	}

	public ToolTip(String name) {
		super();
		setName(name);
	}
	
	public ToolTip(String name, String contents) {
		super();
		setName(name);
		setContents(contents);
	}
	
	public ToolTip(String name, String contents, int width) {
		super();
		setName(name);
		setContents(contents);
		setWidth(width);
	}
	
	public ToolTip(String name, String contents, String title) {
		super();
		setName(name);
		setContents(contents);
		setTitle(title);
	}
	
	public ToolTip(String name, String contents, String title, int width) {
		super();
		setName(name);
		setContents(contents);
		setTitle(title);
		setWidth(width);
	}
	
	/*
	 * (non-Javadoc)
	 * @see net.sf.click.Control#onDeploy(javax.servlet.ServletContext)
	 */
    public void onDeploy(ServletContext servletContext) {
//        ClickUtils.deployFiles(servletContext,
//                               OVERLIB_RESOURCES,
//                               "click/overlib");
    }
	
    /*
     * (non-Javadoc)
     * @see net.sf.click.Control#getHtmlImports()
     */
	public String getHtmlImports() {
        return MessageFormat.format(HTML_IMPORTS, 
        		new Object[]{ getContext().getRequest().getContextPath() }
        );
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
	
	@Override public String toString(){
		String path = getContext().getRequest().getContextPath();
		
		StringBuilder sb = new StringBuilder();
		sb.append("<a href=\"javascript:void(0);\" ");
		sb.append("onmouseover=\"");
		sb.append("return overlib('");
		sb.append(StringEscapeUtils.escapeJavaScript(getContents()));
		sb.append("'");
		if(StringUtils.isNotEmpty(getTitle())){
			sb.append(", CAPTION, '");
			sb.append(StringEscapeUtils.escapeJavaScript(getTitle()));
			sb.append("'");
		}
		sb.append(", WIDTH, ").append(getWidth());
		sb.append(");\" onmouseout=\"return nd();\">");
		
		if(StringUtils.isNotEmpty(getLabel())){
			sb.append(ClickUtils.escapeHtml(getLabel()));
		} else {
			sb.append("<img src=\"").append(path).append("/resources/overlib/help.png\" border=\"0\">");
		}
		
		sb.append("</a>");
		
		return sb.toString();
	}

	/**
	 * ツールチップに表示する内容を取得します。
	 * 
	 * @return ツールチップに表示するHTML
	 */
	public String getContents() {
		return contents;
	}

	/**
	 * ツールチップに表示する内容を設定します。
	 * 
	 * @param contents ツールチップに表示するHTML
	 */
	public void setContents(String contents) {
		this.contents = contents;
	}

	/**
	 * ツールチップの幅を取得します。
	 * 
	 * @return ツールチップの幅（ピクセル単位）
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * ツールチップの幅を設定します。
	 * 
	 * @param width ツールチップの幅（ピクセル単位）
	 */
	public void setWidth(int width) {
		this.width = width;
	}

	/**
	 * ツールチップのタイトルを取得します。
	 * 
	 * @return ツールチップのタイトル
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * ツールチップのタイトルを設定します。
	 * 
	 * @param title ツールチップのタイトル
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * アイコンの代わりに表示するテキストを取得します。
	 * 
	 * @return アイコンの代わりに表示するテキスト
	 */
	public String getLabel() {
		return label;
	}

	/**
	 * アイコンの代わりに表示するテキストを設定します。
	 * 
	 * @param label アイコンの代わりに表示するテキスト
	 */
	public void setLabel(String label) {
		this.label = label;
	}

}
