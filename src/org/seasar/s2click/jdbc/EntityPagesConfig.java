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

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.Column;

import org.apache.click.Page;
import org.apache.click.control.Field;
import org.apache.click.control.HiddenField;
import org.apache.click.control.TextField;
import org.apache.click.extras.control.IntegerField;
import org.apache.click.util.ClickUtils;
import org.apache.commons.lang.StringUtils;
import org.seasar.extension.jdbc.PropertyMeta;
import org.seasar.s2click.control.DateFieldYYYYMMDD;
import org.seasar.s2click.control.LabelField;
import org.seasar.s2click.control.PublicFieldColumn;
import org.seasar.s2click.jdbc.EntityForm.EntityFormMode;

/**
 * エンティティの一覧、登録、編集、削除を行うページの設定を行うクラスです。
 *
 * @author Naoki Takezoe
 * @since 1.0.4
 */
public class EntityPagesConfig {

	private Class<?> entityClass;
	private Class<? extends Page> listPageClass;
	private Class<? extends Page> registerPageClass;
	private Class<? extends Page> editPageClass;
	private Class<? extends Page> deletePageClass;

	private Map<String, String> labelMap = new HashMap<String, String>();

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
			Class<? extends Page> listPageClass,
			Class<? extends Page> regiserPageClass,
			Class<? extends Page> editPageClass,
			Class<? extends Page> deletePageClass){
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
	public Class<? extends Page> getListPageClass() {
		return listPageClass;
	}

	/**
	 * 登録画面のページクラスを取得します。
	 *
	 * @return 登録画面のページクラス
	 */
	public Class<? extends Page> getRegisterPageClass() {
		return registerPageClass;
	}

	/**
	 * 編集画面のページクラスを取得します。
	 *
	 * @return 編集画面のページクラス
	 */
	public Class<? extends Page> getEditPageClass() {
		return editPageClass;
	}

	/**
	 * 削除画面のページクラスを取得します。
	 *
	 * @return 削除画面のページクラス
	 */
	public Class<? extends Page> getDeletePageClass() {
		return deletePageClass;
	}

	/**
	 * エンティティのプロパティに対応した{@link Field}オブジェクトを作成します。
	 *
	 * @param mode フォームのモード
	 * @param propertyMeta エンティティのプロパティ
	 * @return フォームのフィールド（nullの場合はフォームに追加されません）
	 */
	public Field createField(EntityFormMode mode, PropertyMeta propertyMeta){
		String name = propertyMeta.getName();
		Class<?> type = propertyMeta.getPropertyClass();
		Field field = null;

		if(propertyMeta.isId()){
			// 削除モード時はIDをHiddenFieldとして生成
			if(mode == EntityFormMode.DELETE){
				field = new HiddenField(name, propertyMeta.getPropertyClass());
				return field;
			}
			// 更新モード時の場合はIDをHiddenFieldとして生成
			if(mode == EntityFormMode.EDIT){
				field = new HiddenField(name, propertyMeta.getPropertyClass());
				return field;
			}
			// 挿入モード時かつIDが自動採番の場合はフィールドを生成しない
			if(mode == EntityFormMode.REGISTER && propertyMeta.hasIdGenerator()){
				return null;
			}
		}

		// 削除モード時はIDのHiddenField以外は作成しない
		if(mode == EntityFormMode.DELETE){
			field = new LabelField();

		} else {
			// プロパティの型に応じたフィールドを生成
			if(type == String.class){
				field = new TextField();
			} else if(type == Integer.class){
				field = new IntegerField();
			} else if(type == Date.class){
				field = new DateFieldYYYYMMDD();
			}
		}

		if(field != null){
			field.setName(name);
			field.setLabel(getLabel(propertyMeta));

			// 削除モード以外の場合は必須フィールドの設定を行う
			if(mode != EntityFormMode.DELETE){
				Column column = propertyMeta.getField().getAnnotation(Column.class);
				if(column != null){
					if(column.nullable() == false){
						field.setRequired(true);
					}
				}
			}
		}

		return field;
	}

	protected void putLabel(String propertyName, String label){
		this.labelMap.put(propertyName, label);
	}

	/**
	 * エンティティのプロパティの表示用文字列を取得します。
	 * <p>
	 * {@link #putLabel(String, String)}で設定した文字列があればそれを、
	 * 設定されていなければプロパティ名を{@link ClickUtils#toLabel(String)}で変換した文字列を返します。
	 *
	 * @param propertyMeta プロパティ
	 * @return 表示用文字列
	 */
	public String getLabel(PropertyMeta propertyMeta){
		String label = this.labelMap.get(propertyMeta.getName());
		if(StringUtils.isNotEmpty(label)){
			return label;
		}
		return ClickUtils.toLabel(propertyMeta.getName());
	}

	/**
	 * エンティティのプロパティに対応した{@link org.apache.click.control.Column}オブジェクトを生成します。
	 *
	 * @param propertyMeta エンティティのプロパティ
	 * @return テーブルのカラム（nullの場合はテーブルに追加されません）
	 */
	public org.apache.click.control.Column createColumn(PropertyMeta propertyMeta){
		PublicFieldColumn column = new PublicFieldColumn(
				propertyMeta.getName(), getLabel(propertyMeta));

		return column;
	}

}
