package erp.dao;

import erp.irfan.ConnectionManager;
import erp.irfan.entity.Calisan;
import erp.irfan.entity.Genel;
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

    public String calisanGuncelle(String id, String ad, String soyad, String gorev, String durum) {
        Connection conn = connectionManager.getConnection();
        String result = "0";
        String updateQuery = "update irfan.calisan " + "set " + "ad = ? ," + "soyad  = ? ," + "gorev  = ? ,"
                + "durum  = ? " + "where id = ?";

        // connect to DB
        PreparedStatement pstmt = null;
        try {
            pstmt = conn.prepareStatement(updateQuery);
            pstmt.setString(1, ad);
            pstmt.setString(2, soyad);
            pstmt.setString(3, gorev);
            pstmt.setInt(4, Integer.valueOf(durum));
            pstmt.setInt(5, Integer.valueOf(id));
            logger.info(pstmt.toString());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            logger.error("Error: " + e);
            result = e.getMessage();
        } finally {
            try {
                if (pstmt != null) {
                    pstmt.close();
                }
            } catch (SQLException e) {
                logger.error("Error: " + e);
            }
        }
        return result;
    }

    public List<Calisan> calisanListeGetirTum(int offset, Integer durum) {
        Connection conn = connectionManager.getConnection();
        List<Calisan> temp = new ArrayList<Calisan>();

        String pageFilter = (offset == 0) ? "" : " offset " + (offset - 1) * Genel.ROWPERPAGE + " limit " + Genel.ROWPERPAGE;
        String durumFilter = (durum != null && durum == 1) ? " where durum = 1 " : "";
        String searchQuery = "select * from irfan.calisan " + durumFilter + " order by ad ASC " + pageFilter;

        // connect to DB
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            logger.info(searchQuery);
            pstmt = conn.prepareStatement(searchQuery);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                temp.add(new Calisan(rs.getInt("id"), rs.getString("ad"), rs.getString("soyad"), rs.getString("gorev"),
                        rs.getInt("durum")));
            }
        } catch (SQLException e) {
            logger.error("Error: " + e);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException e) {
                logger.error("Error: " + e);
            }
        }
        return temp;
    }

    public String calisanEkle(String ad, String soyad, String gorev) {
        Connection conn = connectionManager.getConnection();
        String retVal = "0";
        String insertQuery = "insert into irfan.calisan (ad, soyad, gorev) values (?,?,?) ";

        // connect to DB
        PreparedStatement pstmt = null;
        try {
            pstmt = conn.prepareStatement(insertQuery);
            pstmt.setString(1, ad);
            pstmt.setString(2, soyad);
            pstmt.setString(3, gorev);
            logger.info(pstmt.toString());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            logger.error("Error: " + e);
            retVal = e.getMessage();
        } finally {
            try {
                if (pstmt != null) {
                    pstmt.close();
                }
            } catch (SQLException e) {
                logger.error("Error: " + e);
            }
        }
        return retVal;
    }

    public String calisanSil(String id) {
        Connection conn = connectionManager.getConnection();
        String result = "0";
        String updateQuery = "select irfan.calisan_sil(?)";
        // connect to DB
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = conn.prepareStatement(updateQuery);
            pstmt.setInt(1, Integer.valueOf(id));
            logger.info(pstmt.toString());

            rs = pstmt.executeQuery();
            if (rs.next()) {
                result = rs.getString(1);
            }
        } catch (SQLException e) {
            logger.error("Error: " + e);
            result = e.getMessage();
        } finally {
            try {
                if (pstmt != null) {
                    pstmt.close();
                }
            } catch (SQLException e) {
                logger.error("Error: " + e);
            }
        }
        return result;
    }
}
