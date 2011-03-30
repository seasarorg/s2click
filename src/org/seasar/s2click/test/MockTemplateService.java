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

import java.io.IOException;
import java.io.Writer;
import java.util.Map;

import javax.servlet.ServletContext;

import org.apache.click.Page;
import org.apache.click.service.TemplateException;
import org.apache.click.service.TemplateService;

@SuppressWarnings("rawtypes")
public class MockTemplateService implements TemplateService {

	private Page page;
	private String templatePath;
	private Map model;

	public void onDestroy() {
	}

	public void onInit(ServletContext servletContext) throws Exception {
	}

	public void renderTemplate(Page page, Map model, Writer writer)
			throws IOException, TemplateException {
		this.page = page;
		this.model = model;
	}

	public void renderTemplate(String templatePath, Map model, Writer writer)
			throws IOException, TemplateException {
		this.templatePath = templatePath;
		this.model = model;
	}

	public Page getPage(){
		return this.page;
	}

	public String getTemplatePath(){
		return this.templatePath;
	}

	public Map getModel(){
		return this.model;
	}

}
