package net.sf.click;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Enumeration;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import net.sf.click.util.PropertyUtils;
import net.sf.click.util.RequestTypeConverter;
import ognl.Ognl;
import ognl.OgnlException;

import org.apache.commons.lang.StringUtils;
import org.seasar.framework.container.S2Container;
import org.seasar.framework.container.factory.SingletonS2ContainerFactory;
import org.seasar.framework.container.util.SmartDeployUtil;
import org.seasar.framework.util.tiger.ReflectionUtil;
import org.seasar.s2click.S2ClickPage;
import org.seasar.s2click.annotation.Request;

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
	@Override public void init() throws ServletException {
		S2Container container = SingletonS2ContainerFactory.getContainer();
		if(!SmartDeployUtil.isHotdeployMode(container)){
			super.init();
			initialized = true;
		}
	}
	
	/**
	 * HOT deployの場合、リクエスト毎にClick Frameworkの初期化を行います。
	 * <p>
	 * TODO ページ数が多くなると遅くなるか…？
	 */
	@Override public void service(ServletRequest req, ServletResponse res)
			throws ServletException, IOException {
		if(initialized == false){
			super.init();
		}
		
		super.service(new S2ClickRequestWrapper((HttpServletRequest) req), res);
	}

	/**
	 * S2Containerからページクラスのインスタンスを取得します。
	 */
	@SuppressWarnings("unchecked")
	@Override protected Page newPageInstance(String path, Class pageClass,
			HttpServletRequest request) throws Exception {
		S2Container container = SingletonS2ContainerFactory.getContainer();
		return (Page) container.getComponent(pageClass);
	}
	
	/**
	 * {@link Request}アノテーションが付与されているフィールドにリクエストパラメータをバインドします。
	 */
	@SuppressWarnings("unchecked")
	@Override protected void processPageRequestParams(Page page) throws OgnlException {

        if (clickApp.getPageFields(page.getClass()).isEmpty()) {
            return;
        }

        Map ognlContext = null;

        boolean customConverter =
            ! getTypeConverter().getClass().equals(RequestTypeConverter.class);

        HttpServletRequest request = page.getContext().getRequest();

        for (Enumeration e = request.getParameterNames(); e.hasMoreElements();) {
            String name = e.nextElement().toString();
            String value = request.getParameter(name);

            if (StringUtils.isNotBlank(value)) {

                Field field = getRequestBindField(page.getClass(), name);

                if (field != null) {
                    Class type = field.getType();

                    if (customConverter
                        || (type.isPrimitive()
                            || String.class.isAssignableFrom(type)
                            || Number.class.isAssignableFrom(type)
                            || Boolean.class.isAssignableFrom(type))) {

                        if (ognlContext == null) {
                            ognlContext = Ognl.createDefaultContext(page, null, getTypeConverter());
                        }

//                        PropertyUtils.setValueOgnl(page, name, value, ognlContext);
                        PropertyUtils.setValueOgnl(page, field.getName(), value, ognlContext);

                        if (logger.isTraceEnabled()) {
                            logger.trace("   auto bound variable: " + name + "=" + value);
                        }
                    }
                }
            }
        }
        
        // @Requestのrequired()パラメータのチェック
		for(Field field: clickApp.getPageFieldArray(page.getClass())){
			Request req = field.getAnnotation(Request.class);
			if(req != null && req.required()){
				Object value = ReflectionUtil.getValue(field, page);
				if(value == null || (value instanceof String && StringUtils.isEmpty((String) value))){
					throw new RuntimeException("パラメータが不正です。");
				}
			}
		}
    }
	
	/**
	 * リクエストパラメータをバインドするページクラスのフィールドを取得します。
	 * 
	 * @param clazz ページクラスの<code>Class</code>オブジェクト
	 * @param name リクエストパラメータ名
	 * @return フィールド（該当のフィールドが存在しな場合は<code>null</code>）
	 */
	private Field getRequestBindField(Class<? extends Page> clazz, String name){
		for(Field field: clickApp.getPageFieldArray(clazz)){
			Request req = field.getAnnotation(Request.class);
			if(req != null){
				if(StringUtils.isEmpty(req.name())){
					if(field.getName().equals(name)){
						return field;
					}
				} else {
					if(req.name().equals(name)){
						return field;
					}
				}
			}
		}
		return null;
	}

	@Override protected void renderTemplate(Page page) throws Exception {
		String skipRendering = (String) page.getContext().getRequestAttribute(
				S2ClickPage.SKIP_RENDERING);
		if(!"true".equals(skipRendering)){
			super.renderTemplate(page);
		}
	}
	
}
