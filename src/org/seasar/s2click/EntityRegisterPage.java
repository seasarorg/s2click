package org.seasar.s2click;

import javax.annotation.Resource;

import org.seasar.extension.jdbc.EntityMetaFactory;
import org.seasar.extension.jdbc.JdbcManager;
import org.seasar.s2click.control.EntityForm;
import org.seasar.s2click.control.EntityForm.EntityFormMode;

public abstract class EntityRegisterPage extends S2ClickPage {

	private static final long serialVersionUID = 1L;

	public EntityForm form;

	@Resource
	protected JdbcManager jdbcManager;

	@Resource
	protected EntityMetaFactory entityMetaFactory;

	protected Class<?> entityClass;

	protected Class<?> listPageClass;

	/**
	 * コンストラクタ。
	 *
	 * @param entityClass エンティティクラス
	 * @param listPageClass 一覧画面のページクラス
	 */
	public EntityRegisterPage(Class<?> entityClass, Class<?> listPageClass){
		form = new EntityForm("form", entityClass, EntityFormMode.REGISTER);
		form.getSubmit().setListener(this, "onRegister");

		this.entityClass = entityClass;
		this.listPageClass = listPageClass;
	}

	public boolean onRegister(){
		if(form.isValid()){
			try {
				Object entity = entityClass.newInstance();
				form.copyTo(entity);

				// TODO 結果が1件じゃなかったらエラーにする？
				jdbcManager.insert(entity).execute();

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
