package erp.dao;

import erp.servlet.ConnectionManager;
import erp.entity.Bilesen;
import erp.model.BilesenTip;
import erp.util.Util;
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

    public List<Bilesen> bilesenListeGetirTum(BilesenTip bilesentip, int offset, Integer bilesenid, Integer durum) {
        List<Bilesen> temp = new ArrayList<Bilesen>();
        String pageFilter = Util.getPageFilter (offset);
        String filter1 = (bilesentip == null) ? "" : " and bilesentipid in (" + bilesentip.id() + ")";
        String filter2 = (bilesenid == null) ? "" : " and b.id = " + bilesenid;
        String filter3 = (durum == null) ? "" : " and b.durum = " + durum;
        String filter0 = " where 1=1" + filter1 + filter2 + filter3;

        String searchQuery = "select b.*, bt.ad bilesentipad, f.ad firmaad, bm.ad birimad"
                + " from " + BaseDao.BILESEN + " b"
                + " join " + BaseDao.BILESENTIP + " bt on bt.id = b.bilesentipid"
                + " left join " + BaseDao.BIRIM + " bm on bm.id = b.birimid"
                + " join " + BaseDao.FIRMA + " f on f.id = b.firmaid" + filter0
                + " order by b.kod " + pageFilter;

        // connect to DB
        ResultSet rs = null;
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(searchQuery)) {
            logger.info(pstmt.toString());
            rs = pstmt.executeQuery();
            while (rs.next()) {
                temp.add(new Bilesen(rs));
            }
        } catch (SQLException e) {
            logger.error("Error: " + e);
        }
        return temp;
    }

    public String bilesenSil(String bilesenid) {
        String result = "0";
        String updateQuery = "select " + BaseDao.F_BILESEN_SIL + "(?)";
        // connect to DB
        ResultSet rs = null;
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(updateQuery)) {
            pstmt.setInt(1, Integer.valueOf(bilesenid));
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

    public String bilesenGuncelle(String bilesenid, String birimid, String firmaid, String bilesenkod, String bilesenad, String durum) {
        String result = "0";
        String updateQuery = "update " + BaseDao.BILESEN
                + " set birimid =?, firmaid =?, kod =?, ad =?, durum =?"
                + " where id =?";

        // connect to DB
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(updateQuery)) {
            if (birimid != null)
                pstmt.setInt(1, Integer.parseInt(birimid));
            else
                pstmt.setNull(1, Types.INTEGER);
            pstmt.setInt(2, Integer.parseInt(firmaid));
            pstmt.setString(3, bilesenkod);
            pstmt.setString(4, bilesenad);
            pstmt.setInt(5, Integer.parseInt(durum));
            pstmt.setInt(6, Integer.parseInt(bilesenid));
            logger.info(pstmt.toString());

            pstmt.executeUpdate();
        } catch (SQLException e) {
            logger.error("Error: " + e);
            result = e.getMessage();
        }
        return result;
    }
}