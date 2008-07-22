package org.seasar.s2click.example.page;

import java.io.File;
import java.io.FileInputStream;

import org.apache.commons.lang.StringUtils;
import org.seasar.s2click.annotation.Path;
import org.seasar.s2click.page.AbstractDownloadPage;

/**
 * ファイルダウンロードのサンプル。
 * 
 * @author Naoki Takezoe
 */
@Path("/file-download.htm")
public class FileDownloadPage extends AbstractDownloadPage {
	
	public String name;

	@Override public void onInit() {
		if(StringUtils.isEmpty(name)){
			throw new RuntimeException("ファイル名が指定されていません。");
		}
		
		File folder = new File(getContext().getServletContext().getRealPath("WEB-INF/files"));
		if(!folder.exists()){
			throw new RuntimeException("ファイルを保存するフォルダが存在しません。");
		}
		
		try {
			File file = new File(folder, name);
			setFileName(name);
			setContents(new FileInputStream(file));
		} catch(Exception ex){
			throw new RuntimeException(ex);
		}
	}
	
}
