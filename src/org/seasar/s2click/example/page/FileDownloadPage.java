package org.seasar.s2click.example.page;

import java.io.File;
import java.io.FileInputStream;

import org.apache.commons.lang.StringUtils;
import org.seasar.s2click.annotation.Path;
import org.seasar.s2click.page.AbstractDownloadPage;

/**
 * �t�@�C���_�E�����[�h�̃T���v���B
 * 
 * @author Naoki Takezoe
 */
@Path("/file-download.htm")
public class FileDownloadPage extends AbstractDownloadPage {
	
	public String name;

	@Override
	public void onInit() {
		if(StringUtils.isEmpty(name)){
			throw new RuntimeException("�t�@�C�������w�肳��Ă��܂���B");
		}
		
		File folder = new File(getContext().getServletContext().getRealPath("WEB-INF/files"));
		if(!folder.exists()){
			throw new RuntimeException("�t�@�C����ۑ�����t�H���_�����݂��܂���B");
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
