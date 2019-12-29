package erp.dao;

import erp.servlet.ConnectionManager;
import erp.entity.Calisan;
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
public class CalisanDao implements Serializable, BaseDao {
    @Inject
    ConnectionManager connectionManager;
    private static final Logger logger = LoggerFactory.getLogger(CalisanDao.class);

    public List<Calisan> calisanListeGetirTum(int offset, Integer durum) {
        List<Calisan> temp = new ArrayList<Calisan>();
        String pageFilter = Util.getPageFilter (offset);
        String durumFilter = (durum != null && durum == 1) ? " where durum = 1 " : "";
        String searchQuery = "select * from " + BaseDao.CALISAN + durumFilter + " order by ad " + pageFilter;

        // connect to DB
        ResultSet rs;
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(searchQuery)) {
            logger.info(searchQuery);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                temp.add(new Calisan(rs));
            }
        } catch (SQLException e) {
            logger.error("Error: " + e);
        }
        return temp;
    }

    public String calisanEkle(String ad, String soyad, String gorev) {
        String retVal = "0";
        String insertQuery = "insert into " + BaseDao.CALISAN + " (ad, soyad, gorev) values (?,?,?)";

        // connect to DB
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(insertQuery)) {
            pstmt.setString(1, ad);
            pstmt.setString(2, soyad);
            pstmt.setString(3, gorev);
            logger.info(pstmt.toString());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            logger.error("Error: " + e);
            retVal = e.getMessage();
        }
        return retVal;
    }

    public String calisanGuncelle(String id, String ad, String soyad, String gorev, String durum) {
        String result = "0";
        String updateQuery = "update " + BaseDao.CALISAN
                + " set ad =?, soyad =?, gorev =?, durum =?"
                + " where id =?";

        // connect to DB
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(updateQuery)) {
            pstmt.setString(1, ad);
            pstmt.setString(2, soyad);
            pstmt.setString(3, gorev);
            pstmt.setInt(4, Integer.parseInt(durum));
            pstmt.setInt(5, Integer.parseInt(id));
            logger.info(pstmt.toString());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            logger.error("Error: " + e);
            result = e.getMessage();
        }
        return result;
    }

    public String calisanSil(String id) {
        String result = "0";
        String updateQuery = "select " + BaseDao.F_CALISAN_SIL + "(?)";
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
        } catch (SQLException e) {
            logger.error("Error: " + e);
            result = e.getMessage();
        }
        return result;
    }
}
