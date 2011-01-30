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

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.seasar.extension.jdbc.EntityMeta;
import org.seasar.extension.jdbc.EntityMetaFactory;
import org.seasar.extension.jdbc.JdbcManager;
import org.seasar.extension.jdbc.PropertyMeta;
import org.seasar.s2click.S2ClickPage;
import org.seasar.s2click.jdbc.EntityForm.EntityFormMode;

public class EntityEditPage extends S2ClickPage {

	private static final long serialVersionUID = 1L;

	/**
	 * エンティティを編集するためのフォーム。
	 */
	public EntityForm form;

	@Resource
	protected EntityMetaFactory entityMetaFactory;

	@Resource
	protected JdbcManager jdbcManager;

	protected EntityPagesConfig config;

	/**
	 * コンストラクタ。
	 *
	 * @param config 設定
	 */
	public EntityEditPage(EntityPagesConfig config){
		form = new EntityForm("form", config, EntityFormMode.EDIT);
		form.getSubmitButton().setListener(this, "onUpdate");
		form.getCancelButton().setListener(this, "onCancel");

		this.config = config;
	}

	@Override
	public void onInit() {
		super.onInit();

		// IDの取得
		EntityMeta entityMeta = entityMetaFactory.getEntityMeta(config.getEntityClass());
		List<String> idList = new ArrayList<String>();
		for(PropertyMeta propertyMeta: entityMeta.getIdPropertyMetaList()){
			String value = getContext().getRequestParameter(propertyMeta.getName());
			if(StringUtils.isEmpty(value)){
				throw new RuntimeException(propertyMeta.getName() + " is not specified.");
			}
			idList.add(value);
		}

		Object entity = jdbcManager.from(config.getEntityClass())
			.id(idList.toArray()).disallowNoResult().getSingleResult();

		form.copyFrom(entity);
	}

	/**
	 * 更新処理の前に呼び出されます。
	 * サブクラスで必要に応じてオーバーライドしてください。
	 *
	 * @param entity エンティティ
	 */
	protected void preUpdate(Object entity){
	}

	/**
	 * 更新処理の後で呼び出されます。
	 * サブクラスで必要に応じてオーバーライドしてください。
	 *
	 * @param entity エンティティ
	 */
	protected void postUpdate(Object entity){
	}

	/**
	 * エンティティの更新処理を行い、一覧画面に戻ります。
	 *
	 * @return
	 */
	public boolean onUpdate(){
		if(form.isValid()){
			try {
				Object entity = config.getEntityClass().newInstance();
				form.copyTo(entity);

				// 前処理
				preUpdate(entity);

				// 更新処理
				// TODO 結果が1件じゃなかったらエラーにする？
				jdbcManager.update(entity).execute();

				// 後処理
				postUpdate(entity);

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
	 * エンティティの更新処理をキャンセルし、一覧画面に戻ります。
	 *
	 * @return
	 */
	public boolean onCancel(){
		setRedirect(config.getListPageClass());
		return false;
	}
}
