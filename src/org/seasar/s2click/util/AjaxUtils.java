package org.seasar.s2click.util;

import java.util.Map;

/**
 * 
 * @author Naoki Takezoe
 * @since 0.3.1
 */
public class AjaxUtils {
	
	public static String createAjaxRequest(String url, Map<String, String> options){
		StringBuilder sb = new StringBuilder();
		sb.append("new Ajax.Request(");
		sb.append("'").append(url).append("', ");
		sb.append("{");
		sb.append("method: 'post', ");
		sb.append(getOptions(options));
		sb.append("})");
		
		return sb.toString();
	}
	
	public static String getOptions(Map<String, String> options){
		StringBuilder sb = new StringBuilder();
		for(Map.Entry<String, String> entry: options.entrySet()){
			if(sb.length() != 0){
				sb.append(", ");
			}
			sb.append(entry.getKey()).append(": ").append(entry.getValue());
		}
		return sb.toString();
	}

	
}
