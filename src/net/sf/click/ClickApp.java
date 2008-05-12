package net.sf.click;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.TreeMap;

import javax.servlet.ServletContext;

import net.sf.click.util.ClickLogger;
import net.sf.click.util.ClickUtils;
import net.sf.click.util.Format;

import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.lang.StringUtils;
import org.apache.velocity.Template;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;
import org.apache.velocity.tools.view.servlet.WebappLoader;
import org.seasar.framework.container.factory.SingletonS2ContainerFactory;
import org.seasar.framework.convention.NamingConvention;
import org.seasar.framework.convention.impl.NamingConventionImpl;
import org.seasar.s2click.S2ClickConfig;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * Seasar2のHOT deployに対応するために改編されたClickAppクラス。
 * <p>
 * TODO WARM deploy、COOL deploy時の性能改善（フィールドリフレクションのキャッシュ）
 *
 * @author Malcolm Edgar
 * @author Naoki Takezoe
 */
@SuppressWarnings("unchecked")
class ClickApp implements EntityResolver {

    /**
     * The Click configuration filename: &nbsp;
     * "<tt>/WEB-INF/click.xml</tt>".
     */
    static final String DEFAULT_APP_CONFIG = "/WEB-INF/click.xml";

    /** The name of the Click logger: &nbsp; "<tt>net.sf.click</tt>". */
    static final String CLICK_LOGGER = "net.sf.click";

    /** The click deployment directory path: &nbsp; "/click". */
    static final String CLICK_PATH = "/click";

    /** The default common page headers. */
    static final Map DEFAULT_HEADERS;

    /**
     * The default velocity properties filename: &nbsp;
     * "<tt>/WEB-INF/velocity.properties</tt>".
     */
    static final String DEFAULT_VEL_PROPS = "/WEB-INF/velocity.properties";

    /** The click DTD file name: &nbsp; "<tt>click.dtd</tt>". */
    static final String DTD_FILE_NAME = "click.dtd";

    /**
     * The resource path of the click DTD file: &nbsp;
     * "<tt>/net/sf/click/click.dtd</tt>".
     */
    static final String DTD_FILE_PATH = "/net/sf/click/" + DTD_FILE_NAME;

    /** The error page file name: &nbsp; "<tt>error.html</tt>". */
    static final String ERROR_FILE_NAME = "error.htm";

    static final String ERROR_PATH = CLICK_PATH + "/" + ERROR_FILE_NAME;

    /** The page not found file name: &nbsp; "<tt>not-found.html</tt>". */
    static final String NOT_FOUND_FILE_NAME = "not-found.htm";

    static final String NOT_FOUND_PATH = CLICK_PATH + "/" + NOT_FOUND_FILE_NAME;

    /**
     * The user supplied macro file name: &nbsp; "<tt>macro.vm</tt>".
     */
    static final String MACRO_VM_FILE_NAME = "macro.vm";

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

    private static final Object PAGE_LOAD_LOCK = new Object();

    /**
     * The name of the Velocity logger: &nbsp; "<tt>org.apache.velocity</tt>".
     */
    static final String VELOCITY_LOGGER = "org.apache.velocity";

    /**
     * The global Velocity macro file name: &nbsp;
     * "<tt>VM_global_library.vm</tt>".
     */
    static final String VM_FILE_NAME = "VM_global_library.vm";

    /** Initialize the default headers. */
    static {
        DEFAULT_HEADERS = new HashMap();
        DEFAULT_HEADERS.put("Pragma", "no-cache");
        DEFAULT_HEADERS.put("Cache-Control", "no-store, no-cache, must-revalidate, post-check=0, pre-check=0");
        DEFAULT_HEADERS.put("Expires", new Date(1L));
    }

    // -------------------------------------------------------- Private Members

    /** The automatically bind controls, request parameters and models flag. */
    private boolean autobinding = true;

    /** The Commons Upload FileItem Factory. */
    private FileItemFactory fileItemFactory;

    /** The format class. */
    private Class formatClass;

    /** The charcter encoding of this application. */
    private String charset;

    /** The Map of global page headers. */
    private Map commonHeaders;

    /** The default application locale.*/
    private Locale locale;

    /** The application logger. */
    private ClickLogger logger;

    /**
     * The application mode:
     * [ PRODUCTION | PROFILE | DEVELOPMENT | DEBUG | TRACE ].
     */
    private int mode;

    /** The map of ClickApp.PageElm keyed on path. */
    private final Map pageByPathMap = new HashMap();

    /** The map of ClickApp.PageElm keyed on class. */
    private final Map pageByClassMap = new HashMap();

    /** The pages package prefix. */
    private String pagesPackage;

    /** The ServletContext instance. */
    private ServletContext servletContext;

    /** The VelocityEngine instance. */
    private final VelocityEngine velocityEngine = new VelocityEngine();

    // --------------------------------------------------------- Public Methods

    /**
     * Return the Click Application servlet context.
     *
     * @return the application servlet context
     */
    public ServletContext getServletContext() {
        return servletContext;
    }

    /**
     * Set the Click Application servlet context.
     *
     * @param servletContext the application servlet context
     */
    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
    }

    /**
     * Initialize the click application.
     *
     * @param clickLogger the Click runtime logger instance
     * @throws Exception if an error occurs initializing the application
     */
    public void init(ClickLogger clickLogger) throws Exception {

        if (clickLogger == null) {
            throw new IllegalArgumentException("Null clickLogger parameter");
        }

        if (getServletContext() == null) {
            throw new IllegalStateException("servlet context not defined");
        }

        logger = clickLogger;

        ClickLogger.setInstance(logger);

    	S2ClickConfig config = (S2ClickConfig) 
    		SingletonS2ContainerFactory.getContainer().getComponent(S2ClickConfig.class);
    	
        // Load the application mode and set the logger levels
        loadMode(config);

        // Load the format class
        loadFormatClass(config);

        // Load the Commons Upload file item factory
        loadFileItemFactory(config);

        // Load the common headers
        loadHeaders(config);

        // Load the pages
        loadPages(config);

        // Load the error and not-found pages
        loadDefaultPages();

        // Load the charset
        loadCharset(config);

        // Load the locale
        loadLocale(config);

        // Deploy the application files if not present
        deployFiles(config);

        // Set ServletContext instance for WebappLoader
        String className = ServletContext.class.getName();
        velocityEngine.setApplicationAttribute(className, servletContext);

        // Load velocity properties
        Properties properties = getVelocityProperties(servletContext);

        // Initialize VelocityEngine
        velocityEngine.init(properties);

        // Turn down the Velocity logging level
        if (mode == DEBUG || mode == TRACE) {
            ClickLogger velocityLogger = ClickLogger.getInstance(velocityEngine);
            velocityLogger.setLevel(ClickLogger.WARN_ID);
        }

        // Cache page templates.
        loadTemplates();
    }

    // --------------------------------------------------------- Public Methods

    /**
     * This method resolves the click.dtd for the XML parser using the
     * classpath resource: <tt>/net/sf/click/click.dtd</tt>.
     *
     * @see EntityResolver#resolveEntity(String, String)
     */
    public InputSource resolveEntity(String publicId, String systemId)
            throws SAXException, IOException {

        InputStream inputStream = ClickUtils.getResourceAsStream(DTD_FILE_PATH, getClass());

        if (inputStream != null) {
            return new InputSource(inputStream);
        } else {
            throw new IOException("could not load resource: " + DTD_FILE_PATH);
        }
    }

    // -------------------------------------------------------- Package Methods

    /**
     * Return the application character encoding.
     *
     * @return the application character encoding
     */
    String getCharset() {
        return charset;
    }

    /**
     * Return the application FileItemFactory.
     *
     * @return the application FileItemFactory
     */
    FileItemFactory getFileItemFactory() {
        return fileItemFactory;
    }

    /**
     * Return a new format object.
     *
     * @return a new format object
     */
    Format getFormat() {
    	try {
    		return (Format) formatClass.newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Return the application locale.
     *
     * @return the application locale
     */
    Locale getLocale() {
        return locale;
    }

    /**
     * Return the application logger.
     *
     * @return the application logger.
     */
    ClickLogger getLogger() {
        return logger;
    }

    /**
     * Return true if auto binding is enabled. Autobinding will automatically
     * bind any request parameters to public fields, add any public controls to
     * the page and add public fields to the page model.
     *
     * @return true if request parameters should be automatically bound to public
     * page fields
     */
    boolean isPagesAutoBinding() {
        return autobinding;
    }

    /**
     * Return true if the application is in PRODUCTION mode.
     *
     * @return true if the application is in PRODUCTION mode
     */
    boolean isProductionMode() {
        return (mode == PRODUCTION);
    }

    /**
     * Return the application mode String value: &nbsp; <tt>["production",
     * "profile", "development", "debug"]</tt>.
     *
     * @return the application mode String value
     */
    String getModeValue() {
        return MODE_VALUES[mode];
    }

    /**
     * Return true if JSP exists for the given ".htm" path.
     *
     * @param path the Page ".htm" path
     * @return true if JSP exists for the given ".htm" path
     */
    boolean isJspPage(String path) {
        String jspPath = StringUtils.replace(path, ".htm", ".jsp");
        return pageByPathMap.containsKey(jspPath);
    }

    /**
     * Return the page <tt>Class</tt> for the given path.
     *
     * @param path the page path
     * @return the page class
     */
    Class getPageClass(String path) {

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
							page = new PageElm(path, pageClassName, commonHeaders);

							pageByPathMap.put(page.getPath(), page);

							if (logger.isDebugEnabled()) {
								String msg = path + " -> " + pageClassName;
								logger.debug(msg);
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

    /**
     * Return the page path for the given page <tt>Class</tt>.
     *
     * @param pageClass the page class
     * @return path the page path
     */
    String getPagePath(Class pageClass) {
        Object object = pageByClassMap.get(pageClass.getName());

        if (object instanceof ClickApp.PageElm) {
            ClickApp.PageElm page = (ClickApp.PageElm) object;
            return page.getPath();

        } else if (object instanceof List) {
            String msg =
                "Page class resolves to multiple paths: " + pageClass.getName();
            throw new IllegalArgumentException(msg);

        } else {
            return null;
        }
    }

    /**
     * Return the headers of the page for the given path.
     *
     * @param path the path of the page
     * @return a Map of headers for the given page path
     */
    Map getPageHeaders(String path) {
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

    /**
     * Return the page not found <tt>Page</tt> <tt>Class</tt>.
     *
     * @return the page not found <tt>Page</tt> <tt>Class</tt>
     */
    Class getNotFoundPageClass() {
        PageElm page = (PageElm) pageByPathMap.get(NOT_FOUND_PATH);

        if (page != null) {
            return page.getPageClass();

        } else {
            return net.sf.click.Page.class;
        }
    }

    /**
     * Return the error handling page <tt>Page</tt> <tt>Class</tt>.
     *
     * @return the error handling page <tt>Page</tt> <tt>Class</tt>
     */
    Class getErrorPageClass() {
        PageElm page = (PageElm) pageByPathMap.get(ERROR_PATH);

        if (page != null) {
            return page.getPageClass();

        } else {
            return net.sf.click.util.ErrorPage.class;
        }
    }

    /**
     * Return the Velocity Template for the give page path.
     *
     * @param path the Velocity template path
     * @return the Velocity Template for the give page path
     * @throws Exception if Velocity error occurs
     */
    Template getTemplate(String path) throws Exception {
        return velocityEngine.getTemplate(path);
    }

    /**
     * Return the Velocity Template for the give page path.
     *
     * @param path the Velocity template path
     * @param charset the template encoding charset
     * @return the Velocity Template for the give page path
     * @throws Exception if Velocity error occurs
     */
    Template getTemplate(String path, String charset) throws Exception {
        return velocityEngine.getTemplate(path, charset);
    }

    /**
     * Return the public field of the given name for the pageClass,
     * or null if not defined.
     *
     * @param pageClass the page class
     * @param fieldName the name of the field
     * @return the public field of the pageClass with the given name or null
     */
    Field getPageField(Class pageClass, String fieldName) {
        return (Field) getPageFields(pageClass).get(fieldName);
    }

    /**
     * Return an array public fields for the given page class.
     *
     * @param pageClass the page class
     * @return an array public fields for the given page class
     */
    Field[] getPageFieldArray(Class pageClass) {
    	Object object = pageByClassMap.get(getRawClassname(pageClass));

        if (object instanceof ClickApp.PageElm) {
            ClickApp.PageElm page = (ClickApp.PageElm) object;
            return page.getFieldArray();

        } else if (object instanceof List) {
            List list = (List) object;
            ClickApp.PageElm page = (ClickApp.PageElm) list.get(0);
            return page.getFieldArray();

        } else {
            return null;
        }
    }

    /**
     * Return Map of public fields for the given page class.
     *
     * @param pageClass the page class
     * @return a Map of public fields for the given page class
     */
    Map getPageFields(Class pageClass) {
        Object object = pageByClassMap.get(getRawClassname(pageClass));

        if (object instanceof ClickApp.PageElm) {
            ClickApp.PageElm page = (ClickApp.PageElm) object;
            return page.getFields();

        } else if (object instanceof List) {
            List list = (List) object;
            ClickApp.PageElm page = (ClickApp.PageElm) list.get(0);
            return page.getFields();

        } else {
            return Collections.EMPTY_MAP;
        }
    }

    // -------------------------------------------------------- Private Methods
    
    private String getRawClassname(Class clazz){
    	String className = clazz.getName();
    	int index = className.indexOf("$$EnhancedByS2AOP$$");
    	if(index >= 0){
    		className = className.substring(0, index);
    	}
    	return className;
    }
    
    private Element getResourceRootElement(String path) throws IOException {
        Document document = null;
        InputStream inputStream = null;
        try {
            inputStream = ClickUtils.getResourceAsStream(path, getClass());

            if (inputStream != null) {
                document = ClickUtils.buildDocument(inputStream, this);
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

    private void deployControls(Class[] deployClasses) throws Exception {
    	if(deployClasses != null){
    		for(Class clazz: deployClasses){
    			Control control = (Control) clazz.newInstance();
    			control.onDeploy(getServletContext());
    		}
    	}
    }

    private void deployControlSets(S2ClickConfig config) throws Exception {
        if (config.controlSets == null) {
            return;
        }
        for (String name: config.controlSets) {
            if (StringUtils.isBlank(name)) {
                String msg =
                        "'control-set' element missing 'name' attribute.";
                throw new RuntimeException(msg);
            }
            deployControls(getDeployClasses(getResourceRootElement("/" + name)));
        }
    }
    
    private Class[] getDeployClasses(Element rootElm){
      if (rootElm == null) {
			return null;
		}

		Element controlsElm = ClickUtils.getChild(rootElm, "controls");

		if (controlsElm == null) {
			return null;
		}

		List deployableList = getChildren(controlsElm, "control");
		List<Class> deployClasses = new ArrayList<Class>();

		for (int i = 0; i < deployableList.size(); i++) {
			Element deployableElm = (Element) deployableList.get(i);

			String classname = deployableElm.getAttribute("classname");
			if (StringUtils.isBlank(classname)) {
				String msg = "'control' element missing 'classname' attribute.";
				throw new RuntimeException(msg);
			}

			Class deployClass = loadClass(classname);
			if(deployClass != null){
				deployClasses.add(deployClass);
			}
		}
		
		return deployClasses.toArray(new Class[deployClasses.size()]);
    }

    private void deployFiles(S2ClickConfig config) throws Exception {

        ClickUtils.deployFile(servletContext,
                              "/net/sf/click/control/control.css",
                              "click");

        ClickUtils.deployFile(servletContext,
                              "/net/sf/click/control/control.js",
                              "click");

        ClickUtils.deployFile(servletContext,
                              "/net/sf/click/util/error.htm",
                              "click");

        ClickUtils.deployFile(servletContext,
                              "/net/sf/click/not-found.htm",
                              "click");

        ClickUtils.deployFile(servletContext,
                              "/net/sf/click/control/VM_global_library.vm",
                              "click");

        deployControls(getDeployClasses(getResourceRootElement("/click-controls.xml")));
        deployControls(getDeployClasses(getResourceRootElement("/extras-controls.xml")));
        
        if(config.controls != null){
        	deployControls(config.controls.toArray(new Class[config.controls.size()]));
        }
        deployControlSets(config);
    }

    private void loadMode(S2ClickConfig config) {
        String modeValue = "development";
        
        if(config.mode != null){
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
            logger.error("invalid application mode: " + mode);
            mode = DEBUG;
        }

        // Set Click and Velocity log levels
        int clickLogLevel = ClickLogger.INFO_ID;
        Integer velocityLogLevel = new Integer(ClickLogger.ERROR_ID);

        if (mode == PRODUCTION) {
            clickLogLevel = ClickLogger.WARN_ID;

        } else if (mode == DEVELOPMENT) {
            velocityLogLevel = new Integer(ClickLogger.WARN_ID);

        } else if (mode == DEBUG) {
            clickLogLevel = ClickLogger.DEBUG_ID;
            velocityLogLevel = new Integer(ClickLogger.WARN_ID);

        } else if (mode == TRACE) {
            clickLogLevel = ClickLogger.TRACE_ID;
            velocityLogLevel = new Integer(ClickLogger.INFO_ID);
        }

        logger.setLevel(clickLogLevel);
        velocityEngine.setApplicationAttribute(ClickLogger.LOG_LEVEL,
                                               velocityLogLevel);
    }

    private void loadDefaultPages() throws ClassNotFoundException {

        if (!pageByPathMap.containsKey(ERROR_PATH)) {
            ClickApp.PageElm page =
                new ClickApp.PageElm("net.sf.click.util.ErrorPage", ERROR_PATH);

            pageByPathMap.put(ERROR_PATH, page);
        }

        if (!pageByPathMap.containsKey(NOT_FOUND_PATH)) {
            ClickApp.PageElm page =
                new ClickApp.PageElm("net.sf.click.Page", NOT_FOUND_PATH);

            pageByPathMap.put(NOT_FOUND_PATH, page);
        }
    }

    private void loadHeaders(S2ClickConfig config) {
        if (config.headers != null || !config.headers.isEmpty()) {
            commonHeaders =
                Collections.unmodifiableMap(config.headers);
        } else {
            commonHeaders = Collections.unmodifiableMap(DEFAULT_HEADERS);
        }
    }

    private void loadFormatClass(S2ClickConfig config)
            throws ClassNotFoundException {

            Class clazz = config.formatClass;

            if(clazz != null){
            	formatClass = clazz;
            } else {
            	formatClass = net.sf.click.util.Format.class;
            }
    }

    private void loadFileItemFactory(S2ClickConfig config) throws Exception {
        if (config.fileItemFactory != null) {
        	fileItemFactory = config.fileItemFactory;
        } else {
            fileItemFactory = new DiskFileItemFactory();
        }
    }

    private void loadTemplates() throws Exception {
        if (mode == ClickApp.PRODUCTION || mode == ClickApp.PROFILE) {

            // Load page templates, which will be cached in ResourceManager
            for (Iterator i = pageByPathMap.keySet().iterator(); i.hasNext();) {
                String path = i.next().toString();

                if (!path.endsWith(".jsp")) {
                    try {
                        getTemplate(path);
                        if (logger.isDebugEnabled()) {
                            logger.debug("loaded page template " + path);
                        }
                    } catch (ParseErrorException pee) {
                        logger.warn("Errors in page '" + path, pee);
                    }
                }
            }
        }
    }

    private void loadPages(S2ClickConfig config) throws ClassNotFoundException {
    	// TODO 自動マッピングを使わない場合はどうする？？
    	
//        Element pagesElm = ClickUtils.getChild(rootElm, "pages");
//
//        if (pagesElm == null) {
//            String msg = "required configuration 'pages' element missing.";
//            throw new RuntimeException(msg);
//        }
    	
    	NamingConvention naming = (NamingConvention) 
    		SingletonS2ContainerFactory.getContainer().getComponent(NamingConventionImpl.class);
        pagesPackage = naming.getRootPackageNames()[0] + "." + naming.getPageSuffix().toLowerCase();
        if (StringUtils.isBlank(pagesPackage)) {
            pagesPackage = "";
        }

        pagesPackage = pagesPackage.trim();
        if (pagesPackage.endsWith(".")) {
            pagesPackage =
                pagesPackage.substring(0, pagesPackage.length() - 2);
        }

        boolean automap = true;
//        String automapStr = pagesElm.getAttribute("automapping");
//        if (StringUtils.isBlank(automapStr)) {
//            automapStr = "true";
//        }
//
//        if ("true".equalsIgnoreCase(automapStr)) {
//            automap = true;
//        } else if ("false".equalsIgnoreCase(automapStr)) {
//            automap = false;
//        } else {
//            String msg = "Invalid pages automapping attribute: " + automapStr;
//            throw new RuntimeException(msg);
//        }


//        String autobindingStr = pagesElm.getAttribute("autobinding");
//        if (StringUtils.isBlank(autobindingStr)) {
//            autobindingStr = "true";
//        }
        
        autobinding = config.autoBinding;

//        List pageList = getChildren(pagesElm, "page");
//
//        if (!pageList.isEmpty() && logger.isDebugEnabled()) {
//            logger.debug("click.xml pages:");
//        }
//
//        for (int i = 0; i < pageList.size(); i++) {
//            Element pageElm = (Element) pageList.get(i);
//
//            ClickApp.PageElm page =
//                new ClickApp.PageElm(pageElm, pagesPackage, commonHeaders);
//
//            pageByPathMap.put(page.getPath(), page);
//
//            if (logger.isDebugEnabled()) {
//                String msg =
//                    page.getPath() + " -> " + page.getPageClass().getName();
//                logger.debug(msg);
//            }
//        }

        if (automap) {

            // Build list of automap path page class overrides
            if (logger.isDebugEnabled()) {
                logger.debug("automapped pages:");
            }

            List templates = getTemplateFiles();

            for (int i = 0; i < templates.size(); i++) {
                String pagePath = (String) templates.get(i);

                if (!pageByPathMap.containsKey(pagePath)) {

                    String pageClassName = getPageClassName(pagePath, pagesPackage);

                    if (pageClassName != null) {
                        ClickApp.PageElm page = new ClickApp.PageElm(pagePath,
                                pageClassName, commonHeaders);

                        pageByPathMap.put(page.getPath(), page);

                        if (logger.isDebugEnabled()) {
                            String msg = pagePath + " -> " + pageClassName;
                            logger.debug(msg);
                        }
                    }
                }
            }
        }

        // Build pages by class map
        for (Iterator i = pageByPathMap.values().iterator(); i.hasNext();) {
            ClickApp.PageElm page = (ClickApp.PageElm) i.next();
            Object value = pageByClassMap.get(page.pageClassName);

            if (value == null) {
                pageByClassMap.put(page.pageClassName, page);

            } else if (value instanceof List) {
                ((List) value).add(value);

            } else if (value instanceof ClickApp.PageElm) {
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

    private void loadCharset(S2ClickConfig config) {
        String charset = config.charset;
        if (charset != null && charset.length() > 0) {
            this.charset = charset;
        }
    }

    private void loadLocale(S2ClickConfig config) {
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

    private Properties getVelocityProperties(ServletContext context)
            throws Exception {

        final Properties velProps = new Properties();

        // Set default velocity runtime properties.

        velProps.setProperty(RuntimeConstants.RESOURCE_LOADER, "webapp, class");
        velProps.setProperty("webapp.resource.loader.class",
                             WebappLoader.class.getName());
        velProps.setProperty("class.resource.loader.class",
                             ClasspathResourceLoader.class.getName());

        if (mode == PRODUCTION || mode == PROFILE) {
            velProps.put("webapp.resource.loader.cache", "true");
            velProps.put("webapp.resource.loader.modificationCheckInterval",
                         "0");
            velProps.put("class.resource.loader.cache", "true");
            velProps.put("class.resource.loader.modificationCheckInterval",
                         "0");
            velProps.put("velocimacro.library.autoreload", "false");
        } else {
            velProps.put("webapp.resource.loader.cache", "false");
            velProps.put("class.resource.loader.cache", "false");
            velProps.put("velocimacro.library.autoreload", "true");
        }

        velProps.put(RuntimeConstants.RUNTIME_LOG_LOGSYSTEM_CLASS,
                     ClickLogger.class.getName());

        // If 'macro.vm' exists set it as default VM library, otherwise use
        // 'click/VM_global_library.vm'
        URL macroURL = context.getResource("/" + MACRO_VM_FILE_NAME);
        if (macroURL != null) {
            velProps.put("velocimacro.library", MACRO_VM_FILE_NAME);
        } else {
            velProps.put("velocimacro.library", CLICK_PATH + File.separator
                         + VM_FILE_NAME);
        }

        // Set the character encoding
        if (getCharset() != null) {
            velProps.put("input.encoding", getCharset());
        }

        // Load user velocity properties.
        Properties userProperties = new Properties();

        String filename = DEFAULT_VEL_PROPS;

        InputStream inputStream = context.getResourceAsStream(filename);

        if (inputStream != null) {
            try {
                userProperties.load(inputStream);

            } catch (IOException ioe) {
                String message = "error loading velocity properties file: "
                        + filename;
                logger.error(message, ioe);

            } finally {
                try {
                    inputStream.close();
                } catch (IOException ioe) {
                    // ignore
                }
            }
        }

        // Add user properties.
        Iterator iterator = userProperties.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry entry = (Map.Entry) iterator.next();

            Object pop = velProps.put(entry.getKey(), entry.getValue());
            if (pop != null && logger.isDebugEnabled()) {
                String message = "user defined property '" + entry.getKey()
                        + "=" + entry.getValue()
                        + "' replaced default propery '" +  entry.getKey()
                        + "=" + pop + "'";
                logger.debug(message);
            }
        }

        if (logger.isTraceEnabled()) {
            TreeMap sortedPropMap = new TreeMap();

            Iterator i = velProps.entrySet().iterator();
            while (i.hasNext()) {
                Map.Entry entry = (Map.Entry) i.next();
                sortedPropMap.put(entry.getKey(), entry.getValue());
            }

            logger.trace("velocity properties: " + sortedPropMap);
        }

        return velProps;
    }

    private static Map loadHeadersMap(Element parentElm) {
        Map headersMap = new HashMap();

        List headerList = getChildren(parentElm, "header");

        for (int i = 0, size = headerList.size(); i < size; i++) {
            Element header = (Element) headerList.get(i);

            String name = header.getAttribute("name");
            String type = header.getAttribute("type");
            String propertyValue = header.getAttribute("value");

            Object value = null;

            if ("".equals(type) || "String".equalsIgnoreCase(type)) {
                value = propertyValue;
            } else if ("Integer".equalsIgnoreCase(type)) {
                value = Integer.valueOf(propertyValue);
            } else if ("Date".equalsIgnoreCase(type)) {
                value = new Date(Long.parseLong(propertyValue));
            } else {
                value = null;
                String message =
                    "Invalid property type [String|Integer|Date]: "
                    + type;
                throw new IllegalArgumentException(message);
            }

            headersMap.put(name, value);
        }

        return headersMap;
    }

    private List getTemplateFiles() {
        List fileList = new ArrayList();

        Set resources = servletContext.getResourcePaths("/");

        for (Iterator i = resources.iterator(); i.hasNext();) {
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

    private void processDirectory(String dirPath, List fileList) {
        Set resources = servletContext.getResourcePaths(dirPath);

        if (resources != null) {
            for (Iterator i = resources.iterator(); i.hasNext();) {
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
				if (logger.isDebugEnabled()) {
					logger.debug(pagePath + " -> CLASS NOT FOUND");
				}
				if (logger.isTraceEnabled()) {
					logger.trace("class not found: " + className);
				}
				return null;
			}
		}
        return className;
    }

    private static List getChildren(Element element, String name) {
        List list = new ArrayList();
        NodeList nodeList = element.getChildNodes();
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            if (node instanceof Element) {
                if (node.getNodeName().equals(name)) {
                    list.add(node);
                }
            }
        }
        return list;
    }
    
    private static Class loadClass(String className){
    	try {
    		return Thread.currentThread().getContextClassLoader().loadClass(className);
		} catch (SecurityException e) {
			return null;
		} catch(ClassNotFoundException e){
			return null;
		}
    }
    
    // ---------------------------------------------------------- Inner Classes

    private static class PageElm {

        private final Map headers;
        private final String path;
        private final String pageClassName;

        private PageElm(Element element, String pagesPackage, Map commonHeaders)
            throws ClassNotFoundException {

            // Set headers
            Map aggregationMap = new HashMap(commonHeaders);
            Map pageHeaders = loadHeadersMap(element);
            aggregationMap.putAll(pageHeaders);
            headers = Collections.unmodifiableMap(aggregationMap);

            // Set path
            String pathValue = element.getAttribute("path");
            if (pathValue.charAt(0) != '/') {
                path = "/" + pathValue;
            } else {
                path = pathValue;
            }

            // Set pageClass
            String value = element.getAttribute("classname");
            if (value != null) {
                if (pagesPackage.trim().length() > 0) {
                    value = pagesPackage + "." + value;
                }
            } else {
                String msg = "No classname defined for page path " + path;
                throw new RuntimeException(msg);
            }
            
            pageClassName = value;
        }

        private PageElm(String path, String pageClassName, Map commonHeaders) {
            headers = Collections.unmodifiableMap(commonHeaders);
            this.path = path;
            this.pageClassName = pageClassName;
        }

        private PageElm(String classname, String path) throws ClassNotFoundException {
            this.headers = Collections.EMPTY_MAP;
            this.pageClassName = classname;
            this.path = path;
        }

        private Field[] getFieldArray() {
        	try {
				return loadClass(pageClassName).getFields();
			} catch (SecurityException e) {
				throw new RuntimeException(e);
			}
        }

        private Map getFields() {
			Map fields = new HashMap();
			for (Field field: getFieldArray()) {
				fields.put(field.getName(), field);
			}
			return fields;
		}

        private Map getHeaders() {
            return headers;
        }

        private Class getPageClass() {
        	return loadClass(this.pageClassName);
        }

        private String getPath() {
            return path;
        }
    }

}
