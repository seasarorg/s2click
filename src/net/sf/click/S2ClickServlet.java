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
import org.seasar.s2click.annotation.Request;

/**
 * Seasar2��Click Framework��A�g�����邽�߂̃T�[�u���b�g�B
 * 
 * @author Naoki Takezoe
 */
public class S2ClickServlet extends ClickServlet {

	private static final long serialVersionUID = 1L;
	private boolean initialized = false;

	/**
	 * HOT deploy�ł͂Ȃ��ꍇ�A���̃��\�b�h��Click Framework�̏��������s���܂��B
	 */
	@Override public void init() throws ServletException {
		S2Container container = SingletonS2ContainerFactory.getContainer();
		if(!SmartDeployUtil.isHotdeployMode(container)){
			super.init();
			initialized = true;
		}
	}
	
	/**
	 * HOT deploy�̏ꍇ�A���N�G�X�g����Click Framework�̏��������s���܂��B
	 * <p>
	 * TODO �y�[�W���������Ȃ�ƒx���Ȃ邩�c�H
	 */
	@Override public void service(ServletRequest req, ServletResponse res)
			throws ServletException, IOException {
		if(initialized == false){
			super.init();
		}
		
		super.service(req, res);
	}

	/**
	 * S2Container����y�[�W�N���X�̃C���X�^���X���擾���܂��B
	 */
	@SuppressWarnings("unchecked")
	@Override protected Page newPageInstance(String path, Class pageClass,
			HttpServletRequest request) throws Exception {
		S2Container container = SingletonS2ContainerFactory.getContainer();
		return (Page) container.getComponent(pageClass);
	}
	
	/**
	 * {@link Request}�A�m�e�[�V�������t�^����Ă���t�B�[���h�Ƀ��N�G�X�g�p�����[�^���o�C���h���܂��B
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

                        PropertyUtils.setValueOgnl(page, name, value, ognlContext);

                        if (logger.isTraceEnabled()) {
                            logger.trace("   auto bound variable: " + name + "=" + value);
                        }
                    }
                }
            }
        }
    }
	
	/**
	 * ���N�G�X�g�p�����[�^���o�C���h����y�[�W�N���X�̃t�B�[���h���擾���܂��B
	 * 
	 * @param clazz �y�[�W�N���X��<code>Class</code>�I�u�W�F�N�g
	 * @param name ���N�G�X�g�p�����[�^��
	 * @return �t�B�[���h�i�Y���̃t�B�[���h�����݂��ȏꍇ��<code>null</code>�j
	 */
	private Field getRequestBindField(Class<? extends Page> clazz, String name){
		for(Field field: clickApp.getPageFieldArray(clazz)){
			Request req = field.getAnnotation(Request.class);
			if(req != null){
				if(StringUtils.isEmpty(req.value())){
					if(field.getName().equals(name)){
						return field;
					}
				} else {
					if(req.value().equals(name)){
						return field;
					}
				}
			}
		}
		return null;
	}
	
	
	
}
