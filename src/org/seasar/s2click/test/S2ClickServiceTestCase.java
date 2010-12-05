/*
 * Copyright 2006-2010 the Seasar Foundation and the Others.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package org.seasar.s2click.test;

import java.io.File;
import java.lang.reflect.Method;
import java.lang.reflect.Type;

import javax.transaction.TransactionManager;

import org.apache.commons.lang.StringUtils;
import org.seasar.extension.dataset.DataSet;
import org.seasar.extension.dataset.impl.SqlReader;
import org.seasar.extension.jdbc.JdbcManager;
import org.seasar.framework.container.SingletonS2Container;
import org.seasar.framework.exception.ResourceNotFoundRuntimeException;
import org.seasar.framework.util.ClassUtil;
import org.seasar.framework.util.FileOutputStreamUtil;
import org.seasar.framework.util.ResourceUtil;
import org.seasar.framework.util.tiger.GenericUtil;

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
 *   <dt>Excelファイルの自動生成</dt>
 *   <dd>
 *     テストメソッドに{@link GenerateExcel}アノテーションを付与しておくことで、
 *     期待値として比較するためのExcelファイルを生成することができます。
 *     生成されるExcelファイルは<strong>テストクラス名_テストメソッド名_expect.xls</strong>というファイル名になります。
 *     生成されるのは初期データの自動投入直後になります。
 *     すでにExcelファイルが存在する場合はなにも行いません。<br>
 *     <code>GenerateExcel</code>アノテーションは主にデータベースの検索処理の結果を検証する際に使用するExcelファイルの生成に利用できます。
 *     アノテーションの記述によってExcel生成時の検索条件やソート順、対象カラムを指定できるので、検証したいデータに応じたExcelを生成することができます。
 *   </dd>
 *
 *   <dt>DBの内容とExcelをアノテーションで比較</dt>
 *   <dd>
 *     テストメソッドに{@link Assert}アノテーションを付与しておくことで、
 *     指定したテーブルのデータを<strong>テストクラス名_テストメソッド名_expect.xls</strong>
 *     という名前のExcelファイルと比較することができます。
 *     さらに、まだExcelファイルが存在しない場合はデータベースから生成されます。
 *     このときの生成のタイミングは<code>GenerateExcel</code>アノテーションで生成する場合と異なり、
 *     テストメソッドの実行直後になります。<br>
 *     <code>Assert</code>アノテーションは主にデータベースの更新処理を行った結果が正しいかどうかの検証に使用します。
 *   </dd>
 * </dl>
 * なお、Excelファイルの自動生成機能を使用するには{@link S2ClickTestConfig}をdiconファイルに登録し、
 * <code>sourceDir</code>プロパティにテストケースのソースフォルダを指定しておく必要があります。
 * テスト時のみ読み込むdiconファイルを用意し、以下のように記述を追加してください。
 * <pre>
 * &lt;component class="org.seasar.s2click.test.S2ClickTestConfig" instance="singleton"&gt;
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
	@SuppressWarnings({"unchecked", "rawtypes"})
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

        	// 事前に結果Excelを作成する
            Method method = getTargetMethod();
        	GenerateExcel annGenExcel = method.getAnnotation(GenerateExcel.class);
        	if(annGenExcel != null){
	            Table[] tables = annGenExcel.tables();
	            // Excelファイルがない場合は作成
	            createExcelFile(tables);
        	}

        	// テストを実行する
        	runTest();

        	// Assertアノテーションの処理
            Assert annAssert = method.getAnnotation(Assert.class);
            if(annAssert != null){
	            Table[] tables = annAssert.tables();
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

	/**
	 * 期待値Excelファイルを自動生成します。
	 *
	 * @param tables 生成するテーブルの情報
	 * @throws Exception Excelファイルの生成に失敗した場合
	 */
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

    	String packagePath = packageName.replace('.', '/');
    	String fileName = getClass().getSimpleName() + "_" + getTargetMethod().getName() + "_expect.xls";

    	File dir = new File(srcDir + "/" + packagePath);
    	File file = new File(dir, fileName);

    	if(file.exists()){
    		return;
    	}

    	SqlReader reader = new SqlReader(getDataSource());
    	for(int i=0;i<tables.length;i++){
    		reader.addTable(tables[i].name(), tables[i].where(), tables[i].orderBy());
    	}

    	DataSet result = reader.read();

    	// ソースパスに生成
    	XlsWriter writer = new XlsWriter(FileOutputStreamUtil.create(file));
    	for(int i=0;i<tables.length;i++){
    		writer.setIncludeColumns(tables[i].name(), tables[i].includeColumns());
    		writer.setExcludeColumns(tables[i].name(), tables[i].excludeColumns());
    	}
        writer.write(result);

        // 同じファイルをクラスパスにも生成
        File buildDir = ResourceUtil.getBuildDir(getClass());

        writer.setOutputStream(FileOutputStreamUtil.create(
        		new File(buildDir, convertPath(packagePath + "/" + fileName))));

        writer.write(result);
    }

}
