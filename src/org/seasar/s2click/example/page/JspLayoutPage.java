package org.seasar.s2click.example.page;

import org.seasar.s2click.S2ClickPage;

/**
 * JSP用の共通レイアウトページです。
 * 
 * @author Naoki Takezoe
 */
public class JspLayoutPage extends S2ClickPage {
	
	public JspLayoutPage(){
		String className = getClass().getName();
		String sourcePath = className.replaceAll("\\.", "/") + ".java";
		addModel("sourcePath", "/WEB-INF/classes/" + sourcePath);
	}
	
	/**
	 * 共通テンプレート <tt>/layout.jsp</tt> を返します。
	 */
	@Override
	public String getTemplate() {
        return "/layout.jsp";
    }

	
}
