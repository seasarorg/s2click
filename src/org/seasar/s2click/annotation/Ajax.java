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
