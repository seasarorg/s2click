package org.seasar.s2click;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.servlet.ServletContext;

import org.apache.click.service.ConfigService;
import org.apache.click.service.ConsoleLogService;
import org.apache.click.service.FileUploadService;
import org.apache.click.service.LogService;
import org.apache.click.service.TemplateService;
import org.apache.click.util.Format;

@SuppressWarnings("unchecked")
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

}
