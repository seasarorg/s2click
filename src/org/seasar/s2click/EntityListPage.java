package org.seasar.s2click;

import java.util.List;

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

	protected Class<?> entityClass;

	protected Class<?> registerPageClass;

	protected Class<?> editPageClass;

	protected Class<?> deletePageClass;

	/**
	 * コンストラクタ。
	 *
	 * @param entityClass エンティティクラス
	 * @param registerPageClass 登録画面のページクラス
	 * @param editPageClass 編集画面のページクラス
	 * @param deletePageClass 削除画面のページクラス
	 */
	public EntityListPage(Class<?> entityClass,
			Class<?> registerPageClass, Class<?> editPageClass, Class<?> deletePageClass){

		this.entityClass = entityClass;
		this.registerPageClass = registerPageClass;
		this.editPageClass = editPageClass;
		this.deletePageClass = deletePageClass;
	}

	@Override
	public void onInit() {
		super.onInit();
		createLinks();
		createTable();
		setTableData();
	}

	protected void createLinks(){
		registerLink = new PageLink("registerLink", "Register", registerPageClass);
		editLink = new PageLink("editLink", "Edit", editPageClass);
		deleteLink = new PageLink("deleteLink", "Delete", deletePageClass);
	}

	protected void createTable(){
		EntityMeta em = entityMetaFactory.getEntityMeta(entityClass);
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
				// TODO パラメータをセット
				String idValue = getIdValue(object);
				editLink.setParameter("id", idValue);
				deleteLink.setParameter("id", idValue);
				return editLink.toString() + " | " + deleteLink.toString();
			}
		});
		table.addColumn(column);
	}

	protected String getIdValue(Object entity){
		try {
			StringBuilder sb = new StringBuilder();
			EntityMeta em = entityMetaFactory.getEntityMeta(entityClass);
			for(PropertyMeta pm: em.getIdPropertyMetaList()){
				Object value = pm.getField().get(entity);
				if(sb.length() != 0){
					sb.append(":");
				}
				sb.append(value);
			}
			return sb.toString();
		} catch(Exception ex){
			throw new RuntimeException(ex);
		}
	}

	/**
	 * 一覧表示用のテーブルにデータを設定します。
	 */
	protected void setTableData(){
		AutoSelect<?> autoSelect = jdbcManager.from(entityClass);

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
		EntityMeta em = entityMetaFactory.getEntityMeta(entityClass);
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
