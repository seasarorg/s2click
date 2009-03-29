package org.seasar.s2click;

import java.lang.reflect.Type;

import org.seasar.framework.container.SingletonS2Container;
import org.seasar.framework.util.tiger.GenericUtil;

/**
 * サービスクラスのテストケースの抽象基底クラスです。
 * 
 * 
 * @author Naoki Takezoe
 *
 * @param <T> テスト対象のサービスクラス
 */
public abstract class S2ClickServiceTestCase<T> extends S2ClickTestCase {
	
	/**
	 * テスト対象のサービスクラスのインスタンス。
	 */
	protected T service;
	
	@Override
	@SuppressWarnings("unchecked")
	protected void setUpAfterContainerInit() throws Throwable {
		super.setUpAfterContainerInit();
		
		Type type = GenericUtil.getTypeVariableMap(getClass())
			.get(S2ClickServiceTestCase.class.getTypeParameters()[0]);

		service = (T) SingletonS2Container.getComponent((Class) type);
	}
	
}
