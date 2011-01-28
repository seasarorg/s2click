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
import org.seasar.extension.jdbc.JdbcManager;
import org.seasar.framework.beans.BeanDesc;
import org.seasar.framework.beans.PropertyDesc;
import org.seasar.framework.beans.factory.BeanDescFactory;
import org.seasar.s2click.control.PublicFieldColumn;

public class EntityListPage extends S2ClickPage {

	private static final long serialVersionUID = 1L;

	public Table table = new Table("table");

	@Resource
	protected JdbcManager jdbcManager;

	protected Class<?> entityClass;

	protected Class<?> registerPageClass;

	protected Class<?> editPageClass;

	protected Class<?> deletePageClass;

	protected PageLink registerLink;

	protected PageLink editLink;

	protected PageLink deleteLink;

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
		BeanDesc beanDesc = BeanDescFactory.getBeanDesc(entityClass);
		int size = beanDesc.getPropertyDescSize();
		for(int i=0; i < size; i++){
			PropertyDesc propertyDesc = beanDesc.getPropertyDesc(i);
			PublicFieldColumn column = new PublicFieldColumn(
					propertyDesc.getPropertyName(), propertyDesc.getPropertyName());
			table.addColumn(column);
		}

		Column column = new Column("operations", "");
		column.setDecorator(new Decorator(){
			public String render(Object object, Context context) {
				// TODO パラメータをセット
				return editLink.toString() + "|" + deleteLink.toString();
			}
		});
	}

	protected void setTableData(){
		AutoSelect<?> autoSelect = jdbcManager.from(entityClass);

		String orderByColumn = getOrderByColumn();
		if(StringUtils.isNotEmpty(orderByColumn)){
			autoSelect = autoSelect.orderBy(orderByColumn);
		}

		List<?> rowList = autoSelect.getResultList();
		table.setRowList(rowList);
	}

	protected String getOrderByColumn(){
		return null;
	}

}
