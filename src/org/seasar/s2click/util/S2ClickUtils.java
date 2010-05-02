/*
 * Copyright 2006-2010 the Seasar Foundation and the Others.
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
package org.seasar.s2click.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.click.Context;
import org.apache.click.control.Field;
import org.apache.click.control.FileField;
import org.apache.click.control.Form;
import org.apache.click.control.HiddenField;
import org.apache.click.extras.control.HiddenList;
import org.apache.click.extras.control.PickList;
import org.apache.click.service.ConfigService;
import org.apache.click.util.ClickUtils;
import org.seasar.framework.beans.Converter;
import org.seasar.framework.beans.util.Copy;
import org.seasar.framework.container.S2Container;
import org.seasar.framework.container.factory.SingletonS2ContainerFactory;

/**
 * S2Click内で使用するユーティリティメソッドを提供します。
 *
 * @author Naoki Takezoe
 */
public class S2ClickUtils {

	/**
	 * <code>ClickApp</code>を取得します。
	 *
	 * @return <code>ClickApp</code>のインスタンス。
	 */
	public static ConfigService getConfigService(){
		return (ConfigService) Context.getThreadLocalContext()
			.getServletContext().getAttribute(ConfigService.CONTEXT_NAME);
	}

	/**
	 * フォームのコントロールを<code>HiddenField</code>（もしくは<code>HiddenList</code>）に変換します。
	 *
	 * @param form 変換するフォーム
	 * @throws IllegalArgumentException hiddenに変換できないコントロールがフォームに含まれていた場合
	 */
	@SuppressWarnings("unchecked")
	public static void convertToHidden(Form form) throws IllegalArgumentException {
		for(Object obj: form.getFieldList().toArray()){
			Field field = (Field) obj;

			// もともとhiddenの場合はなにもしない
			if(field.isHidden()){
				continue;
			}

			// FileFieldの場合は変換できない
			if(field instanceof FileField){
				throw new IllegalArgumentException("FileFieldはHiddenFieldに変換できません。");
			}

			// PickListの場合
			if(field instanceof PickList){
				Object values = ((PickList) field).getSelectedValues();
				HiddenList hidden = new HiddenList(field.getName());
				for(Object valueItem: List.class.cast(values)){
					hidden.addValue(valueItem.toString());
				}

				form.remove(field);
				form.add(hidden);

				continue;
			}

			Object value = field.getValueObject();
			if(value == null){
				value = "";
			}

			form.remove(field);

			if(value instanceof List){
				// 値がListの場合はHiddenListを使用
				HiddenList hidden = new HiddenList(field.getName());
				for(Object valueItem: List.class.cast(value)){
					hidden.addValue(valueItem.toString());
				}
				form.add(hidden);

			} else {
				// List以外の場合はHiddenFieldに文字列として格納
				HiddenField hidden = new HiddenField(field.getName(), value.toString());
				form.add(hidden);
			}
		}
	}

	/**
	 * 連続する半角スペースの2文字目以降をを&nbspに変換します。
	 *
	 * @param value 文字列
	 * @return 変換後の文字列
	 */
	public static String convertNbsp(String value){
		StringBuilder sb = new StringBuilder();
		boolean flag = false;
		for(int i=0;i<value.length();i++){
			char c = value.charAt(i);
			if(c == ' '){
				if(flag){
					sb.append("&nbsp;");
				} else {
					sb.append(c);
					flag = true;
				}
			} else {
				sb.append(c);
				flag = false;
			}
		}
		return sb.toString();
	}

	/**
	 * <code>SingletonS2ContainerFactory</code>からコンポーネントを取得します。
	 *
	 * @param <T> 取得するコンポーネントの型
	 * @param clazz 取得するコンポーネントの型
	 * @return コンポーネント
	 */
	@SuppressWarnings("unchecked")
	public static <T> T getComponent(Class<T> clazz){
		S2Container container = SingletonS2ContainerFactory.getContainer();
		return (T) container.getComponent(clazz);
	}

	/**
	 * publicフィールドに対応した{@link ClickUtils#copyObjectToForm(Object, Form, boolean)}です。
	 *
	 * @param object コピー元のオブジェクト
	 * @param form コピー先のフォーム
	 * @param debug デバッグログを出力するかどうか
	 */
    public static void copyObjectToForm(Object object, Form form,
            boolean debug) {

        if (object == null) {
            throw new IllegalArgumentException("Null object parameter");
        }
        if (form == null) {
            throw new IllegalArgumentException("Null form parameter");
        }
        Map<Object, Object> map = new HashMap<Object, Object>();
        new S2ClickCopy(object, map).execute();
        ClickUtils.copyObjectToForm(map, form, debug);
    }

    /**
     * publicフィールドに対応した{@link ClickUtils#copyFormToObject(Form, Object, boolean)}です。
     *
     * @param form コピー元のフォーム
     * @param object コピー先のオブジェクト
     * @param debug デバッグログを出力するかどうか
     */
    @SuppressWarnings("unchecked")
    public static void copyFormToObject(Form form, Object object,
            boolean debug) {
        if (form == null) {
            throw new IllegalArgumentException("Null form parameter");
        }
        if (object == null) {
            throw new IllegalArgumentException("Null object parameter");
        }

        Map map = new HashMap();
        for(Field field: (List<Field>) form.getFieldList()){
        	map.put(field.getName(), "");
        }
        ClickUtils.copyFormToObject(form, map, debug);
        new S2ClickCopy(map, object).execute();
    }

    /**
     * 日付にデフォルトのコンバータを適用しない<code>Beans.copy()</code>の拡張。
     */
    private static class S2ClickCopy extends Copy {

    	/**
    	 * コンストラクタ。
    	 *
    	 * @param src コピー元
    	 * @param dest コピー先
    	 * @throws NullPointerException
    	 */
		public S2ClickCopy(Object src, Object dest) throws NullPointerException {
			super(src, dest);
		}

		/**
		 * 常にnullを返すことで日付にデフォルトのコンバータを適用しません。
		 */
		@Override
		protected Converter findDefaultConverter(Class<?> clazz) {
			return null;
		}
    }

}
