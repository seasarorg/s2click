package org.seasar.s2click;

import javax.annotation.Resource;

import org.seasar.extension.jdbc.EntityMetaFactory;
import org.seasar.extension.jdbc.JdbcManager;
import org.seasar.s2click.control.EntityForm;
import org.seasar.s2click.control.EntityForm.EntityFormMode;

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
		form = new EntityForm("form", config.getEntityClass(), EntityFormMode.REGISTER);
		form.getSubmit().setListener(this, "onRegister");
		form.getCancel().setListener(this, "onCancel");

		this.config = config;
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

				// TODO 結果が1件じゃなかったらエラーにする？
				jdbcManager.insert(entity).execute();

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
