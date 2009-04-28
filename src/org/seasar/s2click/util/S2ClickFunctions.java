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
package org.seasar.s2click.util;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.click.Context;
import org.apache.click.util.Format;

import ognl.OgnlException;

/**
 * {@link S2ClickFormat}のメソッドをJSP-ELの関数として呼び出すため、staticメソッドでラップします。
 * 
 * @author Naoki Takezoe
 */
@SuppressWarnings("unchecked")
public class S2ClickFunctions {

	private static S2ClickFormat getFormat(){
		HttpServletRequest request = Context.getThreadLocalContext().getRequest();
		return (S2ClickFormat) request.getAttribute("format");
	}
	
	/**
	 * @see S2ClickFormat#datetime(Date)
	 */
	public static String datetime(java.util.Date param1){
		return getFormat().datetime(param1);
	}
	
	/**
	 * @see S2ClickFormat#json(Object)
	 */
	public static String json(Object param1){
		return getFormat().json(param1);
	}
	
	/**
	 * @see S2ClickFormat#nbsp(String)
	 */
	public static String nbsp(String param1){
		return getFormat().nbsp(param1);
	}
	
	/**
	 * @see S2ClickFormat#ognl(String)
	 */
	public static Object ognl(String param1) throws OgnlException{
		return getFormat().ognl(param1);
	}
	
	/**
	 * @see S2ClickFormat#br(String)
	 */
	public static String br(String param1){
		return getFormat().br(param1);
	}
	
	/**
	 * @see S2ClickFormat#mask(String)
	 */
	public static String mask(String param1){
		return getFormat().mask(param1);
	}
	
	/**
	 * @see S2ClickFormat#url(String)
	 */
	public static String url(String param1){
		return getFormat().url(param1);
	}
	
	/**
	 * @see S2ClickFormat#date(Date)
	 */
	public static String date(Date param1){
		return getFormat().date(param1);
	}
	
//	public static String getEmptyString(){
//		return format.getEmptyString();
//	}
	
	/**
	 * @see Format#currentDate(String)
	 */
	public static String currentDate2(String param1){
		return getFormat().currentDate(param1);
	}
	
	/**
	 * @see Format#currentDate()
	 */
	public static String currentDate(){
		return getFormat().currentDate();
	}
	
	/**
	 * @see Format#decimal(Number, String)
	 */
	public static String decimal2(Number param1, String param2){
		return getFormat().decimal(param1, param2);
	}
	
	/**
	 * @see Format#decimal(Number)
	 */
	public static String decimal(Number param1){
		return getFormat().decimal(param1);
	}
	
	/**
	 * @see Format#email(String, String)
	 */
	public static String email2(String param1, String param2){
		return getFormat().email(param1, param2);
	}
	
	/**
	 * @see Format#email(String)
	 */
	public static String email(String param1){
		return getFormat().email(param1);
	}
	
	/**
	 * @see Format#javascript(String)
	 */
	public static String javascript(String param1){
		return getFormat().javascript(param1);
	}
	
	/**
	 * @see Format#limitLength(String, int)
	 */
	public static String limitLength(String param1, int param2){
		return getFormat().limitLength(param1, param2);
	}
	
	/**
	 * @see Format#limitLength(String, int, String)
	 */
	public static String limitLength2(String param1, int param2, String param3){
		return getFormat().limitLength(param1, param2, param3);
	}
	
	/**
	 * @see Format#link(String)
	 */
	public static String link(String param1){
		return getFormat().link(param1);
	}
	
	/**
	 * @see Format#link(String, String)
	 */
	public static String link2(String param1, String param2){
		return getFormat().link(param1, param2);
	}
	
	/**
	 * @see Format#percentage(Number)
	 */
	public static String percentage(Number param1){
		return getFormat().percentage(param1);
	}

//	public static Locale getLocale(){
//		return format.getLocale();
//	}
	
	/**
	 * @see Format#message(String, List)
	 * @see Format#message(String, Object)
	 * @see Format#message(String, Object[])
	 */
	public static String message(String param1, Object param2){
		if(param2 instanceof List){
			return getFormat().message(param1, (List) param2);
			
		} else if(param2.getClass().isArray()){
			return getFormat().message(param1, (String[]) param2);
		}
		return getFormat().message(param1, param2);
	}
	
//	public static String message(String param1, List param2){
//		return format.message(param1, param2);
//	}
//	public static String message(String param1, Object[] param2){
//		return format.message(param1, param2);
//	}
	
	/**
	 * @see Format#time(Date)
	 */
	public static String time(Date param1){
		return getFormat().time(param1);
	}
	
	/**
	 * @see Format#string(Object)
	 */
	public static String string(Object param1){
		return getFormat().string(param1);
	}
	
	/**
	 * @see Format#currency(Number)
	 */
	public static String currency(Number param1){
		return getFormat().currency(param1);
	}
	
	/**
	 * @see Format#date(Date, String)
	 */
	public static String date2(Date param1, String param2){
		return getFormat().date(param1, param2);
	}
	
	/**
	 * @see Format#html(Object)
	 */
	public static String html(Object param1){
		return getFormat().html(param1);
	}

}
