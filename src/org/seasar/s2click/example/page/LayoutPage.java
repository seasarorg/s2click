package org.seasar.s2click.example.page;

import org.seasar.s2click.S2ClickPage;

/**
 * 共通レイアウトを実現するページクラスの抽象基底クラス。
 * 
 * @author Naoki Takezoe
 */
public abstract class LayoutPage extends S2ClickPage {
	
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
