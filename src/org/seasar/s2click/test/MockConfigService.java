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

import org.apache.click.service.ConfigService;
import org.apache.click.service.ConsoleLogService;
import org.apache.click.service.FileUploadService;
import org.apache.click.service.LogService;
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
		// TODO Auto-generated method stub
		return null;
	}

	public String getApplicationMode() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getCharset() {
		// TODO Auto-generated method stub
		return null;
	}

	public Class getErrorPageClass() {
		// TODO Auto-generated method stub
		return null;
	}

	public FileUploadService getFileUploadService() {
		// TODO Auto-generated method stub
		return null;
	}

	public Locale getLocale() {
		// TODO Auto-generated method stub
		return null;
	}

	public LogService getLogService() {
		return new ConsoleLogService();
	}

	public Class getNotFoundPageClass() {
		// TODO Auto-generated method stub
		return null;
	}

	public Class getPageClass(String path) {
		// TODO Auto-generated method stub
		return null;
	}

	public Field getPageField(Class pageClass, String fieldName) {
		// TODO Auto-generated method stub
		return null;
	}

	public Field[] getPageFieldArray(Class pageClass) {
		// TODO Auto-generated method stub
		return null;
	}

	public Map getPageFields(Class pageClass) {
		// TODO Auto-generated method stub
		return null;
	}

	public Map getPageHeaders(String path) {
		// TODO Auto-generated method stub
		return null;
	}

	public String getPagePath(Class pageClass) {
		return this.pagePathMap.get(pageClass);
	}

	public ServletContext getServletContext() {
		// TODO Auto-generated method stub
		return null;
	}

	public void setTemplateService(TemplateService templateService){
		this.templateService = templateService;
	}

	public TemplateService getTemplateService() {
		return templateService;
	}

	public boolean isJspPage(String path) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean isPagesAutoBinding() {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean isProductionMode() {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean isProfileMode() {
		// TODO Auto-generated method stub
		return false;
	}

	public void onDestroy() {
		// TODO Auto-generated method stub

	}

	public void onInit(ServletContext servletContext) throws Exception {
		// TODO Auto-generated method stub

	}

	public AutoBinding getAutoBindingMode() {
		// TODO Auto-generated method stub
		return null;
	}

	public List getPageClassList() {
		// TODO Auto-generated method stub
		return null;
	}

	public ResourceService getResourceService() {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean isTemplate(String path) {
		// TODO Auto-generated method stub
		return false;
	}

}
