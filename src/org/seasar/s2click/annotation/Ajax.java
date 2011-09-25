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
package org.seasar.s2click.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.seasar.s2click.S2ClickPage;

/**
 * Ajaxで呼び出し可能なページクラスのpublicメソッドに付与します。
 * <p>
 * {@link S2ClickPage}のサブクラスのpublicメソッドにこのアノテーションを付与することで、
 * 該当のメソッドをクライアントサイドJavaScriptからAjaxで呼び出すことができるようになります。
 * S2ClickPageはこのアノテーションを付与したpublicメソッドをAjaxで呼び出すためのJavaScript関数を自動生成します。
 * HTMLテンプレートでは生成されたJavaScript関数を使用することでリモートメソッド呼び出しを簡単に行うことができます。
 * <p>
 * ただし、このアノテーションを付与したメソッドをAjaxで呼び出す場合、
 * 通常のページの処理で行われる<code>onInit()</code>、<code>onSecurityCheck()</code>などの呼び出しは行われません。
 * アノテーションを付与したメソッドのみが呼び出されます。
 *
 * @author Naoki Takezoe
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface Ajax {

}
