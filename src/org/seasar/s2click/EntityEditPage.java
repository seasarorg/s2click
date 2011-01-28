package org.seasar.s2click;

import javax.annotation.Resource;

import org.seasar.extension.jdbc.JdbcManager;
import org.seasar.s2click.annotation.Request;
import org.seasar.s2click.control.EntityForm;
import org.seasar.s2click.control.EntityForm.EntityFormMode;

public class EntityEditPage extends S2ClickPage {

	public EntityForm form;

	@Request(required=true)
	public String id;

	@Resource
	protected JdbcManager jdbcManager;

	protected Class<?> entityClass;

	protected Class<?> listPageClass;

	/**
	 * コンストラクタ。
	 *
	 * @param entityClass エンティティクラス
	 * @param listPageClass 一覧画面のページクラス
	 */
	public EntityEditPage(Class<?> entityClass, Class<?> listPageClass){
		form = new EntityForm("form", entityClass, EntityFormMode.EDIT);
		form.getSubmit().setListener(this, "onUpdate");
		this.entityClass = entityClass;
	}

	@Override
	public void onInit() {
		super.onInit();
		// TODO 初期データのセット（型変換が必要？）
		Object entity = jdbcManager.from(entityClass).id(id).disallowNoResult().getSingleResult();
		form.copyFrom(entity);
	}

	public boolean onUpdate(){
		if(form.isValid()){
			try {
				Object entity = entityClass.newInstance();
				form.copyTo(entity);

				// TODO 結果が1件じゃなかったらエラーにする？
				jdbcManager.update(entity).execute();

				setRedirect(listPageClass);
				return false;

			} catch(IllegalAccessException ex){
				throw new RuntimeException(ex);
			} catch(InstantiationException ex){
				throw new RuntimeException(ex);
			}
		}
		return true;
	}
}
