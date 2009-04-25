package org.seasar.s2click.test;

import java.io.Writer;
import java.util.Map;

import javax.servlet.ServletContext;

import org.apache.click.Page;
import org.apache.click.service.TemplateService;

@SuppressWarnings("unchecked")
public class MockTemplateService implements TemplateService {
	
	private Page page;
	private String templatePath;
	private Map model;
	
	public void onDestroy() {
	}

	public void onInit(ServletContext servletContext) throws Exception {
	}

	public void renderTemplate(Page page, Map model, Writer writer) throws Exception {
		this.page = page;
		this.model = model;
	}

	public void renderTemplate(String templatePath, Map model, Writer writer) throws Exception {
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
