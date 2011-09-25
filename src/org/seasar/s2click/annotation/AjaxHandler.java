package org.seasar.s2click.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.apache.commons.lang.StringUtils;
import org.seasar.s2click.control.AjaxControl;
import org.seasar.s2click.control.AjaxSubmit;
import org.seasar.s2click.util.AjaxUtils;

/**
 * フォームクラスのpublicフィールドとして宣言した{@link AjaxControl}コントロールに付与することで、
 * Ajaxハンドラの指定を行うことのできるアノテーションです。
 * <p>
 * {@link AjaxControl#addAjaxHandler(String, String)}での設定をこのアノテーションで代用することができます。
 * たとえば以下のような{@link AjaxSubmit}を使用したフォームクラスがあるとします。
 * <pre>
 * public class JsonForm extends S2ClickForm {
 *
 *   public AjaxSubmit search = new AjaxSubmit("submit", "検索");
 *
 *   public JsonForm(String name){
 *     super(name);
 *     setFieldAutoRegisteration(true);
 *
 *     search.addAjaxHandler(AjaxUtils.ON_CREATE, "startProgress");
 *     search.addAjaxHandler(AjaxUtils.ON_COMPLETE, "displayResult");
 *   }
 *
 * }
 * </pre>
 * このコードは{@link AjaxHandler}アノテーションを使用することで以下のように書き換えることができます。
 * <pre>
 * public class JsonForm extends S2ClickForm {
 *
 *   @AjaxHandler(onCreate="startProgress", onComplete="displayResult")
 *   public AjaxSubmit search = new AjaxSubmit("submit", "検索");
 *
 *   public JsonForm(String name){
 *     super(name);
 *     setFieldAutoRegisteration(true);
 *   }
 *
 * }
 * </pre>
 *
 * @author Naoki Takezoe
 * @since 1.0.5
 * @see AjaxControl
 * @see AjaxHandlerProcessor
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface AjaxHandler {

	String onCreate() default "";

	String onComplete() default "";

	String onException() default "";

	String onFailure() default "";

	String onSuccess() default "";

	/**
	 * {@link AjaxHandler}アノテーションの処理を行います。
	 *
	 * @author Naoki Takezoe
	 * @since 1.0.5
	 */
	public static class AjaxHandlerProcessor {

		public static void process(AjaxHandler handler, AjaxControl field){
			if(StringUtils.isNotEmpty(handler.onCreate())){
				field.addAjaxHandler(AjaxUtils.ON_CREATE, handler.onCreate());
			}
			if(StringUtils.isNotEmpty(handler.onComplete())){
				field.addAjaxHandler(AjaxUtils.ON_COMPLETE, handler.onComplete());
			}
			if(StringUtils.isNotEmpty(handler.onException())){
				field.addAjaxHandler(AjaxUtils.ON_EXCEPTION, handler.onException());
			}
			if(StringUtils.isNotEmpty(handler.onFailure())){
				field.addAjaxHandler(AjaxUtils.ON_FAILURE, handler.onFailure());
			}
			if(StringUtils.isNotEmpty(handler.onSuccess())){
				field.addAjaxHandler(AjaxUtils.ON_SUCCESS, handler.onSuccess());
			}
		}
	}

}
