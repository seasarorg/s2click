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