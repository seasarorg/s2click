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
package org.seasar.s2click.exception;

/**
 * リクエストパラメータの必須チェックエラー時に発生する例外です。
 * 
 * @author Naoki Takezoe
 */
public class RequestRequiredException extends S2ClickException {

	private static final long serialVersionUID = 1L;
	
	/**
	 * コンストラクタ。
	 * 
	 * @param fieldName 必須チェックエラーのフィールド名
	 */
	public RequestRequiredException(String fieldName){
		super("必須パラメータ " + fieldName + " が指定されていません。");
	}
	
}
