package org.seasar.s2click.example.page;

import net.sf.click.Page;

/**
 * 共通レイアウトを実現するページクラスの抽象基底クラス。
 * 
 * @author Naoki Takezoe
 */
public abstract class LayoutPage extends Page {
	
	public LayoutPage(){
		String className = getClass().getName();
		String sourcePath = className.replaceAll("\\.", "/") + ".java";
		addModel("sourcePath", "/WEB-INF/classes/" + sourcePath);
	}
	
	/**
	 * 共通テンプレート <tt>/layout.htm</tt> を返します。
	 */
	@Override public String getTemplate() {
        return "/layout.htm";
    }
	
}
