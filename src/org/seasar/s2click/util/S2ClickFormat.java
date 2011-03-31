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
package org.seasar.s2click.util;

import java.util.Date;

import net.arnx.jsonic.JSON;
import ognl.Ognl;
import ognl.OgnlException;

import org.apache.click.util.Format;

/**
 * 
 * @author Naoki Takezoe
 */
public class S2ClickFormat extends Format {
	
	private static final long serialVersionUID = 1L;

	/**
	 * 日付をyyyy/MM/dd形式でフォーマットします。
	 * 
	 * @param date 日付
	 * @return フォーマットされた文字列
	 */
	@Override
	public String date(Date date){
		return date(date, "yyyy/MM/dd");
	}
	
	/**
	 * 日付をyyyy/MM/dd HH:mm:ss形式でフォーマットします。
	 * 
	 * @param date 日付
	 * @return フォーマットされた文字列
	 */
	public String datetime(Date date){
		return date(date, "yyyy/MM/dd HH:mm:ss");
	}
	
	/**
	 * JavaオブジェクトをJSONに変換します。
	 * 
	 * @param obj オブジェクト
	 * @return JSON
	 */
	public String json(Object obj){
		return JSON.encode(obj);
	}
	
	/**
	 * 連続する半角スペースの2文字目以降を&nbsp;に変換します。
	 * 
	 * @param value 文字列
	 * @return 変換後の文字列
	 */
	public String nbsp(String value){
		return S2ClickUtils.convertNbsp(value);
	}
	
	/**
	 * 引数の文字列をOGNLとして評価し、結果を返却します。
	 * 
	 * @param expression OGNL式
	 * @return OGML式を評価した結果
	 * @throws OgnlException OGNLの評価に失敗した場合
	 */
	public Object ognl(String expression) throws OgnlException {
		return Ognl.getValue(expression, null);
	}
	
	/**
	 * HTMLをエスケープし、改行を&lt;br&gt;タグに変換します。
	 * 
	 * @param value 文字列
	 * @return 変換後の文字列
	 */
	public String br(String value){
		String result = html(value);
		result = result.replaceAll("(\r\n|\r|\n)", "<br>$1");
		return result;
	}
	
	/**
	 * 引数で渡した文字列と同じ長さのアスタリスク文字列を作成します。
	 * 
	 * @param value 文字列
	 * @return 引数で渡した文字列と同じ長さのアスタリスク文字列
	 */
	public String mask(String value){
		StringBuilder sb = new StringBuilder();
		for(int i=0;i<value.length();i++){
			sb.append('*');
		}
		return sb.toString();
	}
	
}
