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
package org.seasar.s2click.jdbc;

/**
 * エンティティの一覧、登録、編集、削除を行うページの設定を行うクラスです。
 *
 * @author Naoki Takezoe
 */
public class EntityPagesConfig {

	private Class<?> entityClass;
	private Class<?> listPageClass;
	private Class<?> registerPageClass;
	private Class<?> editPageClass;
	private Class<?> deletePageClass;

	/**
	 * コンストラクタ。
	 *
	 * @param entityClass 対象のエンティティの型
	 * @param listPageClass 一覧画面のページクラス
	 * @param regiserPageClass 登録画面のページクラス
	 * @param editPageClass 編集画面のページクラス
	 * @param deletePageClass 削除画面のページクラス
	 */
	public EntityPagesConfig(Class<?> entityClass,
			Class<?> listPageClass, Class<?> regiserPageClass, Class<?> editPageClass, Class<?> deletePageClass){
		this.entityClass       = entityClass;
		this.listPageClass     = listPageClass;
		this.registerPageClass = regiserPageClass;
		this.editPageClass     = editPageClass;
		this.deletePageClass   = deletePageClass;
	}

	/**
	 * エンティティの型を取得します。
	 *
	 * @return エンティティの型
	 */
	public Class<?> getEntityClass() {
		return entityClass;
	}

	/**
	 * 一覧画面のページクラスを取得します。
	 *
	 * @return 一覧画面のページクラス
	 */
	public Class<?> getListPageClass() {
		return listPageClass;
	}

	/**
	 * 登録画面のページクラスを取得します。
	 *
	 * @return 登録画面のページクラス
	 */
	public Class<?> getRegisterPageClass() {
		return registerPageClass;
	}

	/**
	 * 編集画面のページクラスを取得します。
	 *
	 * @return 編集画面のページクラス
	 */
	public Class<?> getEditPageClass() {
		return editPageClass;
	}

	/**
	 * 削除画面のページクラスを取得します。
	 *
	 * @return 削除画面のページクラス
	 */
	public Class<?> getDeletePageClass() {
		return deletePageClass;
	}

}
