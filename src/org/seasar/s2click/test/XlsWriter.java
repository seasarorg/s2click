package org.seasar.s2click.test;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.seasar.extension.dataset.DataRow;
import org.seasar.extension.dataset.DataSet;
import org.seasar.extension.dataset.DataSetConstants;
import org.seasar.extension.dataset.DataTable;
import org.seasar.extension.dataset.DataWriter;
import org.seasar.framework.exception.IORuntimeException;
import org.seasar.framework.util.Base64Util;
import org.seasar.framework.util.FileOutputStreamUtil;
import org.seasar.framework.util.ResourceUtil;
import org.seasar.framework.util.StringConversionUtil;

/**
 * S2TestCaseの<code>XlsWriter</code>を拡張し、書き出すカラムを制御できるようにしたものです。
 * <p>
 * {@link #setIncludeColumns(String, String[])}を指定すると、指定したカラムのみ書き出されます。
 * {@link #setExcludeColumns(String, String[])}を指定すると、指定したカラムが書き出されなくなります。
 * 
 * @author higa
 * @author azusa
 * @author Naoki Takezoe
 */
public class XlsWriter implements DataWriter, DataSetConstants {

    /**
     * 出力ストリームです。
     */
    protected OutputStream out;

    /**
     * ワークブックです。
     */
    protected HSSFWorkbook workbook;

    /**
     * 日付用のスタイルです。
     */
    protected HSSFCellStyle dateStyle;

    /**
     * Base64用のスタイルです。
     */
    protected HSSFCellStyle base64Style;
    
    protected Map<String, String[]> includeColumns = new HashMap<String, String[]>();
    
    protected Map<String, String[]> excludeColumns = new HashMap<String, String[]>();

    /**
     * {@link XlsWriter}を作成します。
     * 
     * @param path
     *            パス
     */
    public XlsWriter(String path) {
        this(new File(ResourceUtil.getResourceAsFile("."), path));
    }

    /**
     * {@link XlsWriter}を作成します。
     * 
     * @param dirName
     *            ディレクトリ名
     * @param fileName
     *            ファイル名
     */
    public XlsWriter(String dirName, String fileName) {
        this(ResourceUtil.getResourceAsFile(dirName), fileName);
    }

    /**
     * {@link XlsWriter}を作成します。
     * 
     * @param dir
     *            ディレクトリ
     * @param fileName
     *            ファイル名
     */
    public XlsWriter(File dir, String fileName) {
        this(new File(dir, fileName));
    }

    /**
     * {@link XlsWriter}を作成します。
     * 
     * @param file
     *            ファイル
     */
    public XlsWriter(File file) {
        this(FileOutputStreamUtil.create(file));
    }

    /**
     * {@link XlsWriter}を作成します。
     * 
     * @param out
     *            出力ストリーム
     */
    public XlsWriter(OutputStream out) {
        setOutputStream(out);
    }

    /**
     * 出力ストリームを設定します。
     * 
     * @param out
     *            出力ストリーム
     */
    public void setOutputStream(OutputStream out) {
        this.out = out;
        workbook = new HSSFWorkbook();
        HSSFDataFormat df = workbook.createDataFormat();
        dateStyle = workbook.createCellStyle();
        dateStyle.setDataFormat(df.getFormat(DATE_FORMAT));
        base64Style = workbook.createCellStyle();
        base64Style.setDataFormat(df.getFormat(BASE64_FORMAT));
    }
    
    /**
	 * 書き出すカラム名を指定します。
	 * 
	 * @param tableName テーブル名
	 * @param includeColumns 書き出すカラム名の配列
	 */
	public void setIncludeColumns(String tableName, String[] includeColumns) {
		// 大文字に変換して格納
		String[] newArray = new String[includeColumns.length];
		for (int i = 0; i < includeColumns.length; i++) {
			newArray[i] = includeColumns[i].toUpperCase();
		}
		this.includeColumns.put(tableName.toUpperCase(), newArray);
	}

    /**
	 * 書き出さないカラム名を指定します。
	 * 
	 * @param tableName テーブル名
	 * @param excludeColumns 書き出さないカラム名の配列
	 */
	public void setExcludeColumns(String tableName, String[] excludeColumns) {
		// 大文字に変換して格納
		String[] newArray = new String[excludeColumns.length];
		for (int i = 0; i < excludeColumns.length; i++) {
			newArray[i] = excludeColumns[i].toUpperCase();
		}

		this.excludeColumns.put(tableName.toUpperCase(), newArray);
	}
	
	private static boolean containsColumn(String[] columnNames, String searchName){
		searchName = searchName.toUpperCase();
		
		for(String columnName: columnNames){
			if(columnName.equals(searchName)){
				return true;
			}
		}
		return false;
	}
	
	public void write(DataSet dataSet) {
        for (int i = 0; i < dataSet.getTableSize(); ++i) {
            DataTable table = dataSet.getTable(i);
            
            String[] includeColumns = this.includeColumns.get(table.getTableName().toUpperCase());
            String[] excludeColumns = this.excludeColumns.get(table.getTableName().toUpperCase());
            
            HSSFSheet sheet = workbook.createSheet();
            workbook.setSheetName(i, table.getTableName());
            HSSFRow headerRow = sheet.createRow(0);
            
            int count = 0;
            
            for (int j = 0; j < table.getColumnSize(); ++j) {
            	
            	if(includeColumns != null && includeColumns.length > 0){
            		String columnName = table.getColumnName(j);
            		if(!containsColumn(includeColumns, columnName)){
            			continue;
            		}
            	} else if(excludeColumns != null && excludeColumns.length > 0){
            		String columnName = table.getColumnName(j);
            		if(containsColumn(excludeColumns, columnName)){
            			continue;
            		}
            	}
            	
                HSSFCell cell = headerRow.createCell((short) count);
                cell.setCellValue(new HSSFRichTextString(table .getColumnName(j)));
                
                count++;
            }
            
            for (int j = 0; j < table.getRowSize(); ++j) {
                HSSFRow row = sheet.createRow(j + 1);
                
                count = 0;
                
                for (int k = 0; k < table.getColumnSize(); ++k) {
                	if(includeColumns != null && includeColumns.length > 0){
                		String columnName = table.getColumnName(k);
                		if(!containsColumn(includeColumns, columnName)){
                			continue;
                		}
                	} else if(excludeColumns != null && excludeColumns.length > 0){
                		String columnName = table.getColumnName(k);
                		if(containsColumn(excludeColumns, columnName)){
                			continue;
                		}
                	}
                	
                    DataRow dataRow = table.getRow(j);
                    Object value = dataRow.getValue(k);
                    if (value != null) {
                        HSSFCell cell = row.createCell((short) count);
                        setValue(cell, value);
                    }
                    
                    count++;
                }
            }
        }
        try {
            workbook.write(out);
            out.flush();
            out.close();
        } catch (IOException ex) {
            throw new IORuntimeException(ex);
        }
    }

    /**
     * セルに値を設定します。
     * 
     * @param cell
     *            セル
     * @param value
     *            値
     */
    protected void setValue(HSSFCell cell, Object value) {
        if (value instanceof Number) {
            cell.setCellValue(new HSSFRichTextString(value.toString()));
        } else if (value instanceof Date) {
            cell.setCellValue((Date) value);
            cell.setCellStyle(dateStyle);
        } else if (value instanceof byte[]) {
            cell.setCellValue(new HSSFRichTextString(Base64Util
                    .encode((byte[]) value)));
            cell.setCellStyle(base64Style);
        } else if (value instanceof Boolean) {
            cell.setCellValue(((Boolean) value).booleanValue());
        } else {
            cell.setCellValue(new HSSFRichTextString(StringConversionUtil
                    .toString(value, null)));
        }
    }
}

