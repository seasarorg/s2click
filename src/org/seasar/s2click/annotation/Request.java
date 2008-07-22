package org.seasar.s2click.annotation;

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
public @interface Request {

	/**
	 * リクエストパラメータ名。
	 * 省略した場合はフィールド名をパラメータ名とみなします。
	 * 
	 * @return リクエストパラメータ名
	 */
	String value() default "";
	
}
