/*
 * Copyright 2006-2010 the Seasar Foundation and the Others.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package org.seasar.s2click.interceptor;

import org.aopalliance.intercept.MethodInvocation;
import org.apache.click.Page;
import org.seasar.framework.aop.interceptors.AbstractInterceptor;
import org.seasar.framework.util.StringUtil;
import org.seasar.s2click.annotation.Layout;

/**
 * ページクラスで共通レイアウト機能を実現するためのインターセプターです。
 * <p>
 * ページクラスに{@link Layout}アノテーションを付与しておくと、
 * {@link Page#getTemplate()}メソッドがこのインターセプターの
 * {@link #template}プロパティに設定されたテンプレートのパスを返却するようになります。
 * ただし、Layoutアノテーションでテンプレートのパスが指定されていた場合はそちらが優先されます。
 *
 * @author Naoki Takezoe
 * @see Layout
 */
public class LayoutInterceptor extends AbstractInterceptor {

	private static final long serialVersionUID = 1L;

	/**
	 * デフォルトのテンプレートのパスを指定します。
	 */
	public String template;

	public Object invoke(MethodInvocation invocation) throws Throwable {
		Class<?> pageClass = getTargetClass(invocation);
		Layout layout = pageClass.getAnnotation(Layout.class);

		if(layout != null){
			String template = layout.value();
			if(StringUtil.isNotEmpty(template)){
				return template;
			}

			if(StringUtil.isEmpty(this.template)){
				throw new RuntimeException(
						"LayoutInterceptorのtemplateプロパティに共通テンプレートのパスが設定されていません");
			}

			return this.template;
		}
		return invocation.proceed();
	}

}
