package org.seasar.s2click.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.seasar.s2click.interceptor.LayoutInterceptor;

/**
 * 共通テンプレートを使用するページクラスに付与するアノテーションです。
 *
 * @author Naoki Takezoe
 * @see LayoutInterceptor
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Layout {

	/**
	 * 共通テンプレートのパスを指定します。
	 * <p>
	 * 省略した場合は{@link LayoutInterceptor#template}プロパティに
	 * 設定されたテンプレートのパスが使用されます。
	 *
	 * @return 共通テンプレートのパス
	 */
	String value() default "";

}
