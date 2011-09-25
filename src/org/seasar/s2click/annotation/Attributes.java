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

import org.apache.click.control.Field;
import org.apache.click.control.TextArea;
import org.apache.click.control.TextField;
import org.apache.commons.lang.StringUtils;

/**
 * フォームクラスのpublicフィールドとして宣言した{@link Field}コントロールに付与することで、
 * Fieldのプロパティの設定を行うことができます。
 *
 * @author Naoki Takezoe
 * @since 1.0.5
 * @see Attribute
 * @see AttributesProcessor
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface Attributes {

	String id() default "";

	String label() default "";

	boolean required() default false;

	boolean readonly() default false;

	boolean disabled() default false;

	String width() default "";

	String style() default "";

	int size() default -1;

	int rows() default -1;

	int cols() default -1;

	int minLength() default -1;

	int maxLength() default -1;

	Attribute[] value() default {};

	/**
	 * {@link Attributes}アノテーションの処理を行います。
	 *
	 * @author Naoki Takezoe
	 * @since 1.0.5
	 */
	public static class AttributesProcessor {

		public static void process(Attributes attrs, Field field){
			if(StringUtils.isNotEmpty(attrs.id())){
				field.setId(attrs.id());
			}
			if(StringUtils.isNotEmpty(attrs.label())){
				field.setLabel(attrs.label());
			}
			if(attrs.required() == true){
				field.setRequired(true);
			}
			if(attrs.readonly() == true){
				field.setReadonly(true);
			}
			if(attrs.disabled() == true){
				field.setDisabled(true);
			}
			if(StringUtils.isNotEmpty(attrs.width())){
				field.setWidth(attrs.width());
			}
			if(StringUtils.isNotEmpty(attrs.style())){
				for(String styles: attrs.style().split(";")){
					String[] dim = styles.split(":");
					field.setStyle(dim[0].trim(), dim[1].trim());
				}
			}
			if(attrs.size() > -1 && field instanceof TextField){
				((TextField) field).setSize(attrs.size());
			}
			if(attrs.rows() > -1 && field instanceof TextArea){
				((TextArea) field).setRows(attrs.rows());
			}
			if(attrs.cols() > -1 && field instanceof TextArea){
				((TextArea) field).setCols(attrs.cols());
			}
			if(attrs.minLength() > -1){
				if(field instanceof TextField){
					((TextField) field).setMinLength(attrs.minLength());
				} else if(field instanceof TextArea){
					((TextArea) field).setMinLength(attrs.minLength());
				}
			}
			if(attrs.maxLength() > -1){
				if(field instanceof TextField){
					((TextField) field).setMaxLength(attrs.maxLength());
				} else if(field instanceof TextArea){
					((TextArea) field).setMaxLength(attrs.maxLength());
				}
			}
			for(Attribute attr: attrs.value()){
				field.setAttribute(attr.name(), attr.value());
			}
		}
	}
}
