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
package org.seasar.s2click.example.page;

import org.seasar.s2click.control.GreyboxButton;
import org.seasar.s2click.control.GreyboxLink;
import org.seasar.s2click.example.form.GreyboxForm;

/**
 * {@link GreyboxButton}と{@link GreyboxLink}のサンプルです。
 * <p>
 * Greyboxを使用して値を選択するためのモーダルダイアログを表示します。
 * 
 * @author Naoki Takezoe
 * @since 0.4.0
 */
public class GreyboxPage extends LayoutPage {

	private static final long serialVersionUID = 1L;

	public String title = "GreyBox";
	
	public GreyboxLink link = new GreyboxLink("link", "参照...", "ユーザを選択", GreyboxSelectPage.class);

	public GreyboxForm form = new GreyboxForm("form");
	
}
