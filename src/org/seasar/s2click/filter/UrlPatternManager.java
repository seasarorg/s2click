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
