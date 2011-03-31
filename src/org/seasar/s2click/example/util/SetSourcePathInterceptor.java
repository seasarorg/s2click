/*
 * Copyright 2006-2011 the Seasar Foundation and the Others.
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
package org.seasar.s2click.example.util;

import org.aopalliance.intercept.MethodInvocation;
import org.apache.click.Page;
import org.seasar.framework.aop.interceptors.AbstractInterceptor;
import org.seasar.s2click.annotation.Layout;

/**
 *
 * @author Naoki Takezoe
 */
public class SetSourcePathInterceptor extends AbstractInterceptor {

	private static final long serialVersionUID = 1L;

	public Object invoke(MethodInvocation invocation) throws Throwable {

		Class<?> pageClass = getTargetClass(invocation);

		if(pageClass.getAnnotation(Layout.class) != null){
			String className = pageClass.getName();
			String sourcePath = className.replaceAll("\\.", "/") + ".java";

			Page page = (Page) invocation.getThis();
			page.addModel("sourcePath", "/WEB-INF/classes/" + sourcePath);
		}

		return invocation.proceed();
	}

}
