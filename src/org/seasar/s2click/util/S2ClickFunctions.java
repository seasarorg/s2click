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
	 * 日付をyyyy/MM/dd HH:mm:ss形式でフォーマットします。
	 * 
	 * @param date 日付
	 * @return yyyy/MM/dd HH:mm:ss形式の文字列
	 * 
	 * @see S2ClickFormat#datetime(Date)
	 */
	public static String datetime(java.util.Date date){
		return getFormat().datetime(date);
	}
	
	/**
	 * JavaオブジェクトをJSONに変換します。
	 * 
	 * @param obj オブジェクト
	 * @return JSON形式の文字列
	 * 
	 * @see S2ClickFormat#json(Object)
	 */
	public static String json(Object obj){
		return getFormat().json(obj);
	}
	
	/**
	 * 連続する半角スペースの2文字目以降を&nbsp;に変換します。
	 * 
	 * @param value 文字列
	 * @return 変換された文字列
	 * 
	 * @see S2ClickFormat#nbsp(String)
	 */
	public static String nbsp(String value){
		return getFormat().nbsp(value);
	}
	
	/**
	 * 引数の文字列をOGNLとして評価し、結果を返却します。
	 * 
	 * @see S2ClickFormat#ognl(String)
	 */
	public static Object ognl(String expression) throws OgnlException{
		return getFormat().ognl(expression);
	}
	
	/**
	 * HTMLをエスケープし、改行を&lt;br&gt;タグに変換します。
	 * 
	 * @param value 文字列
	 * @return 変換された文字列
	 * 
	 * @see S2ClickFormat#br(String)
	 */
	public static String br(String value){
		return getFormat().br(value);
	}
	
	/**
	 * 引数で渡した文字列と同じ長さのアスタリスク文字列を作成します。
	 * 
	 * @param value 文字列
	 * @return 変換された文字列
	 * 
	 * @see S2ClickFormat#mask(String)
	 */
	public static String mask(String value){
		return getFormat().mask(value);
	}
	
	/**
	 * 引数に渡された文字列をclick.xmlで指定された文字コードでURLエンコードします。
	 * 
	 * @param value 文字列
	 * @return URLエンコードされた文字列
	 * 
	 * @see Format#url(Object)
	 */
	public static String url(Object value){
		return getFormat().url(value);
	}
	
	/**
	 * 日付をyyyy/MM/dd形式でフォーマットします。
	 * 
	 * @param date 日付
	 * @return yyyy/MM/dd形式の文字列
	 * 
	 * @see S2ClickFormat#date(Date)
	 */
	public static String date(Date date){
		return getFormat().date(date);
	}
	
	/**
	 * 現在日時を指定したパターンでフォーマットします。
	 * 
	 * @param pattern <code>SimpleDateFormat</code>で利用可能なパターン
	 * @return 指定したパターンでフォーマットされた現在日時
	 * 
	 * @see Format#currentDate(String)
	 */
	public static String currentDate2(String pattern){
		return getFormat().currentDate(pattern);
	}
	
	/**
	 * 現在日時をデフォルトのパターンでフォーマットします。
	 * 
	 * @return デフォルトのパターンでフォーマットされた現在日時
	 * 
	 * @see Format#currentDate()
	 */
	public static String currentDate(){
		return getFormat().currentDate();
	}
	
	/**
	 * 数字を指定した10進数パターンでフォーマットします。
	 * 
	 * @param number 数値
	 * @param pattern <code>DecimalFormat</code>で利用可能なパターン
	 * @return 指定したパターンでフォーマットされた数値
	 * 
	 * @see Format#decimal(Number, String)
	 */
	public static String decimal2(Number number, String pattern){
		return getFormat().decimal(number, pattern);
	}
	
	/**
	 * 数字を10進数にフォーマットします。
	 * 
	 * @param number 数値
	 * @return フォーマットされた数値
	 * 
	 * @see Format#decimal(Number)
	 */
	public static String decimal(Number number){
		return getFormat().decimal(number);
	}
	
	/**
	 * Eメールをmailtoリンクに変換します。
	 * 
	 * @param email メールアドレス
	 * @param attribute リンクのaタグの属性。href属性以外の属性を持たせたい場合に指定します。
	 * @return リンクのHTML。ただし、リンクに変換する条件を満たなさない場合は引数<code>email</code>をそのまま返します。
	 * 
	 * @see Format#email(String, String)
	 */
	public static String email2(String email, String attribute){
		return getFormat().email(email, attribute);
	}
	
	/**
	 * Eメールをmailtoリンクに変換します。
	 * 
	 * @param email メールアドレス
	 * @return リンクのHTML。ただし、リンクに変換する条件を満たなさない場合は引数<code>email</code>をそのまま返します。
	 * 
	 * @see Format#email(String)
	 */
	public static String email(String email){
		return getFormat().email(email);
	}
	
	/**
	 * JavaScriptのエスケープを行います。
	 * 
	 * @param value 文字列
	 * @return エスケープされた文字列
	 * 
	 * @see Format#javascript(String)
	 */
	public static String javascript(String value){
		return getFormat().javascript(value);
	}
	
	/**
	 * 文字列を指定文字数でカットします。カットした場合、末尾に"..."が付与されます。
	 * 
	 * @param value 文字列
	 * @param maxLength 最大文字数
	 * @return 指定文字数でカットされた文字列
	 * 
	 * @see Format#limitLength(String, int)
	 */
	public static String limitLength(String value, int maxLength){
		return getFormat().limitLength(value, maxLength);
	}
	
	/**
	 * 文字列を指定文字数でカットします。カットした場合、末尾に指定したサフィックスが付与されます。
	 * 
	 * @param value 文字列
	 * @param maxLength 最大文字数
	 * @param suffix カットした場合に末尾に付与するサフィックス
	 * @return 指定文字数でカットされた文字列
	 * 
	 * @see Format#limitLength(String, int, String)
	 */
	public static String limitLength2(String value, int maxLength, String suffix){
		return getFormat().limitLength(value, maxLength, suffix);
	}
	
	/**
	 * メールアドレスやURLをリンクに変換します。
	 * 
	 * @param value メールアドレスやURL
	 * @return リンクのHTML。ただし、リンクに変換する条件を満たなさない場合は引数<code>value</code>をそのまま返します。
	 * 
	 * @see Format#link(String)
	 */
	public static String link(String value){
		return getFormat().link(value);
	}
	
	/**
	 * メールアドレスやURLをリンクに変換します。
	 * 
	 * @param value メールアドレスやURL
	 * @param attribute リンクのaタグの属性。href属性以外の属性を持たせたい場合に指定します。
	 * @return リンクのHTML。ただし、リンクに変換する条件を満たなさない場合は引数<code>value</code>をそのまま返します。
	 * 
	 * @see Format#link(String, String)
	 */
	public static String link2(String value, String attribute){
		return getFormat().link(value, attribute);
	}
	
	/**
	 * 数値をパーセンテージにフォーマットします。
	 * 
	 * @param number 数値
	 * @return パーセンテージ形式にフォーマットされた文字列
	 * 
	 * @see Format#percentage(Number)
	 */
	public static String percentage(Number number){
		return getFormat().percentage(number);
	}

//	public static Locale getLocale(){
//		return format.getLocale();
//	}
	
	/**
	 * 引数で与えられた値を指定したパターンでフォーマットします。
	 * 
	 * @param pattern <code>MessageFormat</code>で利用可能なパターン
	 * @param args 引数（オブジェクト、リスト、文字列配列のいずれか）
	 * @return フォーマットされた文字列
	 * 
	 * @see Format#message(String, List)
	 * @see Format#message(String, Object)
	 * @see Format#message(String, Object[])
	 */
	public static String message(String pattern, Object args){
		if(args instanceof List){
			return getFormat().message(pattern, (List) args);
			
		} else if(args.getClass().isArray()){
			return getFormat().message(pattern, (String[]) args);
		}
		return getFormat().message(pattern, args);
	}
	
//	public static String message(String param1, List param2){
//		return format.message(param1, param2);
//	}
//	public static String message(String param1, Object[] param2){
//		return format.message(param1, param2);
//	}
	
	/**
	 * 時刻をデフォルトのパターンでフォーマットします。
	 * 
	 * @param date 日付
	 * @return フォーマットされた時刻
	 * 
	 * @see Format#time(Date)
	 */
	public static String time(Date date){
		return getFormat().time(date);
	}
	
	/**
	 * オブジェクトを文字列に変換します。nullの場合は空文字列を返します。
	 * 
	 * @param obj オブジェクト
	 * @return 文字列
	 * 
	 * @see Format#string(Object)
	 */
	public static String string(Object obj){
		return getFormat().string(obj);
	}
	
	/**
	 * 数値を通貨形式でフォーマットします。
	 * 
	 * @param number 数値
	 * @return 通貨形式でフォーマットされた数値
	 * 
	 * @see Format#currency(Number)
	 */
	public static String currency(Number number){
		return getFormat().currency(number);
	}
	
	/**
	 * 日付を指定したパターンでフォーマットします。
	 * 
	 * @param date 日付
	 * @param pattern <code>SimpleDateFormat</code>で利用可能なパターン
	 * @return 指定したパターンでフォーマットされた日時
	 * 
	 * @see Format#date(Date, String)
	 */
	public static String date2(Date date, String pattern){
		return getFormat().date(date, pattern);
	}
	
	/**
	 * HTMLタグのエスケープを行います。
	 * 
	 * @param value オブジェクト
	 * @return エスケープされた文字列
	 * 
	 * @see Format#html(Object)
	 */
	public static String html(Object value){
		return getFormat().html(value);
	}

}
