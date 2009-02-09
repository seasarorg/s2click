package org.seasar.s2click.servlet;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.click.util.ClickUtils;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;

/**
 * クラスパス内のリソースをWebブラウザから参照するためのサーブレットです。
 * <p>
 * web.xmlの設定例を以下に示します。
 * <pre>
 * &lt;servlet&gt;
 *   &lt;servlet-name&gt;ResourceServlet&lt;/servlet-name&gt;
 *   &lt;servlet-class&gt;org.seasar.s2click.servlet.ResourceServlet&lt;/servlet-class&gt;
 *   &lt;init-param&gt;
 *     &lt;param-name&gt;rootPackage&lt;/param-name&gt;
 *     &lt;param-value&gt;org.seasar.s2click.example.resource&lt;/param-value&gt;
 *   &lt;/init-param&gt;
 *   &lt;load-on-startup&gt;3&lt;/load-on-startup&gt;
 * &lt;/servlet&gt;
 * ...
 * &lt;servlet-mapping&gt;
 *   &lt;servlet-name&gt;ResourceServlet&lt;/servlet-name&gt;
 *   &lt;url-pattern&gt;/resources/*&lt;/url-pattern&gt;
 * &lt;/servlet-mapping&gt; </pre>
 *
 * <code>ResourceServlet</code>には初期化パラメータとして<code>rootPackage</code>を指定する必要があります。
 * このパラメータにはリソースを格納するパッケージ名を指定してください。
 * パッケージ名はカンマで区切って複数指定することもできます。
 * <p>
 * 上記の設定の場合、<code>org.seasar.s2click.example.resource</code>パッケージ配下に配置したリソースに対し、
 * <code>http://localhost:8080/s2click/resources/sample.gif</code>というURLでアクセスすることができます
 * （ホスト名、ポートやコンテキストは環境にあわせて読み替えてください）。
 * <p>
 * また、<code>/resources/subpackage/sample.gif</code>のように
 * パスをネストさせることでサブパッケージのリソースを参照することも可能です。
 *
 * @author Naoki Takezoe
 */
public class ResourceServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	private static final String ROOT_PACKAGE = "rootPackage";

	private String[] rootPathArray = null;

	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String path = request.getPathInfo();

		if(path.indexOf("..") != -1){
			throw new IOException("パスが不正です。");
		}

		path = new String(path.getBytes("ISO8859_1"), "UTF-8");

		for(String rootPath: this.rootPathArray){
			String resourcePath = rootPath + path;
	
			InputStream in = Thread.currentThread()
				.getContextClassLoader().getResourceAsStream(resourcePath);
	
			if(in == null){
				continue;
			}
	
			// MIMEタイプの取得
			int index = resourcePath.lastIndexOf(".");
			String contentType = "application/octet-stream";
			if(index != -1){
				String mimeType = ClickUtils.getMimeType(resourcePath);
				if(StringUtils.isNotEmpty(mimeType)){
					contentType = mimeType;
				}
			}
	
			// レスポンスを書き出し
			response.setContentType(contentType);
			OutputStream out = response.getOutputStream();
	
			IOUtils.copy(in, out);
	
			response.flushBuffer();
			return;
		}
		
		// TODO 404エラーを返すべき？
		throw new IOException("リソースが存在しません。");
	}

	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);

		String rootPackage = config.getInitParameter(ROOT_PACKAGE);
		if(StringUtils.isEmpty(rootPackage)){
			throw new ServletException("初期化パラメータ 'rootPackage' が指定されていません。");
		}

		List<String> rootPathList = new ArrayList<String>();
		for(String packageName: rootPackage.split(",")){
			packageName = packageName.trim();
			if(StringUtils.isNotEmpty(packageName)){
				rootPathList.add("/" + rootPackage.replace(".", "/"));
			}
		}
		if(rootPathList.isEmpty()){
			throw new ServletException("初期化パラメータ 'rootPackage' が指定されていません。");
		}
		
		this.rootPathArray = rootPathList.toArray(new String[rootPathList.size()]);
	}

}
