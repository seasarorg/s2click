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
package org.seasar.s2click.jdbc;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.click.Context;
import org.apache.click.control.Column;
import org.apache.click.control.Decorator;
import org.apache.click.control.PageLink;
import org.apache.click.control.Table;
import org.apache.commons.lang.StringUtils;
import org.seasar.extension.jdbc.AutoSelect;
import org.seasar.extension.jdbc.EntityMeta;
import org.seasar.extension.jdbc.EntityMetaFactory;
import org.seasar.extension.jdbc.JdbcManager;
import org.seasar.extension.jdbc.PropertyMeta;
import org.seasar.s2click.S2ClickPage;

/**
 * エンティティを一覧表示するページクラスの抽象基底クラスです。
 * <p>
 * このクラスは開発中であるため、今後大幅に変更される可能性があります。
 *
 * @author Naoki Takezoe
 * @since 1.0.4
 */
public abstract class EntityListPage extends S2ClickPage {

	private static final long serialVersionUID = 1L;

	public Table table = new Table("table");

	/**
	 * 登録画面に遷移するためのリンク。
	 */
	public PageLink registerLink;

	/**
	 * 編集画面に遷移するためのリンク。
	 */
	public PageLink editLink;

	/**
	 * 削除画面に遷移するためのリンク。
	 */
	public PageLink deleteLink;

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
	public EntityListPage(EntityPagesConfig config){
		this.config = config;
	}

	@Override
	public void onInit() {
		super.onInit();
		createLinks();
		createTable();
		setTableData();
	}

	protected void createLinks(){
		registerLink = new PageLink("registerLink", getMessage("link.register"), config.getRegisterPageClass());
		editLink = new PageLink("editLink", getMessage("link.edit"), config.getEditPageClass());
		deleteLink = new PageLink("deleteLink", getMessage("link.delete"), config.getDeletePageClass());
	}

	protected void createTable(){
		EntityMeta entityMeta = entityMetaFactory.getEntityMeta(config.getEntityClass());
		int size = entityMeta.getColumnPropertyMetaSize();

		for(int i=0; i < size; i++){
			PropertyMeta propertyMeta = entityMeta.getColumnPropertyMeta(i);
			Column column = config.createColumn(propertyMeta);
			if(column != null){
				table.addColumn(column);
			}
		}

		Column column = new Column("operations", getMessage("label.operations"));
		column.setDecorator(new Decorator(){
			public String render(Object object, Context context) {
				Map<String, String> map = getIdValueMap(object);
				for(Map.Entry<String, String> entry: map.entrySet()){
					editLink.setParameter(entry.getKey(), entry.getValue());
					deleteLink.setParameter(entry.getKey(), entry.getValue());
				}
				return editLink.toString() + " | " + deleteLink.toString();
			}
		});
		table.addColumn(column);
		table.setClass("blue1");
	}

	protected Map<String, String> getIdValueMap(Object entity){
		try {
			Map<String, String> map = new HashMap<String, String>();
			EntityMeta entityMeta = entityMetaFactory.getEntityMeta(config.getEntityClass());
			for(PropertyMeta propertyMeta: entityMeta.getIdPropertyMetaList()){
				Object value = propertyMeta.getField().get(entity);
				map.put(propertyMeta.getName(), String.valueOf(value));
			}
			return map;
		} catch(Exception ex){
			throw new RuntimeException(ex);
		}
	}

	/**
	 * 一覧表示用のテーブルにデータを設定します。
	 */
	protected void setTableData(){
		AutoSelect<?> autoSelect = jdbcManager.from(config.getEntityClass());

		String orderByColumn = getOrderByColumn();
		if(StringUtils.isNotEmpty(orderByColumn)){
			autoSelect = autoSelect.orderBy(orderByColumn);
		}

		List<?> rowList = autoSelect.getResultList();
		table.setRowList(rowList);
	}

	/**
	 * 特定のカラムでソートする場合にこのメソッドをオーバーライドしてソート用のカラム名を返却します。
	 * デフォルトでは主キーの照準でソートします。
	 *
	 * @return ソート用のカラム名
	 */
	protected String getOrderByColumn(){
		EntityMeta em = entityMetaFactory.getEntityMeta(config.getEntityClass());
		StringBuilder sb = new StringBuilder();
		for(PropertyMeta pm: em.getIdPropertyMetaList()){
			if(sb.length() != 0){
				sb.append(", ");
			}
			sb.append(pm.getName());
		}
		return sb.toString();
	}

}
