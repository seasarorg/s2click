/*
 * Copyright 2006-2009 the Seasar Foundation and the Others.
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
package org.seasar.s2click.test;

/**
 * {@link Assert}アノテーションで比較するテーブルの情報を指定するアノテーションです。
 * 
 * @author Naoki Takezoe
 */
public @interface Table {
	
	/**
	 * テーブル名。
	 */
	String name();
	
	/**
	 * 比較対象のカラム名。
	 * この属性を省略し、なおかつ<code>excludeColumns</code>を指定しなかった場合はすべてのカラムが比較対象になります。
	 */
	String[] includeColumns() default {};
	
	/**
	 * 比較対象外のカラム名。
	 * この属性を省略し、なおかつ<code>includeColumns</code>を指定しなかった場合はすべてのカラムが比較対象になります。
	 */
	String[] excludeColumns() default {};
	
	/**
	 * Excel生成時に条件として使用するWHERE句を指定します。
	 */
	String where() default "";
	
	/**
	 * Excel生成時に行のソートに使用するORDER BY句を指定します。
	 */
	String orderBy() default "";
	
}
