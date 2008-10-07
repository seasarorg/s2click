package org.seasar.s2click.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.apache.commons.io.IOUtils;

public class ZipUtil {
	
	/**
	 * ファイル({@link InputStream}) を指定されたディレクトリに解凍する。
	 * 
	 * @param dir 解凍先ディレクトリ
	 * @param in ZIPファイルの入力ストリーム
	 * @throws IOException 入出力例外が発生した場合
	 */
	public static void unzip(File dir, InputStream in) throws IOException {

	    ZipInputStream zin = new ZipInputStream(in);
	    ZipEntry entry = null;
	    try {
	        while ((entry = zin.getNextEntry()) != null) {
	            try {
	                unzipEntry (entry, dir, zin);
	            } finally {
	                zin.closeEntry();
	            }
	        }
	    } finally {
	    	IOUtils.closeQuietly(zin);
	    }
	}

	private static final void unzipEntry(ZipEntry entry, File dir, ZipInputStream zin) throws IOException {
	    File f  = null;
	    if (entry.isDirectory()) {
	        // フォルダ
	        f = new File(dir, entry.getName());
	        if ( !f.exists() && !f.mkdirs() ) {
	            throw new IOException("ディレクトリの作成に失敗しました: " + f.getAbsolutePath());
	        }
	    } else {
	        // ファイル
	        f = new File(dir, entry.getName());
	        File parent = new File(f.getParent());
	        if ( !parent.exists() && !new File(f.getParent()).mkdirs()) {
	            throw new IOException("ディレクトリの作成に失敗しました: " + parent.getAbsolutePath());
	        }
	        FileOutputStream out = new FileOutputStream(f);
	        byte[] buf = new byte[1024 * 8];
	        int length = 0;
	        try {
	            while ((length = zin.read(buf)) != -1) {
	                out.write(buf, 0, length);
	            }
	        } finally {
	        	IOUtils.closeQuietly(out);
	        }
	    }
	}
}
