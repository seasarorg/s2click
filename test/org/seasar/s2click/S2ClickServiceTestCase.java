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

import org.apache.commons.lang.StringUtils;
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
import org.seasar.framework.exception.ResourceNotFoundRuntimeException;
import org.seasar.framework.util.ClassUtil;
import org.seasar.framework.util.tiger.GenericUtil;
import org.seasar.s2click.Assert.Table;

/**
 * サービスクラスのテストケースの抽象基底クラスです。
 * <code>S2TestCase</code>の機能に加え、さらにユニットテストを省力化するために以下のような機能を提供します。
 * 
 * <dl>
 *   <dt>初期データの自動投入</dt>
 *   <dd>
 *     <strong>テストクラス名_テストメソッド名_data.xls</strong> という名前のExcelファイルが
 *     テストクラスと同じパッケージに存在する場合、そのデータを自動的にデータベースに投入します。
 *   </dd>
 *   
 *   <dt>DBの内容とExcelをアノテーションで比較</dt>
 *   <dd>
 *     テストメソッドに{@link Assert}アノテーションを付与しておくことで、
 *     指定したテーブルのデータを<strong>テストクラス名_テストメソッド名_expect.xls</strong>
 *     という名前のExcelファイルと比較することができます。
 *     さらに、まだExcelファイルが存在しない場合はデータベースから生成されます。
 *   </dd>
 * </dl>
 * なお、Excelファイルの自動生成機能を使用するには{@link S2ClickTestConfig}をdiconファイルに登録し、
 * <code>sourceDir</code>プロパティにテストケースのソースフォルダを指定しておく必要があります。
 * テスト時のみ読み込むdiconファイルを用意し、以下のように記述を追加してください。
 * <pre>
 * &lt;component class="org.seasar.s2click.S2ClickTestConfig" instance="singleton"&gt;
 *   &lt;property name="sourceDir"&gt;"test"&lt;/property&gt;
 * &lt;/component&gt; </pre>
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
        	try {
        		// 初期データを投入する
        		readXlsAllReplaceDb(getClass().getSimpleName() + "_" + getTargetMethod().getName() + "_data.xls");
        	} catch(ResourceNotFoundRuntimeException ex){
        		// ファイルがない場合は無視する
        	}
        	
        	// テストを実行する
        	runTest();
            
        	// Assertアノテーションの処理
            Method method = getTargetMethod();
            Assert ann = method.getAnnotation(Assert.class);
            if(ann != null){
	            Table[] tables = ann.tables();
	            // Excelファイルがない場合は作成
	            createExcelFile(tables);
	            // 結果を比較
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
    
//	protected DataSet getInitDataSet() {
//        return readXls(getClass().getSimpleName() + "_" + getTargetMethod().getName() + "_data.xls");
//	}
	
    /**
     * 現在実行中のテストメソッドに対応した期待値のExcelファイルから<code>DataSet</code>を作成します。
     * Excelファイルが存在しない場合は<code>ResourceNotFoundRuntimeException</code>が発生します。
     */
	protected DataSet getExpectDataSet() {
        return readXls(getClass().getSimpleName() + "_" + getTargetMethod().getName() + "_expect.xls");
	}
    
    private void createExcelFile(Table[] tables) throws Exception {
    	if(config == null){
    		System.err.println(
    			"S2ClickTestConfigがS2Containerに登録されていません。Excelファイルの生成をスキップします。");
    		return;
    	}
    	if(StringUtils.isEmpty(config.sourceDir)){
    		System.err.println(
    			"S2ClickTestConfigのsourceDirプロパティが設定されていません。Excelファイルの生成をスキップします。");
    		return;
    	}
    	
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
