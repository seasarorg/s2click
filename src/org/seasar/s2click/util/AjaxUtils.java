package org.seasar.s2click.util;

import java.util.Map;

import net.arnx.jsonic.JSON;

/**
 * 
 * @author Naoki Takezoe
 * @since 0.4.0
 */
public class AjaxUtils {
	
	public static final String ON_CREATE = "onCreate";
	
	public static final String ON_COMPLETE = "onComplete";
	
	public static final String ON_EXCEPTION = "onException";
	
	public static final String ON_FAILURE = "onFailure";
	
	public static final String ON_SUCCESS = "onSuccess";
	

	// public static final String HTML_IMPORTS =
	// "<script type=\"text/javascript\"
	// src=\"{0}/click/prototype/prototype{1}.js\"></script>\n";
	// TODO Click付属のprototype.jsはバージョンが古い…
	public static final String HTML_IMPORTS = 
		"<script type=\"text/javascript\" src=\"{0}/js/prototype.js\"></script>\n";

	
	public static String createAjaxRequest(String url, Map<String, String> options,
			Map<String, String> parameters){
		StringBuilder sb = new StringBuilder();
		sb.append("new Ajax.Request(");
		sb.append("'").append(url).append("', ");
		sb.append("{");
		sb.append("method: 'post', ");
		if(!parameters.isEmpty()){
			sb.append("parameters: ");
			sb.append(JSON.encode(parameters));
			sb.append(", ");
		}
		sb.append(getOptions(options));
		sb.append("})");
		
		return sb.toString();
	}
	
	public static String createAjaxUpdater(String id, String url, Map<String, String> options,
			Map<String, String> parameters){
		StringBuilder sb = new StringBuilder();
		sb.append("new Ajax.Updater(");
		sb.append("'").append(id).append("', ");
		sb.append("'").append(url).append("', ");
		sb.append("{");
		sb.append("method: 'post', ");
		if(!parameters.isEmpty()){
			sb.append("parameters: ");
			sb.append(JSON.encode(parameters));
			sb.append(", ");
		}
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
