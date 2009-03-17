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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;

import org.apache.click.control.AbstractControl;
import org.apache.click.control.ActionLink;
import org.apache.commons.lang.math.NumberUtils;
import org.seasar.s2click.util.S2ClickUtils;

/**
 * リストをページング処理して表示するためのコントロールです。
 * <p>
 * Velocityテンプレートを指定することで任意の表示形式でレンダリングすることができます。
 * Velocityテンプレートでは以下の変数が使用可能です。
 * </p>
 * <ul>
 *   <li>$list - 現在のページで表示する範囲のオブジェクトを格納したリスト。</li>
 *   <li>$pager - ページリンク部分のHTML。</li>
 *   <li>$format - ClickのFormatオブジェクト。通常のHTMLテンプレートの場合と同じです。</li>
 *   <li>$context - アプリケーションのコンテキストパスを表す文字列。通常のHTMLテンプレートの場合と同じです。</li>
 *   <li>$totalRows - 全件数</li>
 *   <li>$totalPages - 全ページ数</li>
 *   <li>$pageNumber - 現在のページ番号</li>
 *   <li>$startIndex - 表示範囲の開始インデックス</li>
 *   <li>$endIndex - 表示範囲の終了インデックス</li>
 *   <li>$startRow - 表示範囲の開始行（startIndex + 1）</li>
 *   <li>$endRow - 表示範囲の終了行（endIndex + 1）</li>
 * </ul>
 * @author Naoki Takezoe
 * @since 0.5.0
 */
public class PaginateList extends AbstractControl {

	private static final long serialVersionUID = 1L;
	
	/**
	 * ページリンクのパラメータ名。
	 */
    public static final String PAGE = "page";
	
    /**
     * 表示するページ番号。最初のページが0になります。
     */
    protected int pageNumber;

    /**
     * 1ページに表示する行の数。0の場合は無制限になります。
     */
    protected int pageSize;
	
    /**
     * すべてのオブジェクトを格納したリスト。
     */
    protected List<?> rowList;
    
    /**
     * テンプレートのパス。
     */
    protected String templatePath;
    
    /**
     * ページリンクを出力するためのコントロール。
     */
    protected ActionLink controlLink = new ActionLink();
	
    /**
     * コンストラクタ。
     */
	public PaginateList(){
		super();
	}
	
	/**
	 * コンストラクタ。
	 * 
	 * @param name コントロール名
	 */
	public PaginateList(String name){
		setName(name);
	}
	
	/**
	 * コンストラクタ。
	 * 
	 * @param name コントロール名
	 * @param templatePath テンプレートのパス
	 */
	public PaginateList(String name, String templatePath){
		setName(name);
		setTemplatePath(templatePath);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setName(String name){
		super.setName(name);
		controlLink.setName(getName() + "-controlLink");
	}
	
	public int getPageNumber() {
		return pageNumber;
	}

	public void setPageNumber(int pageNumber) {
		this.pageNumber = pageNumber;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

    public void setRowList(List<?> rowList) {
        this.rowList = rowList;
    }

    public List<?> getRowList(){
    	if(this.rowList == null){
    		this.rowList = new ArrayList<Object>();
    	}
    	return this.rowList;
    }
	
    /**
     * レンダリングに使用するテンプレートのパスを指定します。
     * 
     * @return テンプレートのパス
     */
	public String getTemplatePath() {
		return templatePath;
	}

	/**
	 * レンダリングに使用するテンプレートのパスを取得します。
	 * 
	 * @param templatePath テンプレートのパス
	 */
	public void setTemplatePath(String templatePath) {
		this.templatePath = templatePath;
	}

	public String getHtmlImports() {
		return null;
	}

	public void onDeploy(ServletContext servletContext) {
	}

	public void onDestroy() {
	}

	public void onInit() {
	}

	public boolean onProcess() {
        controlLink.onProcess();

        if (controlLink.isClicked()) {
            String page = getContext().getRequestParameter(PAGE);
            if (NumberUtils.isNumber(page)) {
                setPageNumber(Integer.parseInt(page));
            } else {
                setPageNumber(0);
            }
        }

        return true;
	}

	public void onRender() {
	}

	public void setListener(Object listener, String method) {
	}
	
	/**
	 * ページング処理を行うリンク部分のHTMLを取得します。
	 * 
	 * @return ページリンク部分のHTML
	 */
	public String getPager(){
		List<?> rowList = getRowList();
		
		StringBuilder sb = new StringBuilder();
		
		String prevLabel = getMessage("prev-label");
		String nextLabel = getMessage("next-label");

		// 前へリンク
		controlLink.setLabel(prevLabel);
		if(getPageNumber() > 0){
			controlLink.setDisabled(false);
			controlLink.setParameter(PAGE, String.valueOf(getPageNumber() - 1));
		} else {
			controlLink.setDisabled(true);
		}
		sb.append(controlLink.toString());
		
		sb.append("&nbsp;");
		
		// 次へリンク
		controlLink.setLabel(nextLabel);
		if((getPageNumber() + 1) * getPageSize() < rowList.size()){
			controlLink.setDisabled(false);
			controlLink.setParameter(PAGE, String.valueOf(getPageNumber() + 1));
		} else {
			controlLink.setDisabled(true);
		}
		sb.append(controlLink.toString());
		
		return sb.toString();
	}
	
	/**
	 * {@link #setRowList(List)}でセットされたリストの中から、
	 * 現在のページで表示する範囲のオブジェクトを抽出したリストを取得します。
	 * 
	 * @return 現在のページで表示するオブジェクトを格納したリスト
	 */
	public List<?> getDisplayList(){
		List<?> rowList = getRowList();
		List<Object> result = new ArrayList<Object>();
		
		for(int i = getPageNumber() * getPageSize(); 
				i < (getPageNumber() + 1) * getPageSize() && i < rowList.size(); i++){
			result.add(rowList.get(i));
		}
		return result;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString(){
		if(getTemplatePath() == null){
			throw new RuntimeException("テンプレートが指定されていません。");
		}
		
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("pager", getPager());
		param.put("pageNumber", getPageNumber());
		param.put("totalRows", getRowList().size());
		
		if(getRowList().isEmpty()){
			param.put("totalPages", 0);
		} else {
			param.put("totalPages", getRowList().size() / getPageSize());
		}
		
		int beginIndex = getPageNumber() * getPageSize();
		param.put("beginIndex", beginIndex);
		param.put("beginRow", beginIndex + 1);
		
		int endIndex = (getPageNumber() + 1) * getPageSize() - 1;
		if(getRowList().size() <= endIndex){
			endIndex = getRowList().size() - 1;
		}
		param.put("endIndex", endIndex);
		param.put("endRow", endIndex + 1);
		
		param.put("list", getDisplayList());
		param.put("format", S2ClickUtils.getConfigService().createFormat());
		param.put("context", getContext().getRequest().getContextPath());
		param.put("request", getContext().getRequest());
		param.put("response", getContext().getResponse());
		
		return getContext().renderTemplate(getTemplatePath(), param);
	}
	
}
