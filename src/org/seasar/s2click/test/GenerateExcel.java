package org.seasar.s2click.test;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 初期データの自動投入直後に指定したテーブルのデータを期待値比較用のExcelにエクスポートします。
 * <p>
 * データベース検索処理の結果を検証するためのExcelファイルを作成する場合に使用します。
 * すでに期待値のExcelが存在する場合は何も行いません。
 * 
 * @author Naoki Takezoe
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface GenerateExcel {
	
	/**
	 * 生成対象のテーブル。
	 */
	Table[] tables();

}
