package erp.dao;

import erp.entity.Bilesen;
import erp.model.BilesenTip;
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
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

@Singleton
public class BilesenDao implements Serializable, BaseDao {
    @Inject
    private ConnectionManager connectionManager;
    private static final Logger logger = LoggerFactory.getLogger(BilesenDao.class);

    public List<Bilesen> bilesenListeGetirTum(BilesenTip bilesentip, int offset, Integer bilesenid, Integer durum) {
        List<Bilesen> temp = new ArrayList<>();
        String pageFilter = Util.getPageFilter(offset);
        String filter1 = (bilesentip == null) ? "" : " and bilesentipid in (" + bilesentip.id() + ")";
        String filter2 = (bilesenid == null) ? "" : " and b.id = " + bilesenid;
        String filter3 = (durum == null) ? "" : " and b.durum = " + durum;
        String filter0 = " where 1=1" + filter1 + filter2 + filter3;

        String searchQuery = Fields.SELECT + "b.*, bt.ad bilesentipad, f.ad firmaad, bm.ad birimad"
                + Fields.FROM + BaseDao.BILESEN + " b"
                + Fields.JOIN + BaseDao.BILESENTIP + " bt on bt.id = b.bilesentipid"
                + Fields.LEFT_JOIN + BaseDao.BIRIM + " bm on bm.id = b.birimid"
                + Fields.JOIN + BaseDao.FIRMA + " f on f.id = b.firmaid"
                + Fields.JOIN + BaseDao.PARA + " p on p.id = b.fiyatid"
                + filter0
                + Fields.ORDER_BY + "b.id desc " + pageFilter;

        // connect to DB
        ResultSet rs;
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(searchQuery)) {
            long start = System.currentTimeMillis();
            rs = pstmt.executeQuery();
            logger.info(Constants.LOG_FORMAT, pstmt, (System.currentTimeMillis() - start));
            while (rs.next()) {
                temp.add(new Bilesen(rs));
            }
        } catch (Exception e) {
            logger.error(ExceptionUtils.getStackTrace(e));
        }
        return temp;
    }

    public String bilesenSil(String bilesenid) {
        String result = "0";
        String updateQuery = Fields.SELECT + BaseDao.F_BILESEN_SIL + "(?)";
        // connect to DB
        ResultSet rs;
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(updateQuery)) {
            long start = System.currentTimeMillis();
            pstmt.setInt(1, Integer.parseInt(bilesenid));
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

    public String bilesenGuncelle(String bilesenid, String birimid, String firmaid, String bilesenkod, String bilesenad, String durum, String fiyatId, String fiyat, String kritikEsik) {
        String result = "0";
        String updateQuery = Fields.UPDATE + BaseDao.BILESEN
                + Fields.SET + "birimid =?, firmaid =?, kod =?, ad =?, durum =?, fiyatid =?, fiyat =?, kritikesik =?"
                + Fields.WHERE + "id =?";

        // connect to DB
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(updateQuery)) {
            long start = System.currentTimeMillis();
            if (birimid != null)
                pstmt.setInt(1, Integer.parseInt(birimid));
            else
                pstmt.setNull(1, Types.INTEGER);
            pstmt.setInt(2, Integer.parseInt(firmaid));
            pstmt.setString(3, bilesenkod);
            pstmt.setString(4, bilesenad);
            pstmt.setInt(5, Integer.parseInt(durum));
            pstmt.setInt(6, Integer.parseInt(fiyatId));
            pstmt.setDouble(7, Double.parseDouble(fiyat));
            pstmt.setInt(8, Integer.parseInt(kritikEsik));
            pstmt.setInt(9, Integer.parseInt(bilesenid));
            pstmt.executeUpdate();
            logger.info(Constants.LOG_FORMAT, pstmt, (System.currentTimeMillis() - start));
        } catch (Exception e) {
            logger.error(ExceptionUtils.getStackTrace(e));
            result = e.getMessage();
        }
        return result;
    }
}