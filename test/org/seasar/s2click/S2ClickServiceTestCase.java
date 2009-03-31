package org.seasar.s2click;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;

import javax.transaction.TransactionManager;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.seasar.extension.jdbc.JdbcContext;
import org.seasar.extension.jdbc.JdbcManager;
import org.seasar.extension.jdbc.manager.JdbcManagerImplementor;
import org.seasar.framework.container.SingletonS2Container;
import org.seasar.framework.util.tiger.GenericUtil;

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
            // TODO ここでキャプチャ
        	
            super.doRunTest();
            
            // TODO ここでキャプチャ
            Method method = getTargetMethod();
            Assert ann = method.getAnnotation(Assert.class);
            String[] tables = ann.tables();
            
        } finally {
            if (tm != null) {
                tm.rollback();
            }
        }
    }
    
    private void createExcelFile(String[] tables) throws Exception {
    	JdbcContext context = ((JdbcManagerImplementor) jdbcManager).getJdbcContext();
    	
		HSSFWorkbook wb = new HSSFWorkbook();
		
    	for(int i=0;i<tables.length;i++){
    		HSSFSheet sheet = wb.createSheet();
    		wb.setSheetName(i, tables[i]);
    		
	    	PreparedStatement stmt = null;
	    	ResultSet rs = null;
	    	try {
		    	stmt = context.getPreparedStatement("SELECT * FROM " + tables[i]);
		    	rs = stmt.executeQuery();
		    	
		    	ResultSetMetaData md = rs.getMetaData();
		    	int columnCount = md.getColumnCount();
		    	HSSFRow row = sheet.createRow(0);
		    	
		    	for(int j = 1; j <= columnCount; j++){
		    		String columnName = md.getColumnName(j);
		    		HSSFCell cell = row.createCell((short) (j -1));
		    		cell.setCellValue(new HSSFRichTextString(columnName));
		    	}
		    	
		    	int rowCount = 1;
		    	while(rs.next()){
			    	HSSFRow dataRow = sheet.createRow(rowCount);
			    	for(int j = 1; j <= columnCount; j++){
			    		HSSFCell cell = dataRow.createCell((short) (j - 1));
			    		String value = rs.getString(md.getColumnName(j));
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
    }
	
}
