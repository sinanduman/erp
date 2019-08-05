package erp.dao;

import erp.irfan.ConnectionManager;
import erp.irfan.entity.Genel;
import erp.irfan.entity.Makina;
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
public class MakinaDao implements Serializable, BaseDao {
    @Inject
    ConnectionManager connectionManager;
    private static final Logger logger = LoggerFactory.getLogger(MakinaDao.class);

    public List<Makina> makinaListeGetirTum(Integer offset) {
        Connection conn = connectionManager.getConnection();
        List<Makina> temp = new ArrayList<Makina>();

        String pageFilter = (offset == 0) ? "" : " offset " + (offset - 1) * Genel.ROWPERPAGE + " limit " + Genel.ROWPERPAGE;

        String searchQuery = "select m.*, mt.ad makinatipad "
                + "from irfan.makina m join irfan.makinatip mt on mt.id=m.makinatipid "
                + "order by m.ad ASC "
                + pageFilter;

        // connect to DB
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = conn.prepareStatement(searchQuery);
            rs = pstmt.executeQuery();
            logger.info(pstmt.toString());
            while (rs.next()) {
                temp.add(new Makina(rs.getInt("id"), rs.getString("ad"), rs.getInt("makinatipid"),
                        rs.getString("makinatipad")));
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

    public String makinaEkle(String ad, String makinatip) {
        Connection conn = connectionManager.getConnection();
        String retVal = "0";
        String insertQuery = "insert into irfan.makina (ad, makinatipid) values (?, ?) ";

        // connect to DB
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = conn.prepareStatement(insertQuery);
            pstmt.setString(1, ad);
            pstmt.setInt(2, Integer.valueOf(makinatip));
            logger.info(pstmt.toString());
            pstmt.executeUpdate();

        } catch (SQLException e) {
            logger.error("Error: " + e);
            retVal = e.getMessage();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException e) {
                logger.error("Error: " + e);
            }
        }
        return retVal;
    }

    public String makinaSil(String id) {
        Connection conn = connectionManager.getConnection();
        String result = "0";
        String updateQuery = "select irfan.makina_sil(?)";
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

    public String makinaGuncelle(String id, String ad, String tip) {
        Connection conn = connectionManager.getConnection();
        String result = "0";
        String updateQuery = "update irfan.makina set ad = ? , makinatipid = ? where id = ?";

        // connect to DB
        PreparedStatement pstmt = null;
        try {
            pstmt = conn.prepareStatement(updateQuery);
            pstmt.setString(1, ad);
            pstmt.setInt(2, Integer.valueOf(tip));
            pstmt.setInt(3, Integer.valueOf(id));
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
}
