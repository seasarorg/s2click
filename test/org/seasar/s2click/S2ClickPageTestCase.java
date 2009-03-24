package org.seasar.s2click;

import java.lang.reflect.Type;

import org.seasar.framework.util.tiger.GenericUtil;

/**
 * ページクラスのテストケースの抽象基底クラスです。
 * 
 * @author Naoki Takezoe
 * @param <T> ページクラス
 */
public abstract class S2ClickPageTestCase<T> extends S2ClickTestCase {
	
	protected T page;
	
	/**
	 * ページクラスのインスタンスを生成・初期化します。
	 * 生成されたページクラスのインスタンスは{@link #page}フィールドにセットされます。
	 */
	public void setUp() throws Exception {
		super.setUp();
		
		page = createPage();
		
//		Type type = GenericUtil.getTypeVariableMap(getClass())
//			.get(S2ClickPageTestCase.class.getTypeParameters()[0]);
//		page = SingletonS2Container.getComponent((Class<T>) type);
		
		initPage(page);
	}
	
	/**
	 * ページクラスのインスタンスを生成します。
	 * <p>
	 * {@link #setUp()}メソッドから呼び出されます。
	 * 
	 * @return ページクラスのインスタンス
	 */
	protected T createPage(){
		try {
			Type type = GenericUtil.getTypeVariableMap(getClass()).get(
					S2ClickPageTestCase.class.getTypeParameters()[0]);
			
			@SuppressWarnings("unchecked")
			Class<T> clazz = (Class<T>) type;
			
			return clazz.newInstance();
			
		} catch(Exception ex){
			throw new RuntimeException(ex);
		}
	}
	
	/**
	 * ページクラスの初期化を行います。
	 * 必要に応じてサブクラスでオーバーライドしてください。
	 * <p>
	 * {@link #setUp()}メソッドから呼び出されます。
	 * 
	 * @param page ページクラスのインスタンス
	 */
	protected void initPage(T page){
	}

}
