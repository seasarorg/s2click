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
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import org.apache.click.control.Field;

/**
 * {@link Properties}アノテーションと組み合わせることで
 * フォームクラスのpublicフィールドとして宣言した{@link Field}コントロールの
 * 属性を指定することのできるアノテーションです。
 *
 * @author Naoki Takezoe
 * @since 1.0.5
 * @see Properties
 */
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface Attribute {

	String name();

	String value();

}
