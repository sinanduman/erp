package erp.dao;

import erp.servlet.ConnectionManager;
import erp.entity.Irsaliye;
import erp.entity.IrsaliyeBilesen;
import erp.model.IrsaliyeTip;
import erp.util.Util;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.Serializable;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Singleton
public class IrsaliyeDao implements Serializable, BaseDao {
    @Inject
    ConnectionManager connectionManager;
    private static final Logger logger = LoggerFactory.getLogger(IrsaliyeDao.class);

    public String irsaliyeSil(String irsaliyeid) {
        String result = "0";
        String updateQuery = "select " + BaseDao.F_IRSALIYE_SIL + "(?)";

        // connect to DB
        ResultSet rs;
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement  pstmt = conn.prepareStatement(updateQuery)) {
            pstmt.setInt(1, Integer.parseInt(irsaliyeid));
            logger.info(pstmt.toString());
            rs = pstmt.executeQuery();
            if (rs.next()) {
                result = rs.getString(1);
                rs.close();
            }
        } catch (Exception e) {
            logger.error(ExceptionUtils.getStackTrace(e));
        }
        return result;
    }

    public Integer irsaliyeBilesenSil(String irsaliyebilesenid) {
        Integer result = -1;
        String updateQuery = "delete from " + BaseDao.IRSALIYEBILESEN + " where id = ?";

        // connect to DB
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(updateQuery)) {
            pstmt.setInt(1, Integer.parseInt(irsaliyebilesenid));
            logger.info(pstmt.toString());
            result = pstmt.executeUpdate();
        } catch (Exception e) {
            logger.error(ExceptionUtils.getStackTrace(e));
        }
        return result;
    }

    public String irsaliyeNoKontrol(String irsaliyeno) {
        String result = "0";
        String updateQuery = "select count(*) from " + BaseDao.IRSALIYE + " where trim(irsaliyeno) = ?";

        // connect to DB
        ResultSet rs;
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(updateQuery)) {
            pstmt.setString(1, irsaliyeno);
            logger.info(pstmt.toString());
            rs = pstmt.executeQuery();
            if (rs.next()) {
                result = rs.getString(1);
            }
        } catch (Exception e) {
            logger.error(ExceptionUtils.getStackTrace(e));
        }
        return result;
    }

    public String irsaliyeOnayla(String irsaliyeid) {
        String result = "0";
        String updateQuery = "SELECT " + BaseDao.F_IRSALIYE_ONAYLA + "(?)";

        // connect to DB
        ResultSet rs;
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(updateQuery)) {
            pstmt.setInt(1, Integer.parseInt(irsaliyeid));
            logger.info(pstmt.toString());
            rs = pstmt.executeQuery();
            if (rs.next()) {
                result = rs.getString(1);
                rs.close();
            }
        } catch (Exception e) {
            logger.error(ExceptionUtils.getStackTrace(e));
        }
        return result;
    }

    public int irsaliyeKapat(String irsaliyeid) {
        int retVal = 0;
        String updateQuery = "update " + BaseDao.IRSALIYE + " set kapatildi=1 where id = ?";
        // connect to DB
        ResultSet rs;
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(updateQuery)) {
            pstmt.setInt(1, Integer.parseInt(irsaliyeid));
            logger.info(pstmt.toString());
            retVal = pstmt.executeUpdate();
        } catch (Exception e) {
            logger.error(ExceptionUtils.getStackTrace(e));
        }
        return retVal;
    }

    public String irsaliyeBilesenGuncelle(int irsaliyeid, int irsaliyebilesenid, int mamulid, int gkrno, int miktar, String not) {
        String retVal = "-1";
        String updateQuery = "SELECT " + BaseDao.F_IRSALIYEBILESEN_GUNCELLE + "(?,?,?,?,?,?,?)";

        // connect to DB
        ResultSet rs;
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(updateQuery)) {
            pstmt.setInt(1, irsaliyeid);
            pstmt.setInt(2, irsaliyebilesenid);
            pstmt.setInt(3, mamulid);
            pstmt.setInt(4, gkrno);
            pstmt.setInt(5, miktar);
            pstmt.setString(6, not);
            logger.info(pstmt.toString());
            rs = pstmt.executeQuery();
            if (rs.next()) {
                retVal = rs.getString(1);
                rs.close();
            }
        } catch (Exception e) {
            logger.error(ExceptionUtils.getStackTrace(e));
        }
        return retVal;
    }

    public int irsaliyeEkle(String irsaliyeno, String tarih, String firmaid) {
        String insertQuery = "insert into " + BaseDao.IRSALIYE + "(irsaliyeno, olusturmatarihi, firmaid) values (?,?,?)";

        ResultSet rs;
        int newIrsaliyeId = -1;
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, irsaliyeno);
            pstmt.setTimestamp(2, Util.date_tr_to_timestamp(tarih));
            pstmt.setInt(3, Integer.parseInt(firmaid));
            logger.info(pstmt.toString());
            pstmt.executeUpdate();
            rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                newIrsaliyeId = rs.getInt(1);
            }
        } catch (Exception e) {
            logger.error(ExceptionUtils.getStackTrace(e));
        }
        return newIrsaliyeId;
    }

    public String irsaliyeBilesenEkle(int irsaliyeid, int mamulid, int miktar, String not) {
        String retVal = "-1";
        String insertQuery = "SELECT " + BaseDao.F_IRSALIYEBILESEN_EKLE + "(?,?,?,?)";

        ResultSet rs;
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(insertQuery)) {
            pstmt.setInt(1, irsaliyeid);
            pstmt.setInt(2, mamulid);
            pstmt.setInt(3, miktar);
            if (not != null)
                pstmt.setString(4, not);
            else
                pstmt.setNull(4, Types.VARCHAR);
            logger.info(pstmt.toString());
            rs = pstmt.executeQuery();
            if (rs.next()) {
                retVal = rs.getString(1);
            }
        } catch (Exception e) {
            logger.error(ExceptionUtils.getStackTrace(e));
        }
        return retVal;
    }

    public List<Irsaliye> irsaliyeListeGetirTum(IrsaliyeTip irsaliyetip, int offset, String irsaliyeid, String firmaid, String tarih) {
        List<Irsaliye> temp = new ArrayList<Irsaliye>();
        String pageFilter = Util.getPageFilter (offset);
        String irsaliyeFilter = (irsaliyeid == null) ? "" : " and i.id = " + Integer.parseInt(irsaliyeid) + " ";
        String tarihFilter = (tarih == null) ? "" : " and date(gonderimtarihi) = '" + Util.date_tr_to_eng(tarih) + "' ";
        String firmaFilter = (firmaid == null) ? "" : " and f.id= " + Integer.parseInt(firmaid) + " ";

        String searchQuery = "select i.*, f.ad firmaad from " + BaseDao.IRSALIYE + " i "
                + " left join " + BaseDao.FIRMA + " f on f.id=i.firmaid"
                + " where i.kapatildi=1 and i.onaylandi =? "
                + firmaFilter + irsaliyeFilter + tarihFilter
                + " order by i.id DESC "
                + pageFilter;
        ResultSet rs;
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(searchQuery)) {
            long start = System.currentTimeMillis();
            pstmt.setInt(1, irsaliyetip.value());
            rs = pstmt.executeQuery();
            logger.info(pstmt.toString() + (System.currentTimeMillis()-start) +"ms.");
            while (rs.next()) {
                temp.add(new Irsaliye(rs));
            }
        } catch (Exception e) {
            logger.error(ExceptionUtils.getStackTrace(e));
        }
        return temp;
    }

    public List<Irsaliye> irsaliyeListeGetirKapaliTum(IrsaliyeTip irsaliyetip, int offset) {
        List<Irsaliye> temp = new ArrayList<Irsaliye>();
        String pageFilter = Util.getPageFilter (offset);
        String searchQuery = "select i.*, f.ad firmaad from " + BaseDao.IRSALIYE + " i "
                + " left join " + BaseDao.FIRMA + " f on f.id=i.firmaid "
                + " where i.kapatildi= ? "
                + " order by i.id DESC "
                + pageFilter;

        ResultSet rs;
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(searchQuery)) {
            long start = System.currentTimeMillis();
            pstmt.setInt(1, irsaliyetip.value());
            rs = pstmt.executeQuery();
            logger.info(pstmt.toString() + (System.currentTimeMillis()-start) +"ms.");

            while (rs.next()) {
                temp.add(new Irsaliye(rs));
            }
        } catch (Exception e) {
            logger.error(ExceptionUtils.getStackTrace(e));
        }
        return temp;
    }

    public List<IrsaliyeBilesen> irsaliyeBilesenListeGetirTum(IrsaliyeTip irsaliyetip, int onaylandi, String irsaliyeid, String firmaid, String tarih, int offset) {
        List<IrsaliyeBilesen> temp = new ArrayList<IrsaliyeBilesen>();

        String pageFilter = Util.getPageFilter (offset);
        String irsaliyeFilter = (irsaliyeid == null) ? "" : " and x.id = " + Integer.parseInt(irsaliyeid) + " ";
        String tarihFilter = (tarih == null) ? "" : " and date(gonderimtarihi) = '" + Util.date_tr_to_eng(tarih) + "' ";
        String firmaFilter = (firmaid == null) ? "" : " and f.id = " + Integer.parseInt(firmaid) + " ";

        /* acik olan irsaliye bilsenlerini getirir */
        String searchQuery = "select ib.*, i.olusturmatarihi, i.gonderimtarihi, i.irsaliyeno, "
                + " i.firmaid irsaliyefirmaid, m.ad mamulad, m.kod mamulkod, fb.id firmaid, fb.ad firmaad "
                + " from " + BaseDao.IRSALIYEBILESEN + " ib "
                + " join (select x.* from " + BaseDao.IRSALIYE + " x left join " + BaseDao.FIRMA + " f on f.id = x.firmaid where x.kapatildi=? and x.onaylandi=? "
                + firmaFilter + irsaliyeFilter + tarihFilter
                + " order by x.id DESC "
                + pageFilter
                + " ) i on i.id=ib.irsaliyeid "
                + " join " + BaseDao.MAMUL + " m on m.id = ib.mamulid "
                + " join " + BaseDao.FIRMA + " fb on fb.id = m.firmaid "
                + " order by i.id desc, ib.id ";
        // connect to DB
        ResultSet rs;
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(searchQuery)) {
            long start = System.currentTimeMillis();
            pstmt.setInt(1, irsaliyetip.value());
            pstmt.setInt(2, onaylandi);
            rs = pstmt.executeQuery();
            logger.info(pstmt.toString() + (System.currentTimeMillis()-start) +"ms.");
            while (rs.next()) {
                temp.add(new IrsaliyeBilesen(rs));
            }
        } catch (Exception e) {
            logger.error(ExceptionUtils.getStackTrace(e));
        }
        return temp;
    }

    public List<IrsaliyeBilesen> irsaliyeBilesenListeTum(IrsaliyeTip irsaliyetip, int onaylandi, String irsaliyeid, String firmaid, String bas_tarih, String bit_tarih, int offset, String mamulid) {
        List<IrsaliyeBilesen> temp = new ArrayList<IrsaliyeBilesen>();

        String pageFilter = Util.getPageFilter (offset);
        String irsaliyeFilter = (irsaliyeid == null || irsaliyeid.equals("")) ? "" : " and e.id = " + Integer.parseInt(irsaliyeid) + " ";
        String tarihFilter = (bas_tarih != null && bit_tarih != null) ? " and (date(gonderimtarihi) BETWEEN '" + Util.date_tr_to_eng(bas_tarih) + "' AND '" + Util.date_tr_to_eng(bit_tarih) + "')" : "";
        String firmaFilter = (firmaid == null || firmaid.equals("")) ? "" : " and e.firmaid = " + Integer.parseInt(firmaid) + " ";
        String mamulFilter = (mamulid == null || mamulid.equals("")) ? "" : " where ib.mamulid = " + Integer.parseInt(mamulid) + " ";
        String mamulFilterIrs = (mamulid == null || mamulid.equals("")) ? "" : " and exists (select 1 from " + BaseDao.IRSALIYEBILESEN + " ib where e.id = ib.irsaliyeid and ib.mamulid = " + Integer.parseInt(mamulid) + ") ";

        /* acik olan irsaliye bilsenlerini getirir */
        String searchQuery = "select ib.*, i.olusturmatarihi, i.gonderimtarihi, i.irsaliyeno,"
                + " i.firmaid irsaliyefirmaid, m.ad mamulad, m.kod mamulkod, f.id firmaid, f.ad firmaad"
                + " from " + BaseDao.IRSALIYEBILESEN + " ib"
                + " join (select * from " + BaseDao.IRSALIYE + " e where e.kapatildi=? and e.onaylandi=? " + firmaFilter + irsaliyeFilter + tarihFilter + mamulFilterIrs + " order by e.id DESC " + pageFilter + ") i on i.id=ib.irsaliyeid"
                + " join " + BaseDao.MAMUL + " m on m.id = ib.mamulid"
                + " join " + BaseDao.FIRMA + " f on f.id = i.firmaid"
                + mamulFilter
                + " order by i.id desc, ib.id";
        // connect to DB
        ResultSet rs;
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(searchQuery)) {
            long start = System.currentTimeMillis();
            pstmt.setInt(1, irsaliyetip.value());
            pstmt.setInt(2, onaylandi);
            rs = pstmt.executeQuery();
            logger.info(pstmt.toString() + (System.currentTimeMillis()-start) +"ms.");
            while (rs.next()) {
                temp.add(new IrsaliyeBilesen(rs));
            }
        } catch (Exception e) {
            logger.error(ExceptionUtils.getStackTrace(e));
        }
        return temp;
    }
}
