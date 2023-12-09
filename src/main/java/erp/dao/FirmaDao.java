package erp.dao;

import erp.servlet.ConnectionManager;
import erp.entity.Firma;
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
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Singleton
public class FirmaDao implements Serializable, BaseDao {
    @Inject
    ConnectionManager connectionManager;
    private static final Logger logger = LoggerFactory.getLogger(FirmaDao.class);

    public List<Firma> firmaListeGetirTum(int offset) {
        List<Firma> temp = new ArrayList<Firma>();
        String pageFilter = Util.getPageFilter (offset);
        String searchQuery = "select * from " + BaseDao.FIRMA + " order by ad " + pageFilter;
        // connect to DB
        ResultSet rs;
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(searchQuery)) {
            logger.info(pstmt.toString());
            rs = pstmt.executeQuery();
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
        String insertQuery = "insert into " + BaseDao.FIRMA + " (ad, telefon, adres, email) values (?,?,?,?)";
        // connect to DB
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(insertQuery)) {
            pstmt.setString(1, ad);
            pstmt.setString(2, telefon);
            pstmt.setString(3, adres);
            pstmt.setString(4, email);
            logger.info(pstmt.toString());
            pstmt.executeUpdate();
        } catch (Exception e) {
            logger.error(ExceptionUtils.getStackTrace(e));
            retVal = e.getMessage();
        }
        return retVal;
    }

    public String firmaGuncelle(String id, String ad, String tel, String adres, String email) {
        String result = "0";
        String updateQuery = "update " + BaseDao.FIRMA
                + " set ad =? , telefon =? , adres =?, email =?"
                + " where id =?";
        // connect to DB
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(updateQuery)) {
            pstmt.setString(1, ad);
            pstmt.setString(2, tel);
            pstmt.setString(3, adres);
            pstmt.setString(4, email);
            pstmt.setInt(5, Integer.parseInt(id));
            logger.info(pstmt.toString());
            pstmt.executeUpdate();
        } catch (Exception e) {
            logger.error(ExceptionUtils.getStackTrace(e));
            result = e.getMessage();
        }
        return result;
    }

    public String firmaSil(String id) {
        String result = "0";
        String updateQuery = "select " + BaseDao.F_FIRMA_SIL + "(?)";
        // connect to DB
        ResultSet rs;
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(updateQuery)) {
            pstmt.setInt(1, Integer.parseInt(id));
            logger.info(pstmt.toString());
            rs = pstmt.executeQuery();
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
