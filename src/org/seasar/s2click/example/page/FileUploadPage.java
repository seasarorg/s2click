package org.seasar.s2click.example.page;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.io.IOUtils;
import org.seasar.s2click.example.form.FileUploadForm;

/**
 * ファイルアップロードのサンプルページ。
 * 
 * @see FileUploadForm
 * @author Naoki Takezoe
 */
public class FileUploadPage extends LayoutPage {

	public FileUploadForm form = new FileUploadForm("form");
	
	public FileUploadPage(){
		form.submit.setListener(this, "doUpload");
	}
	
	private File getFolder(){
		String path = getContext().getServletContext().getRealPath("WEB-INF/files");
		return new File(path);
	}
	
	@Override
	public void onRender() {
		File folder = getFolder();
		addModel("files", folder.listFiles());
	}

	public boolean doUpload(){
		if(form.isValid()){
			FileItem item = form.file.getFileItem();
			File folder = getFolder();
			
			if(!folder.exists()){
				if(!folder.mkdir()){
					throw new RuntimeException("フォルダの作成に失敗しました。");
				}
			}
			
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
	
}
