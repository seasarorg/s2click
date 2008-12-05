package org.seasar.s2click.servlet;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.seasar.s2click.control.FCKEditor;

/**
 * {@link FCKEditor}コントロールでアップロードしたファイルをダウンロードするためのサーブレットです。
 * <p>
 * いちおう日本語ファイル名にも対応しているはずです。
 * 
 * @author Naoki Takezoe
 * @since 0.4.0
 */
public class FileDownloadServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	
	/**
	 * ファイルダウンロードに認証処理が必要な場合はこのメソッドをオーバーライドして実装します。
	 * 
	 * @param request リクエスト
	 * @param response レスポンス
	 * @param path パス
	 * @param file 実際のファイル
	 * @throws IOException セキュリティエラー
	 */
	protected void onSecurityCheck(HttpServletRequest request, HttpServletResponse response, 
			String path, File file) throws IOException {
	}
	
	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		String path = request.getPathInfo();
		if(path.indexOf("..") != -1){
			throw new IOException("パスが不正です。");
		}
		
		path = new String(path.getBytes("ISO8859_1"), "UTF-8");
		
		String realPath = getServletContext().getRealPath("/files" + path);
		File file = new File(realPath);
		
		// セキュリティチェック
		onSecurityCheck(request, response, realPath, file);
		
		String fileName = file.getName();
		
		if(!file.exists() && !file.isFile()){
			throw new IOException(realPath + "ファイルが存在しません。");
		}
		InputStream in = null;
		OutputStream out = null;
		try {
			String userAgent = request.getHeader("USER-AGENT");
			if(userAgent.indexOf("MSIE") >= 0 && userAgent.indexOf("Opera") < 0){
				// IEの場合
				fileName = new String(fileName.getBytes("Windows-31J"), "ISO8859_1");
				// Apache連携時に問題がある場合は↓のコードを使うこと
				//fileName = URLEncoder.encode(fileName, "UTF-8");
			} else {
				// IE以外の場合（Firefox/Opera）
				fileName = new String(fileName.getBytes("UTF-8"), "ISO8859_1");
				// ↓MIMEエンコードではOperaで文字化けしてしまう
				//fileName = MimeUtility.encodeText(fileName, "UTF-8", null);
			}
			response.setContentType("application/octet-stream");
			response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
			
			in = new FileInputStream(file);
			out = response.getOutputStream();
			
			IOUtils.copy(in, out);
			
			response.flushBuffer();
			
			return;
			
		} catch(Exception ex){
			throw new IOException("ファイルのダウンロードに失敗しました。");
		} finally {
			IOUtils.closeQuietly(in);
			IOUtils.closeQuietly(out);
		}
	}
	
}
