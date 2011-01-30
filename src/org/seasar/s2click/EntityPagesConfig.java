package org.seasar.s2click;

/**
 * エンティティの一覧、登録、編集、削除を行うページの設定を行うクラスです。
 *
 * @author Naoki Takezoe
 */
public class EntityPagesConfig {

	private Class<?> entityClass;
	private Class<?> listPageClass;
	private Class<?> registerPageClass;
	private Class<?> editPageClass;
	private Class<?> deletePageClass;

	/**
	 * コンストラクタ。
	 *
	 * @param entityClass 対象のエンティティの型
	 * @param listPageClass 一覧画面のページクラス
	 * @param regiserPageClass 登録画面のページクラス
	 * @param editPageClass 編集画面のページクラス
	 * @param deletePageClass 削除画面のページクラス
	 */
	public EntityPagesConfig(Class<?> entityClass,
			Class<?> listPageClass, Class<?> regiserPageClass, Class<?> editPageClass, Class<?> deletePageClass){
		this.entityClass       = entityClass;
		this.listPageClass     = listPageClass;
		this.registerPageClass = regiserPageClass;
		this.editPageClass     = editPageClass;
		this.deletePageClass   = deletePageClass;
	}

	public Class<?> getEntityClass() {
		return entityClass;
	}

	public Class<?> getListPageClass() {
		return listPageClass;
	}

	public Class<?> getRegisterPageClass() {
		return registerPageClass;
	}

	public Class<?> getEditPageClass() {
		return editPageClass;
	}

	public Class<?> getDeletePageClass() {
		return deletePageClass;
	}

}
