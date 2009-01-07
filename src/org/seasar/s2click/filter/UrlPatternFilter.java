package org.seasar.s2click.filter;

import java.io.IOException;
import java.util.regex.Matcher;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.seasar.framework.container.S2Container;
import org.seasar.framework.container.factory.SingletonS2ContainerFactory;
import org.seasar.framework.container.util.SmartDeployUtil;
import org.seasar.s2click.annotation.UrlPattern;
import org.seasar.s2click.filter.UrlPatternManager.UrlRewriteInfo;

/**
 * S2ClickでURLパターン機能を実現するためのフィルタです。
 * 
 * @author Naoki Takezoe
 * @since 0.4.0
 * @see UrlPattern
 * @see UrlPatternManager
 */
public class UrlPatternFilter implements Filter {
	
	/**
	 * HOT deploy時のS2ClickServletへの初期化要求のステータスをリクエスト属性に格納するキー。
	 */
	public static final String HOTDEPLOY_INIT_KEY = UrlPatternFilter.class.getName();
	
	/**
	 * URLパターンでURLリライティングを行った場合に元のrequestUriをリクエスト属性に格納するキー。
	 */
	public static final String ROW_REQUEST_URI = UrlPatternFilter.class.getName() + ".requestUri";
	
	/**
	 * URLパターンでURLリライティングを行った場合に元のqueryStringをリクエスト属性に格納するキー。
	 */
	public static final String ROW_QUERY_STRING = UrlPatternFilter.class.getName() + ".queryString";
	
	private static Logger logger = Logger.getLogger(UrlPatternFilter.class);
	
	public void destroy() {
	}
	
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		
		if(logger.isDebugEnabled()){
			logger.debug("UrlRewriteFilterを開始します。");
		}
		
		// HOT deployの場合はここでClickAppを初期化する
		S2Container container = SingletonS2ContainerFactory.getContainer();
		
		if(SmartDeployUtil.isHotdeployMode(container) && request.getAttribute(HOTDEPLOY_INIT_KEY) == null){
			if(logger.isDebugEnabled()){
				logger.debug("UrlRewriteFilterからClickの初期化を要求します。");
			}
			
			request.setAttribute(HOTDEPLOY_INIT_KEY, "initialize");
			RequestDispatcher dispatcher = request.getRequestDispatcher("/init.htm");
			dispatcher.include(request, response);
		}
		
		HttpServletRequest req = HttpServletRequest.class.cast(request);
		
		String requestUri = req.getRequestURI();
		String context = req.getContextPath();
		String queryString = req.getQueryString();
		
		String requestPath = requestUri.substring(context.length());
		if(StringUtils.isNotEmpty(queryString)){
			requestPath = requestPath + "?" + queryString;
		}
		
		for(UrlRewriteInfo info: UrlPatternManager.getAll()){
			Matcher matcher = info.pattern.matcher(requestPath);
			if(matcher.matches()){
				StringBuilder realPath = new StringBuilder();
				realPath.append(info.realPath);
				for(int i=0; i<info.parameters.length; i++){
					if(i == 0){
						realPath.append("?");
					} else {
						realPath.append("&");
					}
					realPath.append(info.parameters[i]);
					realPath.append("=");
					realPath.append(matcher.group(i + 1));
				}
				
				if(logger.isDebugEnabled()){
					logger.debug(realPath.toString() + "にフォワードします。");
				}
				
				// 元のリクエストURIとクエリ文字列を保存しておく
				request.setAttribute(ROW_REQUEST_URI, requestUri);
				request.setAttribute(ROW_QUERY_STRING, queryString);
				
				RequestDispatcher dispatcher = request.getRequestDispatcher(realPath.toString());
				dispatcher.forward(request, response);
				return;
			}
		}
		
		chain.doFilter(request, response);
	}

	public void init(FilterConfig config) throws ServletException {
	}

}
