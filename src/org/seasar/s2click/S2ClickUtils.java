package org.seasar.s2click;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.seasar.framework.container.S2Container;
import org.seasar.framework.container.factory.SingletonS2ContainerFactory;

/**
 * S2Click���Ŏg�p���郆�[�e�B���e�B���\�b�h��񋟂��܂��B
 * 
 * @author Naoki Takezoe
 */
public class S2ClickUtils {
	
    /**
     * JavaScript�̕�������G�X�P�[�v���܂��B
     *
     * @param value ������
     * @return �G�X�P�[�v���ꂽ������
     */
    public static String escapeJavaScript(String value) {
        if (value == null) {
            return "";
        }
        value = value.replaceAll("\\\\", "\\\\\\\\");
        value = value.replaceAll("'", "\\\\'");
        return value;
    }
    
	/**
	 * �����ɓn���ꂽ�������<tt>s2click.dicon</tt>�Ŏw�肳�ꂽ�����R�[�h��URL�G���R�[�h���܂��B
	 * 
	 * @param value ������
	 * @return URL�G���R�[�h��̕�����
	 */
	public static String urlEncode(String value){
		try {
			S2ClickConfig config = getComponent(S2ClickConfig.class);
			return URLEncoder.encode(value, config.charset);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * <code>SingletonS2ContainerFactory</code>����R���|�[�l���g���擾���܂��B
	 * 
	 * @param <T> �擾����R���|�[�l���g�̌^
	 * @param clazz �擾����R���|�[�l���g�̌^
	 * @return �R���|�[�l���g
	 */
	@SuppressWarnings("unchecked")
	public static <T> T getComponent(Class<T> clazz){
		S2Container container = SingletonS2ContainerFactory.getContainer();
		return (T) container.getComponent(clazz);
	}
	
}
