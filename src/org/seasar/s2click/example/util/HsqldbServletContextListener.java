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
package org.seasar.s2click.example.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class HsqldbServletContextListener implements ServletContextListener {
    
    private static final String DB_URL    = "jdbc:hsqldb:hsql://localhost:9001";
    private static final String DB_DRIVER = "org.hsqldb.jdbcDriver";
    private static final String DB_USER   = "sa";
    private static final String DB_PASS   = "";
    private static final String DATA_DIR  = "WEB-INF/data";
    
    public void contextInitialized(ServletContextEvent evt) {
        try {
            ServletContext context = evt.getServletContext();
            org.hsqldb.Server.main(new String[]{"-database",context.getRealPath(DATA_DIR + "/HSQLDB"),
                                                "-no_system_exit","true"});
        } catch(RuntimeException ex){
            throw ex;
        } catch(Exception ex){
            throw new RuntimeException(ex);
        }
    }
    
    public void contextDestroyed(ServletContextEvent evt) {
        try {
            Class.forName(DB_DRIVER);
            Connection conn = DriverManager.getConnection(DB_URL,DB_USER,DB_PASS);
            Statement stmt = conn.createStatement();
            stmt.executeUpdate("SHUTDOWN;");
            stmt.close();
            conn.close();
        } catch(RuntimeException ex){
            throw ex;
        } catch(Exception ex){
            throw new RuntimeException(ex);
        }
    }
}