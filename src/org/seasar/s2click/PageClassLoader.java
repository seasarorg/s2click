package org.seasar.s2click;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.jar.JarFile;

import org.seasar.framework.util.ClassLoaderUtil;
import org.seasar.framework.util.ClassTraversal;
import org.seasar.framework.util.JarFileUtil;
import org.seasar.framework.util.StringUtil;
import org.seasar.framework.util.URLUtil;
import org.seasar.framework.util.ZipFileUtil;
import org.seasar.framework.util.ClassTraversal.ClassHandler;

/**
 * �����o�^�����y�[�W�N���X�̃N���X�����擾���邽�߂̃��[�e�B���e�B�N���X�B
 * 
 * @author Naoki Takezoe
 */
public class PageClassLoader implements ClassHandler {
	
	private String rootPackage;
	private List<String> classes = new ArrayList<String>();
	private Map<String, Strategy> strategies = new HashMap<String, Strategy>();
	
	/**
	 * �R���X�g���N�^�B
	 * 
	 * @param rootPackage �y�[�W�N���X�̃��[�g�p�b�P�[�W��
	 */
	public PageClassLoader(String rootPackage){
		this.rootPackage = rootPackage;
        strategies.put("file", new FileSystemStrategy());
        strategies.put("jar", new JarFileStrategy());
        strategies.put("zip", new ZipFileStrategy());
        strategies.put("code-source", new CodeSourceFileStrategy());
	}
	
	public void processClass(String packageName, String shortClassName) {
		if(shortClassName.endsWith("Page")){
			classes.add(packageName + "." + shortClassName);
		}
	}
	
	/**
	 * �����o�^�����y�[�W�N���X�̊��S�C���N���X���̃��X�g��ԋp���܂��B
	 * 
	 * @return �y�[�W�N���X���̃��X�g
	 */
    public List<String> getPageClasses(){
        final String rootDir = rootPackage.replace('.', '/');
        for (final Iterator<?> it = ClassLoaderUtil.getResources(rootDir); it.hasNext();) {
        	final URL url = (URL) it.next();
        	final Strategy strategy = getStrategy(
        			URLUtil.toCanonicalProtocol(url.getProtocol()));
        		strategy.getPages(rootDir, url);
        }
        return classes;
    }
    
    private Strategy getStrategy(String protocol){
    	return strategies.get(protocol);
    }
    
    private interface Strategy {
        void getPages(String path, URL url);
    }
    
    private class FileSystemStrategy implements Strategy {

        public void getPages(String path, URL url) {
            File rootDir = getRootDir(path, url);
            ClassTraversal.forEach(rootDir, rootPackage, PageClassLoader.this);
        }

        protected File getRootDir(String path, URL url) {
            File file = URLUtil.toFile(url);
            String[] names = StringUtil.split(path, "/");
            for (int i = 0; i < names.length; ++i) {
                file = file.getParentFile();
            }
            return file;
        }
    }

    private class JarFileStrategy implements Strategy {

        public void getPages(String path, URL url) {
            JarFile jarFile = createJarFile(url);
            ClassTraversal.forEach(jarFile, PageClassLoader.this);
        }

        protected JarFile createJarFile(URL url) {
            return JarFileUtil.toJarFile(url);
        }
    }

    private class ZipFileStrategy implements Strategy {

        public void getPages(String path, URL url) {
            final JarFile jarFile = createJarFile(url);
            ClassTraversal.forEach(jarFile, PageClassLoader.this);
        }

        protected JarFile createJarFile(URL url) {
            final String jarFileName = ZipFileUtil.toZipFilePath(url);
            return JarFileUtil.create(new File(jarFileName));
        }
    }

    private class CodeSourceFileStrategy implements Strategy {

        public void getPages(String path, URL url) {
            final JarFile jarFile = createJarFile(url);
            ClassTraversal.forEach(jarFile, PageClassLoader.this);
        }

        protected JarFile createJarFile(final URL url) {
            final URL jarUrl = URLUtil.create("jar:file:" + url.getPath());
            return JarFileUtil.toJarFile(jarUrl);
        }
    }

}
