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

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

import javax.servlet.ServletContext;

import org.apache.click.Control;
import org.apache.click.Page;
import org.apache.click.PageInterceptor;
import org.apache.click.service.CommonsFileUploadService;
import org.apache.click.service.ConfigService;
import org.apache.click.service.ConsoleLogService;
import org.apache.click.service.FileUploadService;
import org.apache.click.service.LogService;
import org.apache.click.service.MessagesMapService;
import org.apache.click.service.ResourceService;
import org.apache.click.service.TemplateService;
import org.apache.click.util.ClickUtils;
import org.apache.click.util.ErrorPage;
import org.apache.click.util.Format;
import org.apache.commons.lang.StringUtils;
import org.seasar.framework.container.SingletonS2Container;
import org.seasar.framework.container.factory.SingletonS2ContainerFactory;
import org.seasar.framework.container.util.SmartDeployUtil;
import org.seasar.framework.convention.NamingConvention;
import org.seasar.framework.convention.impl.NamingConventionImpl;
import org.seasar.s2click.annotation.Path;
import org.seasar.s2click.annotation.UrlPattern;
import org.seasar.s2click.filter.UrlPatternManager;
import org.seasar.s2click.util.S2ClickUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

@SuppressWarnings({"unchecked", "rawtypes"})
public class S2ClickConfigService implements ConfigService {

    private static final Object PAGE_LOAD_LOCK = new Object();

    /** The production application mode. */
    static final int PRODUCTION = 0;

    /** The profile application mode. */
    static final int PROFILE = 1;

    /** The development application mode. */
    static final int DEVELOPMENT = 2;

    /** The debug application mode. */
    static final int DEBUG = 3;

    /** The trace application mode. */
    static final int TRACE = 4;

    static final String[] MODE_VALUES =
        { "production", "profile", "development", "debug", "trace" };

//    /** The automatically bind controls, request parameters and models flag. */
//    private AutoBinding autobinding;

    /** The Commons FileUpload service class. */
    private FileUploadService fileUploadService;

    /** The format class. */
    private Class formatClass;

    /** The charcter encoding of this application. */
    private String charset;

    /** The default application locale.*/
    private Locale locale;

    /** The application log service. */
    private LogService logService;

    /**
     * The application mode:
     * [ PRODUCTION | PROFILE | DEVELOPMENT | DEBUG | TRACE ].
     */
    private int mode;

    /** The ServletContext instance. */
    private ServletContext servletContext;

    /** The application ResourceService. */
    private ResourceService resourceService;

    /** The application TemplateService. */
    private TemplateService templateService;

    /** The application MessagesMapService. */
    private MessagesMapService messagesMapService;

	protected boolean hotDeploy;

	protected Map<String, Object> commonHeaders;

	protected List<String> pageInterceptorList;

    protected final Map pageByPathMap = new HashMap();
    protected final Map pageByClassMap = new HashMap();
    protected String pagesPackage;

    static final Map<String, Object> DEFAULT_HEADERS;
    /** Initialize the default headers. */
    static {
        DEFAULT_HEADERS = new HashMap();
        DEFAULT_HEADERS.put("Pragma", "no-cache");
        DEFAULT_HEADERS.put("Cache-Control", "no-store, no-cache, must-revalidate, post-check=0, pre-check=0");
        DEFAULT_HEADERS.put("Expires", new Date(1L));
    }

	public Format createFormat() {
		try {
			return (Format) formatClass.newInstance();

		} catch(Exception ex){
			throw new RuntimeException(ex);
		}
	}

	public String getApplicationMode() {
        return MODE_VALUES[mode];
	}

	public String getCharset() {
		return charset;
	}

	public Class getErrorPageClass() {
        PageElm page = (PageElm) pageByPathMap.get(ERROR_PATH);

        if (page != null) {
            return page.getPageClass();

        } else {
            return ErrorPage.class;
        }
	}

	public FileUploadService getFileUploadService() {
		return fileUploadService;
	}

	public Locale getLocale() {
		return locale;
	}

	public LogService getLogService() {
		return logService;
	}

	public Class getNotFoundPageClass() {
        PageElm page = (PageElm) pageByPathMap.get(NOT_FOUND_PATH);

        if (page != null) {
            return page.getPageClass();

        } else {
            return Page.class;
        }
	}

    public Class getPageClass(String path) {

        // If in production or profile mode.
        if (mode <= PROFILE) {
            PageElm page = (PageElm) pageByPathMap.get(path);
            if (page == null) {
                String jspPath = StringUtils.replace(path, ".htm", ".jsp");
                page = (PageElm) pageByPathMap.get(jspPath);
            }

            if (page != null) {
                return page.getPageClass();
            } else {
                return null;
            }

        // Else in development, debug or trace mode
        } else {

            synchronized (PAGE_LOAD_LOCK) {
                PageElm page = (PageElm) pageByPathMap.get(path);
                if (page == null) {
                    String jspPath = StringUtils.replace(path, ".htm", ".jsp");
                    page = (PageElm) pageByPathMap.get(jspPath);
                }

                if (page != null) {
                    return page.getPageClass();
                }

                try {
                    URL resource = servletContext.getResource(path);
                    if (resource != null) {
                    		String pageClassName = getPageClassName(path, pagesPackage);
						if (pageClassName != null) {
							page = new PageElm(path, pageClassName, commonHeaders, hotDeploy);

							pageByPathMap.put(page.getPath(), page);

							if (logService.isDebugEnabled()) {
								String msg = path + " -> " + pageClassName;
								logService.debug(msg);
							}
							return loadClass(pageClassName);
						}
                    }
                } catch (Exception e) {
                    // ignore
                }
                return null;
            }
        }
    }

	public Field getPageField(Class<? extends Page> pageClass, String fieldName){
        return (Field) getPageFields(pageClass).get(fieldName);
	}

    public Field[] getPageFieldArray(Class<? extends Page> pageClass){
    	Object object = pageByClassMap.get(getRawClassname(pageClass));

        if (object instanceof PageElm) {
            PageElm page = (PageElm) object;
            return page.getFieldArray();

        } else if (object instanceof List) {
            List list = (List) object;
            PageElm page = (PageElm) list.get(0);
            return page.getFieldArray();

        } else {
            return null;
        }
	}

    public Map<String, Field> getPageFields(Class<? extends Page> pageClass){
        Object object = pageByClassMap.get(getRawClassname(pageClass));

        if (object instanceof PageElm) {
            PageElm page = (PageElm) object;
            return page.getFields();

        } else if (object instanceof List) {
            List list = (List) object;
            PageElm page = (PageElm) list.get(0);
            return page.getFields();

        } else {
            return Collections.EMPTY_MAP;
        }
    }

	public Map<String, Object> getPageHeaders(String path) {
        PageElm page = (PageElm) pageByPathMap.get(path);
        if (page == null) {
            String jspPath = StringUtils.replace(path, ".htm", ".jsp");
            page = (PageElm) pageByPathMap.get(jspPath);
        }

        if (page != null) {
            return page.getHeaders();
        } else {
            return null;
        }
	}

	public String getPagePath(Class<? extends Page> pageClass) {
        Object object = pageByClassMap.get(pageClass.getName());

        if (object instanceof PageElm) {
            PageElm page = (PageElm) object;
            return page.getPath();

        } else if (object instanceof List) {
            String msg =
                "Page class resolves to multiple paths: " + pageClass.getName();
            throw new IllegalArgumentException(msg);

        } else {
            return null;
        }
	}

	public ServletContext getServletContext() {
		return servletContext;
	}

	public TemplateService getTemplateService() {
		return templateService;
	}

	public boolean isJspPage(String path) {
        String jspPath = StringUtils.replace(path, ".htm", ".jsp");
        return pageByPathMap.containsKey(jspPath);
	}

	public boolean isPagesAutoBinding() {
		return true;
	}

    public boolean isProductionMode() {
        return (mode == PRODUCTION);
    }

    public boolean isProfileMode() {
        return (mode == PROFILE);
    }

	public void onDestroy() {
	}

	public void onInit(ServletContext servletContext) throws Exception {
		this.servletContext = servletContext;

        this.hotDeploy = !SmartDeployUtil.isHotdeployMode(SingletonS2ContainerFactory.getContainer());
		S2ClickConfig config = S2ClickUtils.getComponent(S2ClickConfig.class);

		loadLogService(config);
		loadModel(config);

		deployFiles();

		loadCharset(config);
		loadLocale(config);
		loadResourceService(config);
		loadTemplateService(config);
		loadFileUploadService(config);
		loadFormat(config);
		loadHeaders(config);
		loadPages(config);
		loadMessageMapService(config);
		loadPageInterceptors(config);
	}

    private void deployFiles() throws Exception {

        // Deploy application files if they are not already present.
        // Only deploy if servletContext.getRealPath() returns a valid path.
        boolean resourcesDeployable = (servletContext.getRealPath("/") != null);

        if (resourcesDeployable) {
            String[] resources = {
                "/META-INF/resources/error.htm",
                "/META-INF/resources/click/not-found.htm",
                "/META-INF/resources/click/VM_global_library.vm"
            };

            ClickUtils.deployFiles(servletContext, resources, "click");

            deployControls(getResourceRootElement("/click-controls.xml"));
            deployControls(getResourceRootElement("/extras-controls.xml"));
//            deployControls(rootElm);
//            deployControlSets(rootElm);

//            deployFilesInJars();
        } else {
            String msg = "Could not auto deploy files to 'click' web folder."
                + " You may need to manually include click resources in your"
                + " web application.";
            getLogService().warn(msg);
        }
    }

    private Element getResourceRootElement(String path) throws IOException {
        Document document = null;
        InputStream inputStream = null;
        try {
            inputStream = ClickUtils.getResourceAsStream(path, getClass());

            if (inputStream != null) {
                document = ClickUtils.buildDocument(inputStream);
            }

        } finally {
            ClickUtils.close(inputStream);
        }

        if (document != null) {
            return document.getDocumentElement();

        } else {
            return null;
        }
    }

    private void deployControls(Element rootElm) throws Exception {

        if (rootElm == null) {
            return;
        }

        Element controlsElm = ClickUtils.getChild(rootElm, "controls");

        if (controlsElm == null) {
            return;
        }

        List deployableList = ClickUtils.getChildren(controlsElm, "control");

        for (int i = 0; i < deployableList.size(); i++) {
            Element deployableElm = (Element) deployableList.get(i);

            String classname = deployableElm.getAttribute("classname");
            if (StringUtils.isBlank(classname)) {
                String msg =
                    "'control' element missing 'classname' attribute.";
                throw new RuntimeException(msg);
            }

            Class deployClass = ClickUtils.classForName(classname);
            Control control = (Control) deployClass.newInstance();

            control.onDeploy(servletContext);
        }
    }

	protected void loadCharset(S2ClickConfig config){
		charset = config.charset;
	}

	protected void loadModel(S2ClickConfig config){
        String modeValue = "development";

        if (StringUtils.isNotEmpty(config.mode)) {
        	modeValue = config.mode;
        }

        modeValue = System.getProperty("click.mode", modeValue);

        if (modeValue.equalsIgnoreCase("production")) {
            mode = PRODUCTION;
        } else if (modeValue.equalsIgnoreCase("profile")) {
            mode = PROFILE;
        } else if (modeValue.equalsIgnoreCase("development")) {
            mode = DEVELOPMENT;
        } else if (modeValue.equalsIgnoreCase("debug")) {
            mode = DEBUG;
        } else if (modeValue.equalsIgnoreCase("trace")) {
            mode = TRACE;
        } else {
            logService.error("invalid application mode: " + modeValue);
            mode = DEBUG;
        }

        // Set log levels
        if (logService instanceof ConsoleLogService) {
            int logLevel = ConsoleLogService.INFO_LEVEL;

            if (mode == PRODUCTION) {
                logLevel = ConsoleLogService.WARN_LEVEL;

            } else if (mode == DEVELOPMENT) {

            } else if (mode == DEBUG) {
                logLevel = ConsoleLogService.DEBUG_LEVEL;

            } else if (mode == TRACE) {
                logLevel = ConsoleLogService.TRACE_LEVEL;
            }

            ((ConsoleLogService) logService).setLevel(logLevel);
        }
	}

	protected void loadLogService(S2ClickConfig config) throws Exception {
		logService = config.logService;
		logService.onInit(servletContext);
	}

	protected void loadResourceService(S2ClickConfig config) throws Exception {
		resourceService = config.resourceService;
		resourceService.onInit(servletContext);
	}

	protected void loadTemplateService(S2ClickConfig config) throws Exception {
		templateService = config.templateService;
		templateService.onInit(servletContext);
	}

	protected void loadFileUploadService(S2ClickConfig config) throws Exception {
		fileUploadService = new CommonsFileUploadService();
		fileUploadService.onInit(servletContext);
	}

	protected void loadMessageMapService(S2ClickConfig config) throws Exception {
		this.messagesMapService = config.messagesMapService;
		this.messagesMapService.onInit(servletContext);
	}

	protected void loadPageInterceptors(S2ClickConfig config){
		this.pageInterceptorList = config.pageInterceptorList;
	}

	protected void loadLocale(S2ClickConfig config){
        String value = config.locale;
        if (value != null && value.length() > 0) {
            StringTokenizer tokenizer = new StringTokenizer(value, "_");
            if (tokenizer.countTokens() == 1) {
                String language = tokenizer.nextToken();
                locale = new Locale(language);
            } else if (tokenizer.countTokens() == 2) {
                String language = tokenizer.nextToken();
                String country = tokenizer.nextToken();
                locale = new Locale(language, country);
            }
        }
	}

    protected void loadHeaders(S2ClickConfig config) {
        if (config.headers != null || !config.headers.isEmpty()) {
            commonHeaders = Collections.unmodifiableMap(config.headers);
        } else {
            commonHeaders = Collections.unmodifiableMap(DEFAULT_HEADERS);
        }
    }

	protected void loadFormat(S2ClickConfig config){
		formatClass = config.formatClass;
	}

    protected void loadPages(S2ClickConfig config) throws ClassNotFoundException {

    	// URLパターンの情報をクリア
    	UrlPatternManager.getAll().clear();

    	NamingConvention naming = (NamingConvention)
    		SingletonS2ContainerFactory.getContainer().getComponent(NamingConventionImpl.class);
        pagesPackage = naming.getRootPackageNames()[0] + "." + naming.getPageSuffix().toLowerCase();

        // Build list of automap path page class overrides
        if (logService.isDebugEnabled()) {
        	logService.debug("automapped pages:");
        }

        // @Pathアノテーションを処理
        List<String> classes = new PageClassLoader(pagesPackage).getPageClasses();
        for(String className: classes){
        	Class<?> clazz = loadClass(className);
        	Path path = clazz.getAnnotation(Path.class);
        	if(path != null){
        		String value = path.value();
        		if(StringUtils.isNotEmpty(value)){
        			if(!value.startsWith("/")){
        				value = "/" + value;
        			}
                    PageElm page = new PageElm(value, className, commonHeaders, hotDeploy);

                    pageByPathMap.put(value, page);

                    if (logService.isDebugEnabled()) {
                        String msg = value + " -> " + className;
                        logService.debug(msg);
                    }
        		}
        	}
        }

        List<String> templates = getTemplateFiles();

        for (int i = 0; i < templates.size(); i++) {
            String pagePath = templates.get(i);

            if (!pageByPathMap.containsKey(pagePath)) {

                String pageClassName = getPageClassName(pagePath, pagesPackage);

                if (pageClassName != null) {
                    PageElm page = new PageElm(pagePath, pageClassName, commonHeaders, hotDeploy);

                    pageByPathMap.put(page.getPath(), page);

                    if (logService.isDebugEnabled()) {
                        String msg = pagePath + " -> " + pageClassName;
                        logService.debug(msg);
                    }
                }
            }
        }

        // @UrlPatternアノテーションを処理
        for(Map.Entry entry: (Set<Map.Entry>) pageByPathMap.entrySet()){
        	PageElm pageElm = (PageElm) entry.getValue();
        	Class<?> clazz = loadClass(pageElm.pageClassName);
        	UrlPattern urlPattern = clazz.getAnnotation(UrlPattern.class);
        	if(urlPattern != null){
        		String pattern = urlPattern.value();
        		if(StringUtils.isNotEmpty(pattern)){
        			UrlPatternManager.add(pattern, (String) entry.getKey());
                    if (logService.isDebugEnabled()) {
                        String msg = pattern + " -> " + entry.getKey();
                        logService.debug(msg);
                    }
        		}
        	}
        }

        // Build pages by class map
        for (Iterator<PageElm> i = pageByPathMap.values().iterator(); i.hasNext();) {
            PageElm page = (PageElm) i.next();
            Object value = pageByClassMap.get(page.pageClassName);

            if (value == null) {
                pageByClassMap.put(page.pageClassName, page);

            } else if (value instanceof List) {
                ((List) value).add(value);

            } else if (value instanceof PageElm) {
                List list = new ArrayList();
                list.add(value);
                list.add(page);
                pageByClassMap.put(page.pageClassName, list);

            } else {
                // should never occur
                throw new IllegalStateException();
            }
        }
    }

    /**
     * Return the list of templates within the web application.
     *
     * @return list of all templates within the web application
     */
    private List<String> getTemplateFiles() {
        List<String> fileList = new ArrayList<String>();

        Set<?> resources = servletContext.getResourcePaths("/");

        // Add all resources withtin web application
        for (Iterator<?> i = resources.iterator(); i.hasNext();) {
            String resource = (String) i.next();

            if (resource.endsWith(".htm") || resource.endsWith(".jsp")) {
                fileList.add(resource);

            } else if (resource.endsWith("/")) {
                if (!resource.equalsIgnoreCase("/WEB-INF/")) {
                    processDirectory(resource, fileList);
                }
            }
        }

        Collections.sort(fileList);

        return fileList;
    }

    private void processDirectory(String dirPath, List<String> fileList) {
        Set<?> resources = servletContext.getResourcePaths(dirPath);

        if (resources != null) {
            for (Iterator<?> i = resources.iterator(); i.hasNext();) {
                String resource = (String) i.next();

                if (resource.endsWith(".htm") || resource.endsWith(".jsp")) {
                    fileList.add(resource);

                } else if (resource.endsWith("/")) {
                    processDirectory(resource, fileList);
                }
            }
        }
    }

    private String getPageClassName(String pagePath, String pagesPackage) {
        String packageName = pagesPackage + ".";
		String className = "";

		// Strip off .htm extension
		String path = pagePath.substring(0, pagePath.lastIndexOf("."));

		if (path.indexOf("/") != -1) {
			StringTokenizer tokenizer = new StringTokenizer(path, "/");
			while (tokenizer.hasMoreTokens()) {
				String token = tokenizer.nextToken();
				if (tokenizer.hasMoreTokens()) {
					packageName = packageName + token + ".";
				} else {
					className = token;
				}
			}
		} else {
			className = path;
		}

		StringTokenizer tokenizer = new StringTokenizer(className, "_-");
		className = "";
		while (tokenizer.hasMoreTokens()) {
			String token = tokenizer.nextToken();
			token = Character.toUpperCase(token.charAt(0)) + token.substring(1);
			className += token;
		}

		className = packageName + className;
		// if(!className.endsWith("Page")){
		// className = className + "Page";
		// }
		// return className;

		Class pageClass = loadClass(className);
		if (pageClass != null) {
			if (!Page.class.isAssignableFrom(pageClass)) {
				String msg = "Automapped page class " + className
						+ " is not a subclass of net.sf.click.Page";
				throw new RuntimeException(msg);
			}
		} else {

			boolean classFound = false;

			if (!className.endsWith("Page")) {
				className = className + "Page";
				pageClass = loadClass(className);
				if (pageClass != null) {
					if (!Page.class.isAssignableFrom(pageClass)) {
						String msg = "Automapped page class " + className
								+ " is not a subclass of net.sf.click.Page";
						throw new RuntimeException(msg);
					}

					classFound = true;
				}
			}

			if (!classFound) {
				if (logService.isDebugEnabled()) {
					logService.debug(pagePath + " -> CLASS NOT FOUND");
				}
				if (logService.isTraceEnabled()) {
					logService.trace("class not found: " + className);
				}
				return null;
			}
		}

		// abstractクラスは登録しない
    	if(Modifier.isAbstract(pageClass.getModifiers())){
    		return null;
    	}

        return className;
    }

    private String getRawClassname(Class clazz){
    	String className = clazz.getName();
    	int index = className.indexOf("$$EnhancedByS2AOP$$");
    	if(index >= 0){
    		className = className.substring(0, index);
    	}
    	return className;
    }

    private static Class<? extends Page> loadClass(String className){
    	try {
    		return (Class<? extends Page>) Thread.currentThread().getContextClassLoader().loadClass(className);
		} catch (SecurityException e) {
			return null;
		} catch(ClassNotFoundException e){
			return null;
		}
    }


    static class PageElm {

        private final Map<String, Object> headers;
        private final String path;
        private final String pageClassName;
        private final Class<? extends Page> pageClass;
        private final Map<String, Field> fields;
        private final Field[] fieldArray;

        private PageElm(String path, String pageClassName, Map<String, Object> commonHeaders, boolean hotDeploy) {
            headers = Collections.unmodifiableMap(commonHeaders);
            this.path = path;
            this.pageClassName = pageClassName;

            if(!hotDeploy){
            	pageClass = loadClass(pageClassName);
                fieldArray = pageClass.getFields();

                fields = new HashMap();
                for (int i = 0; i < fieldArray.length; i++) {
                    Field field = fieldArray[i];
                    fields.put(field.getName(), field);
                }
            } else {
            	pageClass = null;
            	fields = null;
            	fieldArray = null;
            }
        }

        public Field[] getFieldArray() {
        	if(fieldArray != null){
        		return fieldArray;
        	}
        	try {
				return loadClass(pageClassName).getFields();
			} catch (SecurityException e) {
				throw new RuntimeException(e);
			}
        }

        public Map<String, Field> getFields() {
        	if(fields != null){
        		return fields;
        	}
			Map fields = new HashMap();
			for (Field field: getFieldArray()) {
				fields.put(field.getName(), field);
			}
			return fields;
        }

        public Map<String, Object> getHeaders() {
            return headers;
        }

        public Class<? extends Page> getPageClass() {
        	if(pageClass != null){
        		return pageClass;
        	}
        	return loadClass(this.pageClassName);
        }

        public String getPath() {
            return path;
        }
    }

    /**
     * リクエストパラメータのバインディングはS2Click側で行うため、
     * このメソッドは常に<code>AutoBinding.NONE</code>を返します。
     */
	public AutoBinding getAutoBindingMode() {
		return AutoBinding.DEFAULT;
	}

	public List<Class<? extends Page>> getPageClassList() {
		List<Class<? extends Page>> classList
			= new ArrayList<Class<? extends Page>>(pageByClassMap.size());

		Iterator i = pageByClassMap.keySet().iterator();
		while (i.hasNext()) {
			Class<? extends Page> pageClass = (Class<? extends Page>) i.next();
			classList.add(pageClass);
		}

		return classList;
	}

	public ResourceService getResourceService() {
		return this.resourceService;
	}

	public boolean isTemplate(String path) {
        if (path.endsWith(".htm") || path.endsWith(".jsp")) {
            return true;
        }
        return false;
	}

	public MessagesMapService getMessagesMapService() {
		return messagesMapService;
	}

	public List<PageInterceptor> getPageInterceptors() {
		List<PageInterceptor> pageInterceptors = new ArrayList<PageInterceptor>();
		for(String componentName: this.pageInterceptorList){
			PageInterceptor pageInterceptor = SingletonS2Container.getComponent(componentName);
			pageInterceptors.add(pageInterceptor);
		}
		return pageInterceptors;
	}

}
