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
package org.seasar.s2click.control;

import java.util.ArrayList;
import java.util.List;

import net.sf.click.control.Field;
import net.sf.click.util.HtmlStringBuffer;

/**
 * 複数の値を格納可能なhiddenフィールドです。
 * <p>
 * Click Frameworkではhiddenフィールドを出力するために<code>HiddenField</code>が用意されていますが、
 * <code>Form</code>には同じ名称で複数のコンポーネントを追加することができません。
 * そのため、同じパラメータ名で複数の値をhiddenフィールドとして出力したいという場合には<code>HiddenField</code>を使用することができません。
 * このような場合に<code>HiddenList</code>を使用します。
 * </p>
 * <code>HiddenList</code>の使用例を以下に示します。
 * <pre>
 * HiddenList hiddenList = new HiddenList("hiddenList");
 * hiddenList.addValue("001");
 * hiddenList.addValue("002"); </pre>
 * 上記の<code>HiddenList</code>は以下のようなHTMLを出力します。
 * input要素のid属性には値を追加した順番に連番が振られることに注意してください。
 * <pre>
 * &lt;input type="hidden" name="hiddenList" id="form-hiddenList_1" value="001"/&gt;
 * &lt;input type="hidden" name="hiddenList" id="form-hiddenList_2" value="002"/&gt; </pre>
 * 
 * @author Naoki Takezoe
 * @since 0.4.0
 */
public class HiddenList extends Field {

	private static final long serialVersionUID = 1L;
	
	protected List<String> valueObject;
	
	/**
	 * コンストラクタ。
	 * 
	 * @param name コンポーネント名
	 */
	public HiddenList(String name){
		super(name);
	}
	
	@SuppressWarnings("unchecked")
	public void setValueObject(Object valueObject){
		if(!(valueObject instanceof List)){
			throw new IllegalArgumentException();
		}
		this.valueObject = (List<String>) valueObject;
	}
	
	public Object getValueObject(){
		return this.valueObject;
	}
	
	/**
	 * <code>HiddenList</code>に追加された値を取得します。
	 * 
	 * @return 値のリスト
	 */
	public List<String> getValues(){
		@SuppressWarnings("unchecked")
		List<String> list = (List<String>) getValueObject();
		if(list == null){
			list = new ArrayList<String>();
			setValueObject(list);
		}
		return list;
	}
	
	/**
	 * <code>HiddenList</code>に値を追加します。
	 * 
	 * @param value 値
	 */
	public void addValue(String value){
		getValues().add(value);
	}

	@Override
	public void bindRequestValue() {
        String[] values = getContext().getRequestParameterValues(getName());
        
        if(values != null){
			List<String> list = new ArrayList<String>();
			for(String value: values){
				list.add(value);
			}
			setValueObject(list);
        }
	}
	
	@Override
	public boolean isHidden(){
		return true;
	}
	
	@Override
	public String toString(){
		HtmlStringBuffer buffer = new HtmlStringBuffer();
		List<String> values = getValues();
		
        for(int i=0; i < values.size(); i++){
        	buffer.elementStart("input");
        	buffer.appendAttribute("type", "hidden");
        	buffer.appendAttribute("name", getName());
        	buffer.appendAttribute("id", getId() + "_" + (i + 1));
            buffer.appendAttribute("value", values.get(i));
            buffer.elementEnd();
        }
        
        return buffer.toString();
	}
}
