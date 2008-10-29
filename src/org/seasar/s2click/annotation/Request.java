package org.seasar.s2click.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * リクエストパラメータをバインドするページクラスのpublicフィールドに付与します。
 * 
 * @author Naoki Takezoe
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Request {

	/**
	 * リクエストパラメータ名。
	 * 省略した場合はフィールド名をパラメータ名とみなします。
	 * 
	 * @return リクエストパラメータ名
	 */
	String name() default "";
	
	/**
	 * パラメータが必須かどうかを指定します。
	 * 
	 * @return パラメータが必須かどうか
	 */
	boolean required() default false;
	
}
