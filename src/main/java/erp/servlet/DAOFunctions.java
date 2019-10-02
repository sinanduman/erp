package erp.servlet;

import erp.dao.BaseDao;
import erp.entity.Birim;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DAOFunctions implements Serializable {
    @Inject
    ConnectionManager connectionManager;
    private static final Logger logger = LoggerFactory.getLogger(DAOFunctions.class);

    public List<Birim> birimListeGetirTum() {
        List<Birim> temp = new ArrayList<Birim>();
        String searchQuery = "select * from " + BaseDao.BIRIM + " order by ad ";
        // connect to DB
        ResultSet rs = null;
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(searchQuery)) {
            rs = pstmt.executeQuery();
            while (rs.next()) {
                temp.add(new Birim(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return temp;
    }

    public int gkrnoGetir() {
        int result = -1;
        String selectQuery = "select irfan.gkrno_next()";
        logger.info(selectQuery);

        // connect to DB
        ResultSet rs = null;
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(selectQuery)) {
            logger.info(pstmt.toString());
            rs = pstmt.executeQuery();

            if (rs.next()) {
                result = rs.getInt(1);
            }
        } catch (SQLException e) {
            logger.error("Error: " + e);
        }
        return result;
    }

    public int recordCount(String tablename, String filter) {
        int result = 0;
        String selectQuery = "select count(*) from " + tablename + filter;
        logger.info(selectQuery);

        // connect to DB
        ResultSet rs = null;
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(selectQuery)) {
            rs = pstmt.executeQuery();

            if (rs.next()) {
                result = rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public int recordAgg(String tablename, String aggfunc, String aggfield, String filter) {
        int result = 0;
        String selectQuery = "select " + aggfunc + "(" + aggfield + ")" + " from " + tablename + filter;
        logger.info(selectQuery);
        // connect to DB
        ResultSet rs = null;
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(selectQuery)) {
            rs = pstmt.executeQuery();
            if (rs.next()) {
                result = rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }
}