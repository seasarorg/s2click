/*
 * Copyright 2006-2011 the Seasar Foundation and the Others.
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
package org.seasar.s2click.servlet;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;

import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.arnx.jsonic.JSON;
import ognl.OgnlException;
import ognl.TypeConverter;

import org.apache.click.ClickServlet;
import org.apache.click.Context;
import org.apache.click.Page;
import org.apache.click.util.ErrorPage;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.seasar.framework.container.S2Container;
import org.seasar.framework.container.factory.SingletonS2ContainerFactory;
import org.seasar.framework.container.util.SmartDeployUtil;
import org.seasar.s2click.S2ClickPage;
import org.seasar.s2click.annotation.Ajax;
import org.seasar.s2click.exception.AjaxException;
import org.seasar.s2click.filter.UrlPatternFilter;
import org.seasar.s2click.util.AjaxUtils;

/**
 * Seasar2とClick Frameworkを連携させるためのサーブレット。
 *
 * @author Naoki Takezoe
 */
public class S2ClickServlet extends ClickServlet {

	private static final long serialVersionUID = 1L;
	private boolean initialized = false;

	/**
	 * HOT deployではない場合、このメソッドでClick Frameworkの初期化を行います。
	 */
	@Override
	public void init() throws ServletException {
		S2Container container = SingletonS2ContainerFactory.getContainer();
		if(!SmartDeployUtil.isHotdeployMode(container)){
			super.init();
			initialized = true;
		}
	}

	/**
	 * HOT deployの場合、リクエスト毎にClick Frameworkの初期化を行います。
	 */
	@Override
	public void service(ServletRequest req, ServletResponse res)
			throws ServletException, IOException {

		String hotDeployInitStatus = (String) req.getAttribute(UrlPatternFilter.HOTDEPLOY_INIT_KEY);

		if(initialized == false && !"initialized".equals(hotDeployInitStatus)){
			super.init();
			req.setAttribute(UrlPatternFilter.HOTDEPLOY_INIT_KEY, "initialized");

			if("initialize".equals(hotDeployInitStatus)){
				return;
			}
		}

		super.service(new S2ClickRequestWrapper((HttpServletRequest) req), res);
	}

	/**
	 * S2Containerからページクラスのインスタンスを取得します。
	 */
	@Override
	@SuppressWarnings("rawtypes")
	protected Page newPageInstance(String path, Class pageClass,
			HttpServletRequest request) throws Exception {
		if(pageClass == Page.class || pageClass == ErrorPage.class){
			return (Page) pageClass.newInstance();
		}
		S2Container container = SingletonS2ContainerFactory.getContainer();
		return (Page) container.getComponent(pageClass);
	}

	/**
	 * ページクラスのフィールドへのリクエストパラメータのバインドは{@link S2ClickPage}で行うため、
	 * このメソッドでは何も行いません。
	 */
	@Override
	protected void processPageRequestParams(Page page) throws OgnlException {
		// なにもしない
	}

	@Override
	protected void renderTemplate(Page page) throws Exception {
		String skipRendering = (String) page.getContext().getRequestAttribute(
				S2ClickPage.SKIP_RENDERING);
		if(!"true".equals(skipRendering)){
			super.renderTemplate(page);
		}
	}

    protected void processPage(Page page) throws Exception {

		final Context context = page.getContext();
		final HttpServletRequest request = context.getRequest();

		String methodName = request.getParameter("ajax");
		if(StringUtils.isNotEmpty(methodName)){
			// Ajaxによる呼び出し
			Method method = getAjaxMethod(page, methodName);
			if(method != null){
				// メソッドの引数
				Class<?>[] types = method.getParameterTypes();
				Object[] args = new Object[method.getParameterTypes().length];
				TypeConverter converter = getTypeConverter();
				for(int i=0;i<args.length;i++){
					args[i] = converter.convertValue(
							null, null, null, null, request.getParameter("arg" + i), types[i]);
				}

				// メソッド呼び出し
				Object result = method.invoke(page, (Object[]) args);

				if(result != null){
					renderAjaxResponse(context, result);
				}

			} else {
				throw new AjaxException(
						"Ajaxで呼び出すメソッドが見つかりません。メソッド名: " + methodName);
			}

		} else {
			// 通常のページの処理
			super.processPage(page);
		}
	}

    /**
     * {@link Ajax}アノテーションによって呼び出されたメソッドの戻り値をレンダリングします。
     * <p>
     * 戻り値がStringの場合は文字列をそのまま返却します。コンテンツタイプは<code>"text/html; charset=utf-8"</code>になります。
     * String以外の場合はJSONに変換して返却します。コンテンツタイプは<code>"application/x-javascript; charset=utf-8"</code>になります。
     * <p>
     * 上記以外のレスポンスを返却したい場合は本メソッドをオーバーライドしてください。
     *
     * @param context コンテキスト
     * @param result Ajaxで呼び出されたメソッドの戻り値
     * @throws Exception レンダリング中にエラーが発生した場合
     */
    protected void renderAjaxResponse(Context context, Object result) throws Exception {

		final HttpServletResponse response = context.getResponse();

		// 結果をレスポンスに出力
		OutputStream out = null;
		InputStream in = null;

		try {
			if(result instanceof String){
				// 文字列の場合はHTMLとして返却
				in = new ByteArrayInputStream(((String) result).getBytes("UTF-8"));
				response.setContentType(AjaxUtils.CONTENT_TYPE_HTML);

			} else {
				// それ以外の場合はJSONとして返却
				String json = JSON.encode(result);
				in = new ByteArrayInputStream(json.getBytes("UTF-8"));
				response.setContentType(AjaxUtils.CONTENT_TYPE_JSON);
			}

			response.setContentLength(in.available());
			out = response.getOutputStream();
			IOUtils.copy(in, out);
			response.flushBuffer();

		} finally {
			IOUtils.closeQuietly(out);
			IOUtils.closeQuietly(in);
		}
    }

    /**
     * ページクラスからAjaxで呼び出すメソッドを取得します。
     *
     * @param page ページクラスのインスタンス
     * @param methodName Ajaxで呼び出すメソッド名
     * @return {@link Ajax}アノテーションが付与されており、メソッド名が一致するpublicメソッドを返却します。
     *   メソッドが見つからない場合は<code>null</code>を返します。
     */
    protected Method getAjaxMethod(Page page, String methodName){
		Method[] methods = page.getClass().getMethods();
		for(Method method: methods){
			if(method.getName().equals(methodName) && method.getAnnotation(Ajax.class) != null){
				return method;
			}
		}
		return null;
    }

}
