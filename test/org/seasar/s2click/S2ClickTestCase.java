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
package org.seasar.s2click;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;

import org.apache.click.ClickServlet;
import org.apache.click.MockContext;
import org.apache.click.service.ConfigService;
import org.apache.click.servlet.MockRequest;
import org.apache.click.servlet.MockResponse;
import org.apache.click.servlet.MockServletConfig;
import org.apache.click.servlet.MockServletContext;
import org.apache.commons.io.IOUtils;
import org.seasar.extension.unit.S2TestCase;

/**
 * 
 * @author Naoki Takezoe
 */
public abstract class S2ClickTestCase extends S2TestCase {
	
	protected MockServletConfig config;
	
	protected MockServletContext context;
	
	protected MockRequest request;
	
	protected MockResponse response;
	
	protected ClickServlet servlet;
	
	protected MockConfigService configService;
	
	public void setUp() throws Exception {
		super.setUp();
		
		context = new MockServletContext();
		config = new MockServletConfig(context);
		request = new MockRequest();
		response = new MockResponse();
		servlet = new ClickServlet();
		
		MockContext.initContext(config, request, response, servlet);
		
		configService = new MockConfigService();
		setConfigService(configService);
	}
	
	protected void setConfigService(ConfigService configService){
		MockContext.getThreadLocalContext().getServletContext().setAttribute(
				ConfigService.CONTEXT_NAME, configService);
	}
	
	protected Object getField(Object obj, String fieldName){
		try {
			Class<?> clazz = obj.getClass();
			while(clazz != null){
				for(Field field: clazz.getDeclaredFields()){
					if(field.getName().equals(fieldName)){
						field.setAccessible(true);
						return field.get(obj);
					}
				}
				clazz = clazz.getSuperclass();
			}
			throw new RuntimeException("フィールドが存在しません。");
			
		} catch(Exception ex){
			throw new RuntimeException(ex);
		}
	}
	
	/**
	 * テストケースと同じパッケージにあるテキストファイルを読み込み、文字列として返却します。
	 * テキストファイルの文字コードはUTF-8である必要があります。
	 * また、改行コードはLFに統一されます。
	 * 
	 * @param fileName ファイル名
	 * @return ファイルの内容
	 * @throws RuntimeException ファイルの読み込みに失敗した場合
	 */
	protected String load(String fileName){
		try {
			InputStream in = getClass().getResourceAsStream(fileName);
			String text = IOUtils.toString(in, "UTF-8");
			text = text.replaceAll("\r\n", "\n");
			text = text.replaceAll("\r", "\n");
			return text;
		} catch(IOException ex){
			throw new RuntimeException(ex);
		}
	}
	
}
