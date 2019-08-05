package erp.dao;

import erp.irfan.ConnectionManager;
import erp.irfan.entity.Firma;
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
public class FirmaDao implements Serializable, BaseDao {
    @Inject
    ConnectionManager connectionManager;
    private static final Logger logger = LoggerFactory.getLogger(FirmaDao.class);

    public String firmaSil(String id) {
        Connection conn = connectionManager.getConnection();
        String result = "0";
        String updateQuery = "select irfan.firma_sil(?)";
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

    public String firmaGuncelle(String id, String ad, String tel, String adres) {
        Connection conn = connectionManager.getConnection();
        String result = "0";
        String updateQuery = "update irfan.firma "
                + "set ad = ? , telefon = ? , adres = ? "
                + "where id = ?";

        // connect to DB
        PreparedStatement pstmt = null;
        try {
            pstmt = conn.prepareStatement(updateQuery);
            pstmt.setString(1, ad);
            pstmt.setString(2, tel);
            pstmt.setString(3, adres);
            pstmt.setInt(4, Integer.valueOf(id));
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

    public String firmaEkle(String ad, String telefon, String adres) {
        Connection conn = connectionManager.getConnection();
        String retVal = "0";
        String insertQuery = "insert into irfan.firma (ad, telefon, adres) values (?, ?, ?)";

        // connect to DB
        PreparedStatement pstmt = null;
        try {
            pstmt = conn.prepareStatement(insertQuery);
            pstmt.setString(1, ad);
            pstmt.setString(2, telefon);
            pstmt.setString(3, adres);
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

    public List<Firma> firmaListeGetirTum(int offset) {
        Connection conn = connectionManager.getConnection();
        List<Firma> temp = new ArrayList<Firma>();
        String pageFilter = (offset == 0) ? "" : " offset " + (offset - 1) * Genel.ROWPERPAGE + " limit " + Genel.ROWPERPAGE;

        String searchQuery = "select * from irfan.firma order by ad ASC" + pageFilter;

        // connect to DB
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = conn.prepareStatement(searchQuery);
            logger.info(pstmt.toString());
            rs = pstmt.executeQuery();
            while (rs.next()) {
                temp.add(
                        new Firma(rs.getInt("id"), rs.getString("ad"), rs.getString("telefon"), rs.getString("adres")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return temp;
    }
}
