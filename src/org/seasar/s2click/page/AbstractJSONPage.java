package org.seasar.s2click.page;

import java.io.OutputStream;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.seasar.s2click.annotation.Path;

import net.arnx.jsonic.JSON;
import net.sf.click.Page;

/**
 * JSON��ԋp����y�[�W�N���X�̂��߂̒��ۊ��N���X�ł��B
 * <p>
 * {@link #setContents(Object)}��POJO�A�z��A<code>java.util.List</code>�A<code>java.util.Map</code>���Z�b�g���邱�ƂŁA
 * <a href="http://jsonic.sourceforge.jp/">JSONIC</a>�ɂ����JSON�ɕϊ������e�L�X�g�����X�|���X�Ƃ��ĕԋp���邱�Ƃ��ł��܂��B
 * <p>
 * �ȉ��Ɏ�����������܂��F
 * <pre>
 * <span class="kw">public</span> SampleJsonPage <span class="kw">extends</span> AbstractJSONPage {
 *   <span class="kw">public</span> SampleJSONPage(){
 *     List&lt;Employee&gt; list = new ArrayList&lt;Employee&gt;();
 *     ...
 *     setContents(list);
 *   }
 * }</pre>
 * <code>AbstractJSONPage</code>�̃T�u�N���X�ł́A�y�[�W�N���X����JSON�����X�|���X�ɏ����o����邽�߁AHTML�e���v���[�g�͕s�v�ł��B
 * �������AClick��HTML�e���v���[�g�����݂��Ȃ��y�[�W�N���X�͎����}�b�s���O�̑ΏۊO�ƂȂ邽�߁A
 * �N���X��{@link Path}�A�m�e�[�V������t�^���邱�ƂŃp�X�𖾎�����Ƃ悢�ł��傤�B
 * 
 * @author Naoki Takezoe
 */
public class AbstractJSONPage extends Page {
	
	protected String contentType = "application/x-javascript; charset=utf-8";
	protected Object contents;
	
	/**
	 * JSON�Ƃ��ĕԋp����JavaBean���Z�b�g���܂��B
	 * 
	 * @param contents JSON�Ƃ��ĕԋp����JavaBean
	 */
	protected void setContents(Object contents){
		if(contents == null){
			throw new IllegalArgumentException("contents shouldn't be null.");
		}
		this.contents = contents;
	}
	
	/**
	 * Content-Type�w�b�_���Z�b�g���܂��B
	 * 
	 * @param contentType Content-Type�w�b�_�̒l�B
	 *   �f�t�H���g�l��<code>"application/x-javascript; charset=utf-8"</code>�ł��B
	 */
	protected void setContentType(String contentType){
		if(contentType == null){
			throw new IllegalArgumentException("contentType shouldn't be null.");
		}
		this.contentType = contentType;
	}
	
	/**
	 * JSON��ԋp����y�[�W�ł�HTML�e���v���[�g���s�v�ł��邽�߁A
	 * ���̃��\�b�h�͏��<code>null</code>��Ԃ��悤��������Ă��܂��B
	 */
	@Override public String getPath() {
		return null;
	}
	
	/**
	 * ���̃��\�b�h����JSON���X�|���X���o�͂��܂��B
	 */
	@Override public void onRender() {
		if(this.contents == null){
			throw new IllegalStateException("JSON contents is not specified.");
		}
		
		HttpServletResponse res = getContext().getResponse();
		OutputStream out = null;
		
		try {
			res.setContentType(this.contentType);
			out = res.getOutputStream();
			out.write(JSON.encode(this.contents).getBytes("UTF-8"));
			
			res.flushBuffer();
			
		} catch(Exception ex){
			throw new RuntimeException(ex);
		} finally {
			IOUtils.closeQuietly(out);
		}
	}

}
