package org.seasar.s2click.example.page;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.servlet.http.HttpServletRequest;

import net.sf.click.Context;
import net.sf.click.util.ClickUtils;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.seasar.s2click.S2ClickPage;
import org.seasar.s2click.annotation.Path;

/**
 * FCKeditorのファイルマネージャのサーバサイド側実装を提供します。
 * <p>
 * クイックアップロード（アップロードタブからのアップロード）はサポートしません。
 * 
 * <ul>
 *   <li>TODO 日本語のファイル名（LinuxでもOKなのか？）</li>
 *   <li>TODO IEの場合の動作確認</li>
 * </ul>
 * 
 * @author Naoki Takezoe
 */
@Path("/fckeditor/browser.htm")
public class FCKEditorBrowserPage extends S2ClickPage {
	
	private static final Logger logger = Logger.getLogger(FCKEditorBrowserPage.class);
	
	private static List<String> ALLOWED_COMMANDS = new CopyOnWriteArrayList<String>();
	static {
//		ALLOWED_COMMANDS.add("QuickUpload");
		ALLOWED_COMMANDS.add("FileUpload");
		ALLOWED_COMMANDS.add("GetFolders");
		ALLOWED_COMMANDS.add("GetFoldersAndFiles");
		ALLOWED_COMMANDS.add("CreateFolder");
	}
	
	private static List<String> ALLOWED_TYPES = new CopyOnWriteArrayList<String>();
	static {
		ALLOWED_TYPES.add("File");
		ALLOWED_TYPES.add("Image");
		ALLOWED_TYPES.add("Flash");
		ALLOWED_TYPES.add("Media");
	}
	
	/**
	 * TODO 設定で変更できるようにする？
	 */
	private static final String USER_FILES_PATH = "files/";
	
//	private static Map<String, String> QUICK_UPLOAD_PATH = new ConcurrentHashMap<String, String>();
//	static {
//		QUICK_UPLOAD_PATH.put("File", USER_FILES_PATH);
//		QUICK_UPLOAD_PATH.put("Image", USER_FILES_PATH);
//		QUICK_UPLOAD_PATH.put("Flash", USER_FILES_PATH);
//		QUICK_UPLOAD_PATH.put("Media", USER_FILES_PATH);
//	}
	
	private static Map<String, String> FILE_TYPES_PATH = new ConcurrentHashMap<String, String>();
	static {
		FILE_TYPES_PATH.put("File", USER_FILES_PATH + "file/");
		FILE_TYPES_PATH.put("Image", USER_FILES_PATH + "image/");
		FILE_TYPES_PATH.put("Flash", USER_FILES_PATH + "flash/");
		FILE_TYPES_PATH.put("Media", USER_FILES_PATH + "media/");
	}
	
	private static Map<String, ICommand> COMMANDS = new ConcurrentHashMap<String, ICommand>();
	static {
		COMMANDS.put("GetFolders", new GetFolderCommand());
		COMMANDS.put("GetFoldersAndFiles", new GetFoldersAndFilesCommand());
		COMMANDS.put("CreateFolder", new CreateFolderCommand());
	}
	
	@Override
	public String getPath(){
		return null;
	}
	
	@Override
	public void onRender(){
		String command = getContext().getRequestParameter("Command");
		String type = getContext().getRequestParameter("Type");
		String currentFolder = getContext().getRequestParameter("CurrentFolder");
		
		if(StringUtils.isEmpty(command) || StringUtils.isEmpty(type) || StringUtils.isEmpty(currentFolder)){
			String queryString = getContext().getRequest().getQueryString();
			if(StringUtils.isNotEmpty(queryString)){
				for(String item: queryString.split("&")){
					String[] dim = item.split("=");
					try {
						if(dim[0].equals("Command")){
							command = URLDecoder.decode(dim[1], "UTF-8");
						} else if(dim[0].equals("Type")){
							type = URLDecoder.decode(dim[1], "UTF-8");
						} else if(dim[0].equals("CurrentFolder")){
							currentFolder = URLDecoder.decode(dim[1], "UTF-8");
						}
					} catch(UnsupportedEncodingException ex){
						// ありえない
					}
				}
			}
//			if(StringUtils.isEmpty(command)){
//				command = "QuickUpload";
//				if(StringUtils.isEmpty(type)){
//					type = "File";
//				}
//				currentFolder = "/";
//			}
			
			if(StringUtils.isEmpty(command) || StringUtils.isEmpty(type) || StringUtils.isEmpty(currentFolder)){
				return;
			}
		}
		
		try {
			StringBuilder sb = new StringBuilder();
			currentFolder = getCurrentFolder(currentFolder);
			
			if(!ALLOWED_COMMANDS.contains(command)){
				throw new FCKEditorConnectorException(1, command + "は許可されていないコマンドです。");
			}
			if(!ALLOWED_TYPES.contains(type)){
				throw new FCKEditorConnectorException(1, "不正なタイプが指定されました。");
			}
			
			if(command.equals("FileUpload")){
				fileUpload(command, type, currentFolder, getContext());
				return;
			}
			
			createXmlHeader(sb, command, type, currentFolder, getContext());
			
			ICommand commandImpl = COMMANDS.get(command);
			if(commandImpl != null){
				commandImpl.execute(sb, type, currentFolder, getContext());
			}
			
			createXmlFooter(sb);
			
			setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
			setHeader("Pragma", "no-cache");
			renderResponse("text/xml; charset=UTF-8", sb.toString());
			
			if(logger.isTraceEnabled()){
				logger.trace(sb.toString());
			}
			
		} catch(FCKEditorConnectorException ex){
			sendError(ex);
		}
		
	}
	
	/**
	 * ファイルをアップロードします。
	 */
	private void fileUpload(String command, String type, String folderPath, Context context){
		int code = 0;
		String fileName = "";
		try {
			String root = context.getServletContext().getRealPath("/");
			String serverDir = serverMapFolder(type, folderPath, command, context);
			File dir = new File(root, serverDir);
			
			FileItem item = getContext().getFileItem("NewFile");
			fileName = item.getName(); // TODO IEだとフルパスで渡ってきてた気がする。
			
			File file = new File(dir, fileName);
			
			InputStream in = null;
			FileOutputStream out = null;
			try {
				in = item.getInputStream();
				out = new FileOutputStream(file);
				IOUtils.copy(in, out);
			} catch(IOException ex){
				throw new FCKEditorConnectorException(202, ex.toString());
			} finally {
				IOUtils.closeQuietly(in);
				IOUtils.closeQuietly(out);
			}
		} catch(FCKEditorConnectorException ex){
			code = ex.code;
		}
		
		String fileUrl = getUrlFromPath(type, combinePaths(folderPath, fileName), command, context);
		
		StringBuilder sb = new StringBuilder();
		sb.append("<script type=\"text/javascript\">");
		sb.append("(function(){var d=document.domain;while (true){try{var A=window.parent.document.domain;break;}catch(e) {};d=d.replace(/.*?(?:\\.|$)/,'');if (d.length==0) break;try{document.domain=d;}catch (e){break;}}})();");
		sb.append("window.parent.OnUploadCompleted(").append(code).append(",'").append(fileUrl).append("','").append(fileName).append("', '');");
		sb.append("</script>");
		
		renderResponse("text/javascript", sb.toString());
		
		if(logger.isTraceEnabled()){
			logger.trace(sb.toString());
		}
	}
	
	private void renderResponse(String contentType, String source){
		try {
			renderResponse(contentType, new ByteArrayInputStream(source.getBytes("UTF-8")));
		} catch(UnsupportedEncodingException ex){
			// ありえない
		}
	}
	
	private static void createXmlHeader(StringBuilder sb, String command, String type, String currentFolder, Context context){
		sb.append("<?xml version=\"1.0\" encoding=\"utf-8\" ?>\n");
		sb.append("<Connector command=\"").append(command).append("\" resourceType=\"").append(type).append("\">\n");
		
		sb.append("<CurrentFolder path=\"").append(ClickUtils.escapeHtml(currentFolder))
				.append("\" url=\"").append(ClickUtils.escapeHtml(getUrlFromPath(type, currentFolder, command, context))).append("\" />\n");
	}
	
	private static String getUrlFromPath(String type, String folderPath, String command, Context context){
		HttpServletRequest request = context.getRequest();
		String contextPath = request.getContextPath();
		
		return contextPath + "/" + combinePaths(getResourceTypePath(type, command), folderPath) ;
	}
	
	private static String getResourceTypePath(String type, String command){
//		if(command.equals("QuickUpload")){
//			return QUICK_UPLOAD_PATH.get(type);
//		} else {
			return FILE_TYPES_PATH.get(type);
//		}
	}
	
	private static String removeFromStart(String sourceString, String charToRemove){
		return sourceString.replaceFirst("^" + charToRemove + "+", "");
	}
	
	private static String removeFromEnd(String sourceString, String charToRemove){
		return sourceString.replaceFirst(charToRemove + "$", "");
	}
	
	private static String combinePaths(String basePath, String folder){
		return removeFromEnd(basePath, "/") + "/" + removeFromStart(folder, "/") ;
	}

	private static void createXmlFooter(StringBuilder sb){
		sb.append("</Connector>\n");
	}
	
	private static String getCurrentFolder(String currentFolder){
		if(StringUtils.isEmpty(currentFolder)){
			currentFolder = "/";
		}
		if(!currentFolder.endsWith("/")){
			currentFolder = currentFolder + "/";
		}
		if(!currentFolder.startsWith("/")){
			currentFolder = "/" + currentFolder;
		}
		currentFolder = currentFolder.replaceAll("//", "/");
		if(currentFolder.indexOf("..") >= 0 || currentFolder.indexOf("\\") >= 0){
			throw new FCKEditorConnectorException(102, "");
		}
		
		return currentFolder;
	}
	
	/**
	 * エラー電文を返却します。
	 * 
	 * @param ex 例外
	 */
	private void sendError(FCKEditorConnectorException ex){
		setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
		setHeader("Pragma", "no-cache");
		
		StringBuilder sb = new StringBuilder();
		sb.append("<?xml version=\"1.0\" encoding=\"utf-8\" ?>\n");
		sb.append("<Connector>\n");
		sb.append("<Error number=\"").append(ex.code).append("\" text=\"").append(ClickUtils.escapeHtml(ex.message)).append("\" />\n");
		sb.append("</Connector>\n");
		
		renderResponse("text/xml; charset=UTF-8", sb.toString());
	}
	
	private static String serverMapFolder(String type, String folderPath, String command, Context context){
		// Get the resource type directory.
		String resourceTypePath = getResourceTypePath(type, command);

		// Ensure that the directory exists.
		String errorMessage = createServerFolder(resourceTypePath, context);
		if (StringUtils.isNotEmpty(errorMessage)){
			throw new FCKEditorConnectorException(1, "");
		}

		// Return the resource type directory combined with the required path.
		return combinePaths(resourceTypePath , folderPath) ;
	}
	
	private static String createServerFolder(String folderPath, Context context){
		String root = context.getServletContext().getRealPath("/");
		File dir = new File(root, folderPath);
		if(!dir.exists()){
			try {
				FileUtils.forceMkdir(dir);
			} catch(IOException ex){
				return ex.toString();
			}
		}
		
		return "";
	}
	
	private static class FCKEditorConnectorException extends RuntimeException {
		
		private static final long serialVersionUID = 1L;
		
		public int code;
		public String message;
		
		public FCKEditorConnectorException(int code, String message){
			this.code = code;
			this.message = message;
		}
	}
	
	/**
	 * ファイルブラウザの各コマンドのインターフェース。
	 */
	private static interface ICommand {
		public void execute(StringBuilder sb, String type, String currentFolder, Context context);
	}
	
	private static class GetFolderCommand implements ICommand {
		public void execute(StringBuilder sb, String type, String currentFolder, Context context) {
			String serverDir = serverMapFolder(type, currentFolder, "GetFolders", context);
			
			String root = context.getServletContext().getRealPath("/");
			File dir = new File(root, serverDir);
			
			sb.append("<Folders>\n");
			for(File file: dir.listFiles()){
				if(file.isDirectory()){
					sb.append("<Folder name=\"").append(ClickUtils.escapeHtml(file.getName())).append("\" />\n");
				}
			}
			sb.append("</Folders>\n");
		}
		
	}
	
	private static class GetFoldersAndFilesCommand implements ICommand {
		public void execute(StringBuilder sb, String type, String currentFolder, Context context) {
			String serverDir = serverMapFolder(type, currentFolder, "GetFolders", context);
			
			String root = context.getServletContext().getRealPath("/");
			File dir = new File(root, serverDir);
			
			sb.append("<Folders>\n");
			for(File file: dir.listFiles()){
				if(file.isDirectory()){
					sb.append("<Folder name=\"").append(ClickUtils.escapeHtml(file.getName())).append("\" />\n");
				}
			}
			sb.append("</Folders>\n");
			sb.append("<Files>\n");
			for(File file: dir.listFiles()){
				if(file.isFile()){
					sb.append("<File name=\"").append(ClickUtils.escapeHtml(file.getName())).append("\" size=\"").append(file.length()).append("\" />\n");
				}
			}
			sb.append("</Files>\n");
		}
	}
	
	private static class CreateFolderCommand implements ICommand {
		public void execute(StringBuilder sb, String type, String currentFolder, Context context) {
			String serverDir = serverMapFolder(type, currentFolder, "GetFolders", context);
			
			String root = context.getServletContext().getRealPath("/");
			File dir = new File(root, serverDir);
			
			String newFolderName = context.getRequestParameter("NewFolderName");
			File newDir = new File(dir, newFolderName);
			
			if(!newDir.mkdir()){
				throw new FCKEditorConnectorException(110, newFolderName + "の作成に失敗しました。");
			}
		}
	}
}
