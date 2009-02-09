package org.seasar.s2click.example.page;

import net.sf.click.util.ErrorPage;

import org.seasar.s2click.annotation.Path;

/**
 * カスタムエラーページのサンプルです。
 * 
 * @author Naoki Takezoe
 */
@Path("/click/error.htm")
public class CustomErrorPage extends ErrorPage {
	
	public String title = "エラーページ";
	
	public CustomErrorPage(){
		String className = getClass().getName();
		String sourcePath = className.replaceAll("\\.", "/") + ".java";
		addModel("sourcePath", "/WEB-INF/classes/" + sourcePath);
	}
	
	/**
	 * 共通テンプレート <tt>/layout.htm</tt> を返します。
	 */
	@Override
	public String getTemplate() {
        return "/layout.htm";
    }

}
