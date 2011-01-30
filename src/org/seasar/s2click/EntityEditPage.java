package org.seasar.s2click;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.seasar.extension.jdbc.EntityMeta;
import org.seasar.extension.jdbc.EntityMetaFactory;
import org.seasar.extension.jdbc.JdbcManager;
import org.seasar.extension.jdbc.PropertyMeta;
import org.seasar.s2click.control.EntityForm;
import org.seasar.s2click.control.EntityForm.EntityFormMode;

public class EntityEditPage extends S2ClickPage {

	private static final long serialVersionUID = 1L;

	public EntityForm form;

	@Resource
	protected EntityMetaFactory entityMetaFactory;

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
		form.getCancel().setListener(this, "onCancel");

		this.entityClass = entityClass;
		this.listPageClass = listPageClass;
	}

	@Override
	public void onInit() {
		super.onInit();

		// IDの取得
		EntityMeta em = entityMetaFactory.getEntityMeta(entityClass);
		List<String> idList = new ArrayList<String>();
		for(PropertyMeta pm: em.getIdPropertyMetaList()){
			String value = getContext().getRequestParameter(pm.getName());
			if(StringUtils.isEmpty(value)){
				throw new RuntimeException(pm.getName() + " is not specified.");
			}
			idList.add(value);
		}

		Object entity = jdbcManager.from(entityClass)
			.id(idList.toArray()).disallowNoResult().getSingleResult();

		form.copyFrom(entity);
	}

	/**
	 * エンティティの更新処理を行い、一覧画面に戻ります。
	 *
	 * @return
	 */
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

	public boolean onCancel(){
		setRedirect(listPageClass);
		return false;
	}
}
