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
	
	public void onInit(){
		super.onInit();
		if(getError().getMessage() != null){
			addModel("errorMessage", getError().getMessage());
		} else {
			addModel("errorMessage", getError().toString());
		}
	}
	
	/**
	 * 共通テンプレート <tt>/layout.htm</tt> を返します。
	 */
	@Override
	public String getTemplate() {
        return "/layout.htm";
    }

}
