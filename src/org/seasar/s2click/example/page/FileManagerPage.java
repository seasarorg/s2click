package org.seasar.s2click.example.page;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import net.sf.click.Context;
import net.sf.click.util.ClickUtils;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.seasar.s2click.S2ClickPage;
import org.seasar.s2click.annotation.Path;

@Path("/fckeditor/filemanager.htm")
public class FileManagerPage extends S2ClickPage {
	
//	@Request("Command")
//	public String command;
//	
//	@Request("Type")
//	public String type;
//	
//	@Request("CurrentFolder")
//	public String currentFolder;
	
	private static List<String> ALLOWED_COMMANDS = new CopyOnWriteArrayList<String>();
	static {
		ALLOWED_COMMANDS.add("QuickUpload");
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
	
	private static final String USER_FILES_PATH = "files/";
	
	private static Map<String, String> QUICK_UPLOAD_PATH = new ConcurrentHashMap<String, String>();
	static {
		QUICK_UPLOAD_PATH.put("File", USER_FILES_PATH);
		QUICK_UPLOAD_PATH.put("Image", USER_FILES_PATH);
		QUICK_UPLOAD_PATH.put("Flash", USER_FILES_PATH);
		QUICK_UPLOAD_PATH.put("Media", USER_FILES_PATH);
	}
	
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
		
		Context context = getContext();
		System.out.println(context.getFileItem("Command"));
		
		if(StringUtils.isEmpty(command) || StringUtils.isEmpty(type) || StringUtils.isEmpty(currentFolder)){
			return;
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
				fileUpload();
			}
			
			createXmlHeader(sb, command, type, currentFolder);
			
			ICommand commandImpl = COMMANDS.get(command);
			if(commandImpl != null){
				commandImpl.execute(sb, type, currentFolder, getContext());
			}
			
			createXmlFooter(sb);
			
			setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
			setHeader("Pragma", "no-cache");
			renderResponse("text/xml; charset=UTF-8", new ByteArrayInputStream(sb.toString().getBytes("UTF-8")));
			
		} catch(FCKEditorConnectorException ex){
			sendError(ex);
		} catch(UnsupportedEncodingException ex){
			// ありえない
		}
		
	}
	
	private void fileUpload(){
		FileItem item = getContext().getFileItem("NewFile");
		System.out.println("** ファイルアップロード **");
		System.out.println(item.getName());
		System.out.println(item.getFieldName());
	}
	
	private void createXmlHeader(StringBuilder sb, String command, String type, String currentFolder){
		sb.append("<?xml version=\"1.0\" encoding=\"utf-8\" ?>\n");
		sb.append("<Connector command=\"").append(command).append("\" resourceType=\"").append(type).append("\">\n");
		
		sb.append("<CurrentFolder path=\"").append(ClickUtils.escapeHtml(currentFolder))
				.append("\" url=\"").append(ClickUtils.escapeHtml(getUrlFromPath(type, currentFolder, command))).append("\" />\n");
	}
	
	private String getUrlFromPath(String type, String folderPath, String command){
		return combinePaths(getResourceTypePath(type, command), folderPath) ;
	}
	
	private static String getResourceTypePath(String type, String command){
		if(command.equals("QuickUpload")){
			return QUICK_UPLOAD_PATH.get(type);
		} else {
			return FILE_TYPES_PATH.get(type);
		}
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

	private void createXmlFooter(StringBuilder sb){
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
	
	private void sendError(FCKEditorConnectorException ex){
		setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
		setHeader("Pragma", "no-cache");
		
		StringBuilder sb = new StringBuilder();
		sb.append("<?xml version=\"1.0\" encoding=\"utf-8\" ?>\n");
		sb.append("<Connector>\n");
		sb.append("<Error number=\"").append(ex.code).append("\" text=\"").append(ClickUtils.escapeHtml(ex.message)).append("\" />\n");
		sb.append("</Connector>\n");
		
		try {
			renderResponse("text/xml; charset=UTF-8", new ByteArrayInputStream(sb.toString().getBytes("UTF-8")));
		} catch(UnsupportedEncodingException e){
			// ありえない
		}
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
		String root = context.getServletContext().getRealPath("WEB-INF");
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
			
			String root = context.getServletContext().getRealPath("WEB-INF");
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
			
			String root = context.getServletContext().getRealPath("WEB-INF");
			File dir = new File(root, serverDir);
			
			sb.append("<Folders>\n");
			for(File file: dir.listFiles()){
				if(file.isDirectory()){
					sb.append("<Folder name=\"").append(ClickUtils.escapeHtml(file.getName())).append("\" />\n");
				} else if(file.isFile()){
					sb.append("<File name=\"").append(ClickUtils.escapeHtml(file.getName())).append("\" size=\"").append(file.length()).append("\"/>\n");
				}
			}
			sb.append("</Folders>\n");
		}
	}
	
	private static class CreateFolderCommand implements ICommand {
		public void execute(StringBuilder sb, String type, String currentFolder, Context context) {
			String serverDir = serverMapFolder(type, currentFolder, "GetFolders", context);
			
			String root = context.getServletContext().getRealPath("WEB-INF");
			File dir = new File(root, serverDir);
			
			String newFolderName = context.getRequestParameter("NewFolderName");
			File newDir = new File(dir, newFolderName);
			
			if(!newDir.mkdir()){
				throw new FCKEditorConnectorException(110, newFolderName + "の作成に失敗しました。");
			}
			
//			sb.append("<Folders>\n");
//			for(File file: dir.listFiles()){
//				if(file.isDirectory()){
//					sb.append("<Folder name=\"").append(ClickUtils.escapeHtml(file.getName())).append("\" />\n");
//				} else if(file.isFile()){
//					sb.append("<File name=\"").append(ClickUtils.escapeHtml(file.getName())).append("\" size=\"").append(file.length()).append("\"/>\n");
//				}
//			}
//			sb.append("</Folders>\n");
		}
	}
}
