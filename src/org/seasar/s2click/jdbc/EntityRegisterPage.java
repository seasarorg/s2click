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

import javax.annotation.Resource;

import org.seasar.extension.jdbc.EntityMetaFactory;
import org.seasar.extension.jdbc.JdbcManager;
import org.seasar.s2click.S2ClickPage;
import org.seasar.s2click.jdbc.EntityForm.EntityFormMode;

public abstract class EntityRegisterPage extends S2ClickPage {

	private static final long serialVersionUID = 1L;

	/**
	 * エンティティを登録するためのフォーム。
	 */
	public EntityForm form;

	@Resource
	protected JdbcManager jdbcManager;

	@Resource
	protected EntityMetaFactory entityMetaFactory;

	protected EntityPagesConfig config;

	/**
	 * コンストラクタ。
	 *
	 * @param config 設定
	 */
	public EntityRegisterPage(EntityPagesConfig config){
		form = new EntityForm("form", config, EntityFormMode.REGISTER);
		form.getSubmitButton().setListener(this, "onRegister");
		form.getCancelButton().setListener(this, "onCancel");

		this.config = config;
	}

	/**
	 * 登録処理の前に呼び出されます。
	 * サブクラスで必要に応じてオーバーライドしてください。
	 *
	 * @param entity エンティティ
	 */
	protected void preRegister(Object entity){
	}

	/**
	 * 登録処理の後で呼び出されます。
	 * サブクラスで必要に応じてオーバーライドしてください。
	 *
	 * @param entity エンティティ
	 */
	protected void postRegister(Object entity){
	}

	/**
	 * エンティティの登録処理を行い、一覧画面に戻ります。
	 *
	 * @return
	 */
	public boolean onRegister(){
		if(form.isValid()){
			try {
				Object entity = config.getEntityClass().newInstance();
				form.copyTo(entity);

				// 前処理
				preRegister(entity);

				// 登録処理
				// TODO 結果が1件じゃなかったらエラーにする？
				jdbcManager.insert(entity).execute();

				// 後処理
				postRegister(entity);

				setRedirect(config.getListPageClass());
				return false;

			} catch(IllegalAccessException ex){
				throw new RuntimeException(ex);
			} catch(InstantiationException ex){
				throw new RuntimeException(ex);
			}
		}
		return true;
	}

	/**
	 * エンティティの登録処理をキャンセルし、一覧画面に戻ります。
	 *
	 * @return
	 */
	public boolean onCancel(){
		setRedirect(config.getListPageClass());
		return false;
	}

}
