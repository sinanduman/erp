package erp.servlet;

import erp.dao.BaseDao;
import erp.entity.Birim;
import erp.util.Constants;
import erp.util.Fields;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class DAOFunctions implements Serializable {
    @Inject
    private ConnectionManager connectionManager;
    private static final Logger logger = LoggerFactory.getLogger(DAOFunctions.class);

    public List<Birim> birimListeGetirTum() {
        List<Birim> temp = new ArrayList<>();
        String searchQuery = Fields.SELECT + "*"
                + Fields.FROM + BaseDao.BIRIM
                + Fields.ORDER_BY + "ad";
        // connect to DB
        ResultSet rs;
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(searchQuery)) {
            logger.info("{}", pstmt);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                temp.add(new Birim(rs));
            }
        } catch (Exception e) {
            logger.error(ExceptionUtils.getStackTrace(e));
        }
        return temp;
    }

    public int gkrnoGetir() {
        int result = -1;
        String selectQuery = Fields.SELECT + "irfan.gkrno_next()";
        logger.info(selectQuery);

        // connect to DB
        ResultSet rs;
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(selectQuery)) {
            logger.info("{}", pstmt);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                result = rs.getInt(1);
            }
        } catch (Exception e) {
            logger.error(ExceptionUtils.getStackTrace(e));
        }
        return result;
    }

    public int recordAgg(String tablename, String aggfunc, String aggfield, String filter) {
        return recordAgg2(tablename, aggfunc, aggfield, filter, null);
    }

    private int recordAgg2(String tablename, String aggfunc, String aggfield, String filter, String prefix) {
        int result = 0;
        String selectQuery = Fields.SELECT + aggfunc + "(" + aggfield + ")" +
                Fields.FROM + tablename + (prefix != null ? prefix : "") +
                Fields.WHERE + " 1=1 " + filter;
        logger.info(selectQuery);
        // connect to DB
        ResultSet rs;
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(selectQuery)) {
            long start = System.currentTimeMillis();
            rs = pstmt.executeQuery();
            logger.info(Constants.LOG_FORMAT, pstmt, (System.currentTimeMillis() - start));
            if (rs.next()) {
                result = rs.getInt(1);
            }
        } catch (Exception e) {
            logger.error(ExceptionUtils.getStackTrace(e));
        }
        return result;
    }
}