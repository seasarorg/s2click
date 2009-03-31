package org.seasar.s2click;

import java.io.File;
import java.io.FileOutputStream;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.transaction.TransactionManager;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.seasar.extension.dataset.DataSet;
import org.seasar.extension.jdbc.JdbcContext;
import org.seasar.extension.jdbc.JdbcManager;
import org.seasar.extension.jdbc.manager.JdbcManagerImplementor;
import org.seasar.framework.container.SingletonS2Container;
import org.seasar.framework.util.ClassUtil;
import org.seasar.framework.util.tiger.GenericUtil;
import org.seasar.s2click.Assert.Table;

/**
 * サービスクラスのテストケースの抽象基底クラスです。
 * 
 * 
 * @author Naoki Takezoe
 *
 * @param <T> テスト対象のサービスクラス
 */
public abstract class S2ClickServiceTestCase<T> extends S2ClickTestCase {
	
	/**
	 * テスト対象のサービスクラスのインスタンス。
	 */
	protected T service;
	
	protected JdbcManager jdbcManager;
	
	protected S2ClickTestConfig config;
	
	@Override
	@SuppressWarnings("unchecked")
	protected void setUpAfterContainerInit() throws Throwable {
		super.setUpAfterContainerInit();
		
		Type type = GenericUtil.getTypeVariableMap(getClass())
			.get(S2ClickServiceTestCase.class.getTypeParameters()[0]);

		service = (T) SingletonS2Container.getComponent((Class) type);
	}
	
    protected void doRunTest() throws Throwable {
        TransactionManager tm = null;
        if (needTransaction()) {
            try {
                tm = (TransactionManager) getComponent(TransactionManager.class);
                tm.begin();
            } catch (Throwable t) {
                System.err.println(t);
            }
        }
        try {
        	runTest();
            
            Method method = getTargetMethod();
            Assert ann = method.getAnnotation(Assert.class);
            if(ann != null){
	            Table[] tables = ann.tables();
	            createExcelFile(tables);
	            DataSet ds = getExpectDataSet();
	            for(Table table: tables){
	            	String tableName = table.name();
	            	assertEquals(ds.getTable(tableName), readDbByTable(tableName));
	            }
            }
            
            
        } finally {
            if (tm != null) {
                tm.rollback();
            }
        }
    }
    
	protected DataSet getInitDataSet() {
        return readXls(getClass().getSimpleName() + "_" + getTargetMethod().getName() + "_data.xls");
	}
	
	protected DataSet getExpectDataSet() {
        return readXls(getClass().getSimpleName() + "_" + getTargetMethod().getName() + "_expect.xls");
	}
    
    private void createExcelFile(Table[] tables) throws Exception {
    	String packageName = ClassUtil.getPackageName(getClass());
    	
    	String srcDir = config.sourceDir;
    	File dir = new File(srcDir + "/" + packageName.replace('.', '/'));
    	
    	File file = new File(dir, getClass().getSimpleName() + "_" + getTargetMethod().getName() + "_expect.xls");
    	if(file.exists()){
    		return;
    	}
    	
    	JdbcContext context = ((JdbcManagerImplementor) jdbcManager).getJdbcContext();
    	
		HSSFWorkbook wb = new HSSFWorkbook();
		
    	for(int i=0;i<tables.length;i++){
    		HSSFSheet sheet = wb.createSheet();
    		wb.setSheetName(i, tables[i].name());
    		
	    	PreparedStatement stmt = null;
	    	ResultSet rs = null;
	    	try {
		    	stmt = context.getPreparedStatement("SELECT * FROM " + tables[i].name());
		    	rs = stmt.executeQuery();
		    	
		    	ResultSetMetaData md = rs.getMetaData();
		    	int columnCount = md.getColumnCount();
		    	HSSFRow row = sheet.createRow(0);
		    	
		    	List<String> columnNames = new ArrayList<String>();
		    	for(int j = 1; j <= columnCount; j++){
		    		String columnName = md.getColumnName(j);
		    		
		    		if(tables[i].includeColumns().length > 0 &&
		    				Arrays.binarySearch(tables[i].includeColumns(), columnName) == -1){
		    			continue;
		    		}
		    		if(tables[i].excludeColumns().length > 0 &&
		    				Arrays.binarySearch(tables[i].excludeColumns(), columnName) != -1){
		    			continue;
		    		}
		    		
		    		HSSFCell cell = row.createCell((short) (j -1));
		    		cell.setCellValue(new HSSFRichTextString(columnName));
		    		columnNames.add(columnName);
		    	}
		    	
		    	int rowCount = 1;
		    	while(rs.next()){
			    	HSSFRow dataRow = sheet.createRow(rowCount);
			    	for(int j = 0; j < columnNames.size(); j++){
			    		HSSFCell cell = dataRow.createCell((short) j);
			    		String value = rs.getString(columnNames.get(j));
			    		if(value != null){
			    			cell.setCellValue(new HSSFRichTextString(value));
			    		}
			    	}
			    	rowCount++;
		    	}
		    	
	    	} finally {
	    		if(rs != null){
	    			try {
	    				rs.close();
	    			} catch(Exception ex){
	    				;
	    			}
	    		}
	    		if(stmt != null){
	    			try {
	    				stmt.close();
	    			} catch(Exception ex){
	    				;
	    			}
	    		}
	    	}
    	}
    	
   		file.createNewFile();
    	FileOutputStream out = new FileOutputStream(file);
    	wb.write(out);
    	out.close();
    }
	
}
