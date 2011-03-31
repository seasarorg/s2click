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

import org.apache.click.control.Field;
import org.apache.click.control.Submit;
import org.seasar.extension.jdbc.EntityMeta;
import org.seasar.extension.jdbc.EntityMetaFactory;
import org.seasar.extension.jdbc.PropertyMeta;
import org.seasar.framework.container.SingletonS2Container;
import org.seasar.s2click.control.S2ClickForm;

/**
 * エンティティを登録、更新、削除するための入力フォームです。
 * <p>
 * このクラスは開発中であるため、今後大幅に変更される可能性があります。
 *
 * @author Naoki Takezoe
 * @since 1.0.4
 */
public class EntityForm extends S2ClickForm {

	private static final long serialVersionUID = 1L;

	protected EntityPagesConfig config;

	protected EntityFormMode mode;

	/**
	 * コンストラクタ。
	 *
	 * @param name フォーム名
	 * @param config 設定
	 * @param mode モード
	 */
	public EntityForm(String name, EntityPagesConfig config, EntityFormMode mode){
		super(name);
		this.config = config;
		this.mode = mode;

		createFields();
		createButtons();
	}

	/**
	 * エンティティを編集するフォームのフィールドを作成します。
	 */
	protected void createFields(){
		EntityMetaFactory factory = SingletonS2Container.getComponent(EntityMetaFactory.class);
		EntityMeta entityMeta = factory.getEntityMeta(config.getEntityClass());
		int size = entityMeta.getColumnPropertyMetaSize();
		for(int i=0; i < size; i++){
			PropertyMeta propertyMeta = entityMeta.getColumnPropertyMeta(i);
			Field field = config.createField(mode, propertyMeta);
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
			submit = new Submit("submit", getMessage("button.register"));
		} else if(mode == EntityFormMode.EDIT){
			submit = new Submit("submit", getMessage("button.update"));
		} else if(mode == EntityFormMode.DELETE){
			submit = new Submit("submit", getMessage("button.delete"));
		}
		add(submit);

		Submit cancel = new Submit("cancel", getMessage("button.cancel"));
		add(cancel);
	}

	/**
	 * 送信ボタンのインスタンスを取得します。
	 *
	 * @return 送信ボタン
	 */
	public Submit getSubmitButton(){
		return (Submit) getField("submit");
	}

	/**
	 * キャンセルボタンのインスタンスを取得します。
	 *
	 * @return キャンセルボタン
	 */
	public Submit getCancelButton(){
		return (Submit) getField("cancel");
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
