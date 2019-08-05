package erp.dao;

import erp.irfan.ConnectionManager;
import erp.irfan.entity.Bilesen;
import erp.irfan.entity.BilesenTip;
import erp.irfan.entity.Genel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.Serializable;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Singleton
public class BilesenDao implements Serializable, BaseDao {
    @Inject
    ConnectionManager connectionManager;
    private static final Logger logger = LoggerFactory.getLogger(BilesenDao.class);

    public List<Bilesen> bilesenListeGetirTum(BilesenTip bilesentip, int offset, Integer bilesenid) {
        Connection conn = connectionManager.getConnection();
        List<Bilesen> temp = new ArrayList<Bilesen>();

        String pageFilter = (offset == 0) ? "" : " offset " + (offset - 1) * Genel.ROWPERPAGE + " limit " + Genel.ROWPERPAGE;

        String filter1 = (bilesentip == null) ? "" : " and bilesentipid IN (" + bilesentip.id() + ") ";
        String filter2 = (bilesenid == null) ? "" : " and b.id = " + bilesenid + " ";
        String filter0 = " where 1=1 " + filter1 + filter2;

        String searchQuery = "select b.*, bt.ad bilesentipad, f.ad firmaad, bm.ad birimad "
                + "from irfan.bilesen b "
                + "join irfan.bilesentip bt on bt.id = b.bilesentipid "
                + "left join irfan.birim bm on bm.id = b.birimid "
                + "join irfan.firma f on f.id = b.firmaid " + filter0
                + "order by b.kod ASC " + pageFilter;

        // connect to DB
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = conn.prepareStatement(searchQuery);
            logger.info(pstmt.toString());
            rs = pstmt.executeQuery();

            while (rs.next()) {
                temp.add(
                        new Bilesen(
                                rs.getInt("id"),
                                rs.getString("ad"),
                                rs.getString("kod"),
                                rs.getInt("bilesentipid"),
                                rs.getString("bilesentipad"),
                                rs.getInt("birimid"),
                                rs.getString("birimad"),
                                rs.getInt("firmaid"),
                                rs.getString("firmaad"),
                                rs.getDate("eklemetarihi")));
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

    public String bilesenSil(String bilesenid) {
        Connection conn = connectionManager.getConnection();
        String result = "0";
        String updateQuery = "select irfan.bilesen_sil(?)";
        // connect to DB
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = conn.prepareStatement(updateQuery);
            pstmt.setInt(1, Integer.valueOf(bilesenid));
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

    public String bilesenGuncelle(String bilesenid, String birimid, String firmaid, String bilesenkod, String bilesenad) {
        Connection conn = connectionManager.getConnection();
        String result = "0";
        String updateQuery = "update irfan.bilesen "
                + "set birimid = ?, firmaid = ?, kod = ?, ad = ? "
                + "where id = ?";

        // connect to DB
        PreparedStatement pstmt = null;
        try {
            pstmt = conn.prepareStatement(updateQuery);
            if (birimid != null) pstmt.setInt(1, Integer.parseInt(birimid));
            else pstmt.setNull(1, Types.INTEGER);
            pstmt.setInt(2, Integer.parseInt(firmaid));
            pstmt.setString(3, bilesenkod);
            pstmt.setString(4, bilesenad);
            pstmt.setInt(5, Integer.parseInt(bilesenid));
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
