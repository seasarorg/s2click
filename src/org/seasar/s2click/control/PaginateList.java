package org.seasar.s2click.control;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;

import net.sf.click.control.AbstractControl;
import net.sf.click.control.ActionLink;

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
		
		if(rowList.isEmpty()){
			sb.append("&lt;前へ");
			sb.append("&nbsp;");
			sb.append("次へ&gt;");
			
		} else {
			if(getPageNumber() > 0){
				controlLink.setLabel("<前へ");
				controlLink.setParameter(PAGE, String.valueOf(getPageNumber() - 1));
				sb.append(controlLink.toString());
			} else {
				sb.append("&lt;前へ");
			}
			
			sb.append("&nbsp;");
			
			if((getPageNumber() + 1) * getPageSize() < rowList.size()){
				controlLink.setLabel("次へ>");
				controlLink.setParameter(PAGE, String.valueOf(getPageNumber() + 1));
				sb.append(controlLink.toString());
			} else {
				sb.append("次へ&gt;");
			}
		}
		
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
		param.put("list", getDisplayList());
		param.put("format", S2ClickUtils.getClickApp().getFormat());
		param.put("context", getContext().getRequest().getContextPath());
		
		return getContext().renderTemplate(getTemplatePath(), param);
	}
	
}
