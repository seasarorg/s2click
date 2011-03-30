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
package org.seasar.s2click.test;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.ServletContext;

import org.apache.click.Page;
import org.apache.click.PageInterceptor;
import org.apache.click.service.ConfigService;
import org.apache.click.service.ConsoleLogService;
import org.apache.click.service.FileUploadService;
import org.apache.click.service.LogService;
import org.apache.click.service.MessagesMapService;
import org.apache.click.service.ResourceService;
import org.apache.click.service.TemplateService;
import org.apache.click.util.Format;

@SuppressWarnings("rawtypes")
public class MockConfigService implements ConfigService {

	private TemplateService templateService;
	private Map<Class, String> pagePathMap = new HashMap<Class, String>();

	public void setPagePath(Class pageClass, String pagePath){
		this.pagePathMap.put(pageClass, pagePath);
	}

	public Format createFormat() {
		return null;
	}

	public String getApplicationMode() {
		return null;
	}

	public String getCharset() {
		return null;
	}

	public Class<? extends Page> getErrorPageClass() {
		return null;
	}

	public FileUploadService getFileUploadService() {
		return null;
	}

	public Locale getLocale() {
		return null;
	}

	public LogService getLogService() {
		return new ConsoleLogService();
	}

	public Class<? extends Page> getNotFoundPageClass() {
		return null;
	}

	public Class<? extends Page> getPageClass(String path) {
		return null;
	}

	public Field getPageField(Class<? extends Page> pageClass, String fieldName) {
		return null;
	}

	public Field[] getPageFieldArray(Class<? extends Page> pageClass) {
		return null;
	}

	public Map<String, Field> getPageFields(Class<? extends Page> pageClass) {
		return null;
	}

	public Map<String, Object> getPageHeaders(String path) {
		return null;
	}

	public String getPagePath(Class<? extends Page> pageClass) {
		return this.pagePathMap.get(pageClass);
	}

	public ServletContext getServletContext() {
		return null;
	}

	public void setTemplateService(TemplateService templateService){
		this.templateService = templateService;
	}

	public TemplateService getTemplateService() {
		return templateService;
	}

	public boolean isJspPage(String path) {
		return false;
	}

	public boolean isPagesAutoBinding() {
		return false;
	}

	public boolean isProductionMode() {
		return false;
	}

	public boolean isProfileMode() {
		return false;
	}

	public void onDestroy() {
	}

	public void onInit(ServletContext servletContext) throws Exception {
	}

	public AutoBinding getAutoBindingMode() {
		return null;
	}

	public List<Class<? extends Page>> getPageClassList() {
		return null;
	}

	public ResourceService getResourceService() {
		return null;
	}

	public boolean isTemplate(String path) {
		return false;
	}

	public MessagesMapService getMessagesMapService() {
		return null;
	}

	public List<PageInterceptor> getPageInterceptors() {
		return null;
	}

}
