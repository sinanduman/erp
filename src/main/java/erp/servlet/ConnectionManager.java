package erp.servlet;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;
import java.io.Serializable;
import java.sql.Connection;

public class ConnectionManager implements Serializable {
    private static final Logger logger = LoggerFactory.getLogger(ConnectionManager.class);

    private Connection conn;
    private DataSource ds;

    @PostConstruct
    private void connectionReady() {
        Context ctx;
        try {
            ctx = new InitialContext();
            ds = (DataSource) ctx.lookup("java:/comp/env/jdbc/postgres");
            if (ds == null)
                throw new RuntimeException("Data source not found!");
        } catch (Throwable e) {
            logger.error(ExceptionUtils.getStackTrace(e));
        }
    }

    public synchronized Connection getConnection() {
        if (conn == null) {
            try {
                conn = ds.getConnection();
            } catch (Exception e) {
                logger.error(ExceptionUtils.getStackTrace(e));
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
        } catch (Exception e) {
            logger.error(ExceptionUtils.getStackTrace(e));
        }
        return conn;
    }
}
