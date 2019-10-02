package erp.dao;

import erp.servlet.ConnectionManager;
import erp.entity.Firma;
import erp.util.Util;
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
        ResultSet rs = null;
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(searchQuery)) {
            logger.info(pstmt.toString());
            rs = pstmt.executeQuery();
            while (rs.next()) {
                temp.add(new Firma(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return temp;
    }

    public String firmaEkle(String ad, String telefon, String adres) {
        String retVal = "0";
        String insertQuery = "insert into " + BaseDao.FIRMA + " (ad, telefon, adres) values (?,?,?)";
        // connect to DB
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(insertQuery)) {
            pstmt.setString(1, ad);
            pstmt.setString(2, telefon);
            pstmt.setString(3, adres);
            logger.info(pstmt.toString());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            logger.error("Error: " + e);
            retVal = e.getMessage();
        }
        return retVal;
    }

    public String firmaGuncelle(String id, String ad, String tel, String adres) {
        String result = "0";
        String updateQuery = "update " + BaseDao.FIRMA
                + " set ad =? , telefon =? , adres =?"
                + " where id =?";
        // connect to DB
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(updateQuery)) {
            pstmt.setString(1, ad);
            pstmt.setString(2, tel);
            pstmt.setString(3, adres);
            pstmt.setInt(4, Integer.valueOf(id));
            logger.info(pstmt.toString());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            logger.error("Error: " + e);
            result = e.getMessage();
        }
        return result;
    }

    public String firmaSil(String id) {
        String result = "0";
        String updateQuery = "select " + BaseDao.F_FIRMA_SIL + "(?)";
        // connect to DB
        ResultSet rs = null;
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(updateQuery)) {
            pstmt.setInt(1, Integer.valueOf(id));
            logger.info(pstmt.toString());
            rs = pstmt.executeQuery();
            if (rs.next()) {
                result = rs.getString(1);
            }
        } catch (SQLException e) {
            logger.error("Error: " + e);
            result = e.getMessage();
        }
        return result;
    }
}
