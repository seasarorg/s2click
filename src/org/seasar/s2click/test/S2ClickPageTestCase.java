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
package org.seasar.s2click.test;

import java.lang.reflect.Type;

import org.seasar.framework.util.tiger.GenericUtil;

/**
 * ページクラスのテストケースの抽象基底クラスです。
 * 
 * @author Naoki Takezoe
 * 
 * @param <T> テスト対象のページクラス
 */
public abstract class S2ClickPageTestCase<T> extends S2ClickTestCase {
	
	/**
	 * テスト対象のページクラスのインスタンス。
	 */
	protected T page;
	
	/**
	 * ページクラスのインスタンスを生成・初期化します。
	 * 生成されたページクラスのインスタンスは{@link #page}フィールドにセットされます。
	 */
	public void setUp() throws Exception {
		super.setUp();
		
		page = createPage();
		
//		Type type = GenericUtil.getTypeVariableMap(getClass())
//			.get(S2ClickPageTestCase.class.getTypeParameters()[0]);
//		page = SingletonS2Container.getComponent((Class<T>) type);
		
		initPage(page);
	}
	
	/**
	 * ページクラスのインスタンスを生成します。
	 * <p>
	 * {@link #setUp()}メソッドから呼び出されます。
	 * 
	 * @return ページクラスのインスタンス
	 */
	protected T createPage(){
		try {
			Type type = GenericUtil.getTypeVariableMap(getClass()).get(
					S2ClickPageTestCase.class.getTypeParameters()[0]);
			
			@SuppressWarnings("unchecked")
			Class<T> clazz = (Class<T>) type;
			
			return clazz.newInstance();
			
		} catch(Exception ex){
			throw new RuntimeException(ex);
		}
	}
	
	/**
	 * ページクラスの初期化を行います。
	 * 必要に応じてサブクラスでオーバーライドしてください。
	 * <p>
	 * {@link #setUp()}メソッドから呼び出されます。
	 * 
	 * @param page ページクラスのインスタンス
	 */
	protected void initPage(T page){
	}

}
