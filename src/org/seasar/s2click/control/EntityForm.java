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
package org.seasar.s2click.control;

import java.util.Date;

import javax.persistence.Column;

import org.apache.click.control.Field;
import org.apache.click.control.HiddenField;
import org.apache.click.control.Submit;
import org.apache.click.control.TextField;
import org.apache.click.extras.control.IntegerField;
import org.seasar.extension.jdbc.EntityMeta;
import org.seasar.extension.jdbc.EntityMetaFactory;
import org.seasar.extension.jdbc.PropertyMeta;
import org.seasar.framework.container.SingletonS2Container;

/**
 * エンティティを登録、更新、削除するための入力フォームです。
 *
 * @author Naoki Takezoe
 */
public class EntityForm extends S2ClickForm {

	private static final long serialVersionUID = 1L;

	protected Class<?> entityClass;
	protected EntityFormMode mode;

	/**
	 * コンストラクタ。
	 *
	 * @param name フォーム名
	 * @param entityClass エンティティの型
	 * @param mode モード
	 */
	public EntityForm(String name, Class<?> entityClass, EntityFormMode mode){
		super(name);
		this.entityClass = entityClass;
		this.mode = mode;

		createFields();
		createButtons();
	}

	/**
	 * エンティティを編集するフォームのフィールドを作成します。
	 */
	protected void createFields(){
		EntityMetaFactory factory = SingletonS2Container.getComponent(EntityMetaFactory.class);
		EntityMeta em = factory.getEntityMeta(entityClass);
		int size = em.getColumnPropertyMetaSize();
		for(int i=0; i < size; i++){
			PropertyMeta pm = em.getColumnPropertyMeta(i);
			Field field = createField(em, pm);
			if(field != null){
				add(field);
			}
		}
	}

	/**
	 * エンティティを編集するフォームのボタンを作成します。
	 */
	protected void createButtons(){
		Submit submit = null;
		if(mode == EntityFormMode.REGISTER){
			submit = new Submit("submit", "Register");
		} else if(mode == EntityFormMode.EDIT){
			submit = new Submit("submit", "Update");
		} else if(mode == EntityFormMode.DELETE){
			submit = new Submit("submit", "Delete");
		}
		add(submit);

		Submit cancel = new Submit("cancel", "Cancel");
		add(cancel);
	}

	/**
	 * 送信ボタンのインスタンスを取得します。
	 *
	 * @return 送信ボタン
	 */
	public Submit getSubmit(){
		return (Submit) getField("submit");
	}

	public Submit getCancel(){
		return (Submit) getField("cancel");
	}

	// TODO 別クラスにしてDIして使うようにしたほうがいいかも
	protected Field createField(EntityMeta em, PropertyMeta pm){

		String name = pm.getName();
		Class<?> type = pm.getPropertyClass();
		Field field = null;

		if(pm.isId()){
			// 削除モード時はIDをHiddenFieldとして生成
			if(mode == EntityFormMode.DELETE){
				field = new HiddenField(name, pm.getPropertyClass());
				return field;
			}
			// 更新モード時の場合はIDをHiddenFieldとして生成
			if(mode == EntityFormMode.EDIT){
				field = new HiddenField(name, pm.getPropertyClass());
				return field;
			}
			// 挿入モード時かつIDが自動採番の場合はフィールドを生成しない
			if(mode == EntityFormMode.REGISTER && pm.hasIdGenerator()){
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

			// 削除モード以外の場合は必須フィールドの設定を行う
			if(mode != EntityFormMode.DELETE){
				Column column = pm.getField().getAnnotation(Column.class);
				if(column != null){
					if(column.nullable() == false){
						field.setRequired(true);
					}
				}
			}
		}

		return field;
	}

	/**
	 * {@link EntityForm}のモードを指定するための列挙型です。
	 *
	 * @author Naoki Takezoe
	 */
	public static enum EntityFormMode {
		/** 登録モード */
		REGISTER,
		/** 編集モード */
		EDIT,
		/** 削除モード */
		DELETE
	}

}
