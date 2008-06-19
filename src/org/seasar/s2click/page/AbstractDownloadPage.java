package org.seasar.s2click.page;

import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.click.Page;

import org.apache.commons.io.IOUtils;
import org.seasar.s2click.annotation.Path;

/**
 * �t�@�C�����_�E�����[�h����y�[�W�̂��߂̒��ۊ��N���X�ł��B
 * <p>
 * �ȉ��Ɏ�����������܂��F
 * <pre>
 * <span class="kw">public</span> SampleDownloadPage <span class="kw">extends</span> AbstractDownloadPage {
 *   <span class="kw">public</span> SampleDownloadPage(){
 *     setFileName(<span class="st">"sample.txt"</span>);
 *     setContents(SampleDownloadPage.class.getResourceAsStream(<span class="st">"sample.txt"</span>));
 *   }
 * }</pre>
 * �t�@�C���_�E�����[�h�y�[�W�̓_�E�����[�h����R���e���c���������g�Ń��X�|���X�ɏ������ނ��߁AHTML�e���v���[�g�͕s�v�ł��B
 * �������AClick��HTML�e���v���[�g�����݂��Ȃ��y�[�W�N���X�͎����}�b�s���O�̑ΏۊO�ƂȂ邽�߁A
 * �N���X��{@link Path}�A�m�e�[�V������t�^���邱�ƂŃp�X�𖾎�����Ƃ悢�ł��傤�B
 * 
 * @author Naoki Takezoe
 */
public abstract class AbstractDownloadPage extends Page {
	
	protected String contentType = "application/octet-stream";
	protected String contentDisposition = "attachment";
	protected String fileName;
	protected InputStream contents;
	
	/**
	 * Content-Type�w�b�_�̒l��ݒ肵�܂��B
	 * 
	 * @param contentType �R���e���c�^�C�v�B
	 *   �f�t�H���g�l��<code>"application/octet-stream"</code>�ł��B
	 */
	protected void setContentType(String contentType){
		if(contentType == null){
			throw new IllegalArgumentException("contentType shouldn't be null.");
		}
		this.contentType = contentType;
	}
	
	/**
	 * �_�E�����[�h�t�@�C�������Z�b�g���܂��B
	 * 
	 * @param fileName �t�@�C����
	 */
	protected void setFileName(String fileName){
		this.fileName = fileName;
	}
	
	/**
	 * �_�E�����[�h����R���e���c��<code>InputStream</code>���Z�b�g���܂��B
	 * 
	 * @param contents �_�E�����[�h����R���e���c
	 */
	protected void setContents(InputStream contents){
		if(contents == null){
			throw new IllegalArgumentException("contents shouldn't be null.");
		}
		this.contents = contents;
	}
	
	/**
	 * Content-Disposition�w�b�_�̒l���Z�b�g���܂��B
	 * 
	 * @param contentDisposition <code>"attachment"</code>��������<code>"inline"</code>�B
	 *    �f�t�H���g�l��<code>"attachment"</code>�ł��B
	 */
	protected void setContentDisposition(String contentDisposition){
		this.contentDisposition = contentDisposition;
	}

	/**
	 * �t�@�C���_�E�����[�h�y�[�W�ł�HTML�e���v���[�g���s�v�ł��邽�߁A
	 * ���̃��\�b�h�͏��<code>null</code>��Ԃ��悤��������Ă��܂��B
	 */
	@Override public String getPath() {
		return null;
	}

	/**
	 * ���̃��\�b�h���Ń_�E�����[�h�R���e���c�����X�|���X�ɏ����o���܂��B
	 */
	@Override public void onRender() {
		if(this.contents == null){
			throw new IllegalStateException("download contents is not specified.");
		}
		
		HttpServletRequest req = getContext().getRequest();
		HttpServletResponse res = getContext().getResponse();
		OutputStream out = null;
		String fileName = this.fileName;
		String contentDisposition = this.contentDisposition;
		
		try {
			res.setContentType(this.contentType);
			
			if(this.contentType != null){
				String userAgent = req.getHeader("USER-AGENT");
				if(userAgent.indexOf("MSIE") >= 0 && userAgent.indexOf("Opera") < 0){
					fileName = new String(fileName.getBytes("Windows-31J"), "ISO8859_1");
				} else {
					fileName = new String(fileName.getBytes("UTF-8"), "ISO8859_1");
				}
				contentDisposition = contentDisposition + "; filename=\"" + fileName + "\"";
			}
			res.setHeader("Content-Disposition", contentDisposition);
			
			out = res.getOutputStream();
			byte[] buf = new byte[1024 * 8];
			int length = 0;
			while((length = this.contents.read(buf)) >= 0){
				out.write(buf, 0, length);
			}
			
			res.flushBuffer();
			
		} catch(Exception ex){
			throw new RuntimeException(ex);
		} finally {
			IOUtils.closeQuietly(this.contents);
			IOUtils.closeQuietly(out);
		}
	}
	
}
