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
