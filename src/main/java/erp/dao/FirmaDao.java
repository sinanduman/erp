package erp.dao;

import erp.entity.Firma;
import erp.servlet.ConnectionManager;
import erp.util.Constants;
import erp.util.Fields;
import erp.util.Util;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

@Singleton
public class FirmaDao implements Serializable, BaseDao {
    private static final Logger logger = LoggerFactory.getLogger(FirmaDao.class);
    @Inject
    private ConnectionManager connectionManager;

    public List<Firma> firmaListeGetirTum(int offset) {
        List<Firma> temp = new ArrayList<>();
        String pageFilter = Util.getPageFilter(offset);
        String searchQuery = Fields.SELECT + "*"
                + Fields.FROM + BaseDao.FIRMA
                + Fields.ORDER_BY + "ad "
                + pageFilter;
        // connect to DB
        ResultSet rs;
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(searchQuery)) {
            long start = System.currentTimeMillis();
            rs = pstmt.executeQuery();
            logger.info(Constants.LOG_FORMAT, pstmt, (System.currentTimeMillis() - start));
            while (rs.next()) {
                temp.add(new Firma(rs));
            }
        } catch (Exception e) {
            logger.error(ExceptionUtils.getStackTrace(e));
        }
        return temp;
    }

    public String firmaEkle(String ad, String telefon, String adres, String email) {
        String retVal = "0";
        String insertQuery = Fields.INSERT_INTO + BaseDao.FIRMA + " (ad, telefon, adres, email) values (?,?,?,?)";
        // connect to DB
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(insertQuery)) {
            long start = System.currentTimeMillis();
            pstmt.setString(1, ad);
            pstmt.setString(2, telefon);
            pstmt.setString(3, adres);
            pstmt.setString(4, email);
            pstmt.executeUpdate();
            logger.info(Constants.LOG_FORMAT, pstmt, (System.currentTimeMillis() - start));
        } catch (Exception e) {
            logger.error(ExceptionUtils.getStackTrace(e));
            retVal = e.getMessage();
        }
        return retVal;
    }

    public String firmaGuncelle(String id, String ad, String tel, String adres, String email) {
        String result = "0";
        String updateQuery = Fields.UPDATE + BaseDao.FIRMA
                + Fields.SET + "ad =? , telefon =? , adres =?, email =?"
                + Fields.WHERE + "id =?";
        // connect to DB
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(updateQuery)) {
            long start = System.currentTimeMillis();
            pstmt.setString(1, ad);
            pstmt.setString(2, tel);
            pstmt.setString(3, adres);
            pstmt.setString(4, email);
            pstmt.setInt(5, Integer.parseInt(id));
            pstmt.executeUpdate();
            logger.info(Constants.LOG_FORMAT, pstmt, (System.currentTimeMillis() - start));
        } catch (Exception e) {
            logger.error(ExceptionUtils.getStackTrace(e));
            result = e.getMessage();
        }
        return result;
    }

    public String firmaSil(String id) {
        String result = "0";
        String updateQuery = Fields.SELECT + BaseDao.F_FIRMA_SIL + "(?)";
        // connect to DB
        ResultSet rs;
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(updateQuery)) {
            long start = System.currentTimeMillis();
            pstmt.setInt(1, Integer.parseInt(id));
            rs = pstmt.executeQuery();
            logger.info(Constants.LOG_FORMAT, pstmt, (System.currentTimeMillis() - start));
            if (rs.next()) {
                result = rs.getString(1);
            }
        } catch (Exception e) {
            logger.error(ExceptionUtils.getStackTrace(e));
            result = e.getMessage();
        }
        return result;
    }
}
