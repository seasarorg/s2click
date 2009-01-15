/*
 * Copyright 2004-2008 Malcolm A. Edgar
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.sf.click;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Provides a mock Context object for unit testing.
 * 
 * @author Malcolm Edgar
 */
@SuppressWarnings("unchecked")
public class MockContext extends Context {
    
	protected Map<Class, String> pagePathMap = new HashMap<Class, String>();
	
    // ----------------------------------------------------------- Constructors
    
    private MockContext() {
        super(new MockRequest(), new MockResponse());
    }

//    private MockContext(Locale locale) {
//        super(new MockRequest(locale), new MockResponse());
//    }
    
    private MockContext(HttpServletRequest request) {
        super(request, new MockResponse());
    }
    
    private MockContext(HttpServletResponse response) {
        super(new MockRequest(), response);
    }
    
    private MockContext(HttpServletRequest request, HttpServletResponse response) {
        super(request, response);
    }
    
    // --------------------------------------------------------- Public Methods
    
    public static void initContext() {
    	Context.pushThreadLocalContext(new MockContext());
    }
    
//    public static void initContext(Locale locale) {
//    	Context.pushThreadLocalContext(new MockContext(locale));
//    }
    
    public static void initContext(HttpServletRequest request) {
    	Context.pushThreadLocalContext(new MockContext(request));
    }
    
    public static void initContext(HttpServletResponse response) {
    	Context.pushThreadLocalContext(new MockContext(response));
    }
    
    public static void initContext(HttpServletRequest request,
    		HttpServletResponse response) {
    	Context.pushThreadLocalContext(new MockContext(request, response));
    }
    
    /**
     * @see Context#getLocale()
     */
    @Override
    public Locale getLocale() {
        return request.getLocale();
    }
    
    /**
     * @see Context#getApplicationMode()
     */
    @Override
    public String getApplicationMode() {
        return "debug";
    }
    
    /**
     * @see Context#getRequestParameter(String)
     */
    @Override
    public String getRequestParameter(String name) {
        return request.getParameter(name);
    }    
   
    /**
     * @see Context#getRequestParameterMap()
     */
    @Override
    public Map getRequestParameterMap() {
        return request.getParameterMap();
    }

    /**
     * @see Context#getRequestParameterValues(String)
     */
    @Override
    public String[] getRequestParameterValues(String name) {
        return request.getParameterValues(name);
    }
    
    public void setPagePath(Class pageClass, String pagePath){
    	pagePathMap.put(pageClass, pagePath);
    }
    
    @Override
    public String getPagePath(Class pageClass){
    	return pagePathMap.get(pageClass);
    }
}
