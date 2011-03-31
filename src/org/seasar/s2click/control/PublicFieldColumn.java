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
package org.seasar.s2click.control;

import java.lang.reflect.Field;

import org.apache.click.control.Column;
import org.apache.click.control.Decorator;

/**
 * publicフィールドをテーブル列として扱うための<code>Column</code>拡張クラスです。
 *
 * @author Naoki Takezoe
 */
public class PublicFieldColumn extends Column {

	private static final long serialVersionUID = 1L;

	public PublicFieldColumn() {
		super();
	}

	public PublicFieldColumn(CharSequence name, String title) {
		super(name.toString(), title);
	}

	public PublicFieldColumn(CharSequence name) {
		super(name.toString());
	}

	public PublicFieldColumn(CharSequence name, String title, Decorator decorator) {
		super(name.toString(), title);
		setDecorator(decorator);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object getProperty(String name, Object row) {
		try {
			String[] names = new String[]{name};
			if(name.indexOf('.') >= 0){
				names = name.split("\\.");
			}
			Class<?> target = row.getClass();
			Object value = row;
			for(String propertyName: names){
				Field field = target.getField(propertyName);
				value = field.get(value);
				target = field.getType();
			}
			return value;
		} catch(Exception ex){
			throw new RuntimeException(ex);
		}
	}

}

