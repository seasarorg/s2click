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
