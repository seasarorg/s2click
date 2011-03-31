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
package org.seasar.s2click.filter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.regex.Pattern;

import org.seasar.s2click.annotation.UrlPattern;

/**
 * {@link UrlPattern}アノテーションで設定されたURLパターンの情報を管理するクラスです。
 * <p>
 * <code>ClickApp</code>の初期化時に情報が格納されます。
 * 格納された情報を{@link UrlPatternFilter}が読み取ってURLリライティングを行います。
 * 
 * @author Naoki Takezoe
 * @since 0.4.0
 */
public class UrlPatternManager {
	
	// TODO アプリケーションスコープに入れたほうがよい？
	private static List<UrlRewriteInfo> infoList = new CopyOnWriteArrayList<UrlRewriteInfo>();
	
	public static class UrlRewriteInfo {
		
		public Pattern pattern;
		public String[] parameters;
		public String realPath;
		
		public UrlRewriteInfo(String pattern, String[] parameters, String realPath){
			this.pattern = Pattern.compile(pattern);
			this.parameters = parameters;
			this.realPath = realPath;
		}
	}
	
	public static void add(String path, String realPath){
		StringBuilder pattern = new StringBuilder();
		StringBuilder parameter = new StringBuilder();
		List<String> parameters = new ArrayList<String>();
		
		for(int i=0;i<path.length();i++){
			char c = path.charAt(i);
			if(parameter.length() == 0 && c == '{'){
				parameter.append(c);
				if(pattern.length() != 0){
					pattern.append("\\E");
				}
				pattern.append("(.+?)");
				pattern.append("\\Q");
				
			} else if(parameter.length() != 0 && c== '}'){
				parameters.add(parameter.substring(1));
				parameter.setLength(0);
				
			} else {
				if(parameter.length() != 0){
					parameter.append(c);
				} else {
					if(pattern.length() == 0){
						pattern.append("\\Q");
					}
					pattern.append(c);
				}
			}
		}
		pattern.append("\\E");
		
		infoList.add(new UrlRewriteInfo(pattern.toString(), 
				parameters.toArray(new String[parameters.size()]),
				realPath));
	}
	
	public static List<UrlRewriteInfo> getAll(){
		return infoList;
	}
	
}
