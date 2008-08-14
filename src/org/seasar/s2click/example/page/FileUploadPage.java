package org.seasar.s2click.example.page;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import net.sf.click.Context;
import net.sf.click.control.ActionLink;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.seasar.s2click.annotation.Request;
import org.seasar.s2click.example.form.FileUploadForm;

/**
 * ファイルアップロードのサンプルページ。
 * 
 * @see FileUploadForm
 * @author Naoki Takezoe
 */
public class FileUploadPage extends LayoutPage {

	public String title = "ファイルアップロード＆ダウンロード";
	public FileUploadForm form = new FileUploadForm("form");
	public ActionLink link = new ActionLink("link", this, "doDownload");
	
	/** ファイルのダウンロード時にファイル名を受け取るためのフィールド */
	@Request public String name;
	
	public FileUploadPage(){
		form.submit.setListener(this, "doUpload");
	}
	
	private static synchronized File getFolder(Context context){
		String path = context.getServletContext().getRealPath("WEB-INF/files");
		File folder = new File(path);
		if(!folder.exists() || !folder.isDirectory()){
			if(!folder.mkdir()){
				throw new RuntimeException("添付ファイルの保存用フォルダの作成に失敗しました。");
			}
		}
		return folder;
	}
	
	@Override public void onRender() {
		File folder = getFolder(getContext());
		addModel("files", folder.listFiles());
	}

	/**
	 * ファイルをアップロードします。
	 */
	public boolean doUpload(){
		if(form.isValid()){
			FileItem item = form.file.getFileItem();
			File folder = getFolder(getContext());
			
			String fileName = item.getName();
			if(fileName.indexOf("/") >= 0){
				fileName = fileName.substring(fileName.lastIndexOf("/") + 1, fileName.length());
			} else if(fileName.indexOf("\\") >= 0){
				fileName = fileName.substring(fileName.lastIndexOf("\\") + 1, fileName.length());
			}
			
			File file = new File(folder, fileName);
			InputStream in = null;
			FileOutputStream out = null;
			try {
				in = item.getInputStream();
				out = new FileOutputStream(file);
				IOUtils.copy(in, out);
			} catch(IOException ex){
				throw new RuntimeException(ex);
			} finally {
				IOUtils.closeQuietly(in);
				IOUtils.closeQuietly(out);
			}
		}
		return true;
	}
	
	public boolean doDownload() throws Exception {
		if(StringUtils.isEmpty(name)){
			throw new RuntimeException("ファイル名が指定されていません。");
		}
		
		File folder = new File(getContext().getServletContext().getRealPath("WEB-INF/files"));
		if(!folder.exists()){
			throw new RuntimeException("ファイルを保存するフォルダが存在しません。");
		}
		
		try {
			File file = new File(folder, name);
			renderFile(name, new FileInputStream(file));
		} catch(Exception ex){
			throw new RuntimeException(ex);
		}
		
		return false;
	}
}
