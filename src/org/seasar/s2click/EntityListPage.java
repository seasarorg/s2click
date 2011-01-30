package org.seasar.s2click;

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
import org.seasar.s2click.control.PublicFieldColumn;

/**
 *
 * @author Naoki Takezoe
 */
public class EntityListPage extends S2ClickPage {

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
		registerLink = new PageLink("registerLink", "Register", config.getRegisterPageClass());
		editLink = new PageLink("editLink", "Edit", config.getEditPageClass());
		deleteLink = new PageLink("deleteLink", "Delete", config.getDeletePageClass());
	}

	protected void createTable(){
		EntityMeta em = entityMetaFactory.getEntityMeta(config.getEntityClass());
		int size = em.getColumnPropertyMetaSize();

		for(int i=0; i < size; i++){
			PropertyMeta pm = em.getColumnPropertyMeta(i);

			PublicFieldColumn column = new PublicFieldColumn(
					pm.getName(), pm.getName());

			table.addColumn(column);
		}

		Column column = new Column("operations", "");
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
			EntityMeta em = entityMetaFactory.getEntityMeta(config.getEntityClass());
			for(PropertyMeta pm: em.getIdPropertyMetaList()){
				Object value = pm.getField().get(entity);
				map.put(pm.getName(), String.valueOf(value));
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
