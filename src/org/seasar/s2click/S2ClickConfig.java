package org.seasar.s2click;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.click.Control;
import net.sf.click.util.Format;

import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;

/**
 * dicon�t�@�C����Click�̐ݒ���s�����߂̃N���X�B
 * 
 * @author Naoki Takezoe
 */
public class S2ClickConfig {
	
	/**
	 * �����R�[�h�B�f�t�H���g��UTF-8�ł��B
	 */
	public String charset = "UTF-8";
	
	/**
	 * �y�[�W�e���v���[�g�ŗ��p�\�ȃt�H�[�}�b�g�N���X�B
	 * �f�t�H���g��<code>net.sf.click.util.Format</code>�ł��B
	 */
	public Class<? extends Format> formatClass = Format.class;
	
	/**
	 * Click�̓��샂�[�h�B�f�t�H���g��development�ł��B
	 * <p>
	 * TODO mode�Ɏw��\�Ȓl��Javadoc�ɋL�q����
	 */
	public String mode = "development";
	
	/**
	 * ���ʃ��X�|���X�w�b�_�B
	 */
	public Map<String, String> headers = new HashMap<String, String>();
	
	/**
	 * ���P�[���B
	 */
	public String locale;
	
	/**
	 * �y�[�W�N���X�Ƀ��N�G�X�g�p�����[�^�������o�C���h���邩�ǂ����B
	 * �f�t�H���g��true�ł��B
	 */
	public boolean autoBinding = true;
	
	/**
	 * �R���g���[���Z�b�g���`�����ݒ�t�@�C���Q�̃p�X�B
	 */
	public List<String> controlSets = new ArrayList<String>();
	
	/**
	 * �f�v���C�i�t�@�C���̓W�J�j���K�v�ȃR���g���[���N���X�Q�B
	 */
	public List<Class<? extends Control>> controls = new ArrayList<Class<? extends Control>>();
	
	/**
	 * Click��FileField���g�p����Commons FileUpload��<code>FileItemFactory</code>�̃C���X�^���X�B
	 * �f�t�H���g��<code>org.apache.commons.fileupload.disk.DiskFileItemFactory</code>�ł��B
	 */
	public FileItemFactory fileItemFactory = new DiskFileItemFactory();
	
}
