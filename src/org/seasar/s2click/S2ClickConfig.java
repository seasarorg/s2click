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
package org.seasar.s2click;

import java.util.HashMap;
import java.util.Map;

import org.apache.click.service.ClickResourceService;
import org.apache.click.service.ConsoleLogService;
import org.apache.click.service.LogService;
import org.apache.click.service.ResourceService;
import org.apache.click.service.TemplateService;
import org.apache.click.service.VelocityTemplateService;
import org.apache.click.util.Format;
import org.seasar.s2click.util.S2ClickFormat;

/**
 * diconファイルでClickの設定を行うためのクラス。
 *
 * @author Naoki Takezoe
 */
public class S2ClickConfig {

	/**
	 * 文字コード。デフォルトはUTF-8です。
	 */
	public String charset = "UTF-8";

	/**
	 * ページテンプレートで利用可能なフォーマットクラス。
	 * デフォルトは<code>net.sf.click.util.Format</code>です。
	 */
	public Class<? extends Format> formatClass = S2ClickFormat.class;

	/**
	 * Clickの動作モード。以下の値が指定可能です。デフォルトはdevelopmentです。
	 * <ul>
	 *   <li>production</li>
	 *   <li>profile</li>
	 *   <li>development</li>
	 *   <li>debug</li>
	 *   <li>trace</li>
	 * </ul>
	 * モードによってClickの動作（HTMLテンプレートのリロードを行うかどうか等）やログに出力される内容が変化します。
	 * 詳細については<a href="http://click.sourceforge.net/docs/configuration.html#application-configuration">Clickのドキュメント</a>を参照してください。
	 */
	public String mode = "development";

	/**
	 * 共通レスポンスヘッダ。
	 */
	public Map<String, String> headers = new HashMap<String, String>();

	/**
	 * ロケール。
	 */
	public String locale;

	/**
	 * ログサービス。
	 * デフォルトは<code>ConsoleLogService</code>です。
	 */
	public Class<? extends LogService> logService = ConsoleLogService.class;

	/**
	 * ログサービスのプロパティ。
	 */
	public Map<String, String> logServicePropertyMap = new HashMap<String, String>();

	/**
	 * リソースサービス。
	 * デフォルトは<code>ClickResourceService</code>です。
	 */
	public Class<? extends ResourceService> resourceService = ClickResourceService.class;

	/**
	 * リソースサービスのプロパティ。
	 */
	public Map<String, String> resourceServicePropertyMap = new HashMap<String, String>();

	/**
	 * テンプレートサービス。
	 * デフォルトは<code>VelocityTemplateService></code>です。
	 */
	public Class<? extends TemplateService> templateService = VelocityTemplateService.class;

	/**
	 * テンプレートサービスのプロパティ。
	 */
	public Map<String, String> templateServicePropertyMap = new HashMap<String, String>();

	/**
	 * ログサービスのプロパティを追加します。
	 *
	 * @param name プロパティ名
	 * @param value プロパティ値
	 */
	public void addLogServiceProperty(String name, String value){
		this.logServicePropertyMap.put(name, value);
	}

	/**
	 * テンプレートサービスのプロパティを追加します。
	 *
	 * @param name プロパティ名
	 * @param value プロパティ値
	 */
	public void addTemplateServiceProperty(String name, String value){
		this.templateServicePropertyMap.put(name, value);
	}

	/**
	 * 共通レイアウトのテンプレートのパス。
	 */
	public String layoutTemplatePath = null;

}
