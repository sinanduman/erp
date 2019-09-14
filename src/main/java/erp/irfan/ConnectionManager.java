package erp.irfan;

import javax.annotation.PostConstruct;
import javax.inject.Singleton;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.SQLException;

public class ConnectionManager implements Serializable {

    private Connection conn = null;
    private DataSource ds;

    @PostConstruct
    private void connectionReady() {
        Context ctx = null;
        try {
            /*
            String hostName = "localhost";
            String port = "5432";
            String userName = "postgres";
            String password = "postgres";
            String dbname = "erp";
            Class.forName("org.postgresql.Driver");
            String connectionURL = "jdbc:postgresql://" + hostName + ":" + port + "/" + dbname;
            conn = DriverManager.getConnection(connectionURL, userName, password);
             */
            ctx = new InitialContext();
            ds = (DataSource) ctx.lookup("java:/comp/env/jdbc/postgres");
            if (ds == null)
                throw new Exception("Data source not found!");
        } catch (Throwable ex) {
            ex.printStackTrace();
        }
    }

    public synchronized Connection getConnection() {
        if (conn == null) {
            try {
                conn = ds.getConnection();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        conn = reGainConnection();
        return conn;
    }

    public Connection reGainConnection() {
        try {
            if (conn.isClosed()) {
                connectionReady();
                conn = ds.getConnection();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return conn;
    }
}
