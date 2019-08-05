package erp.dao;

import erp.irfan.ConnectionManager;
import erp.irfan.entity.Genel;
import erp.irfan.entity.Irsaliye;
import erp.irfan.entity.IrsaliyeBilesen;
import erp.irfan.entity.IrsaliyeTip;
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
public class IrsaliyeDao implements Serializable, BaseDao {
    @Inject
    ConnectionManager connectionManager;
    private static final Logger logger = LoggerFactory.getLogger(IrsaliyeDao.class);

    public List<IrsaliyeBilesen> irsaliyeBilesenListeTum(IrsaliyeTip irsaliyetip, int onaylandi, String irsaliyeid, String firmaid, String bas_tarih, String bit_tarih, int offset, String mamulid) {
        List<IrsaliyeBilesen> temp = new ArrayList<IrsaliyeBilesen>();
        Connection conn = connectionManager.getConnection();
        String pageFilter = (offset == 0) ? "" : " offset " + (offset - 1) * Genel.ROWPERPAGE + " limit " + Genel.ROWPERPAGE;
        String irsaliyeFilter = (irsaliyeid == null || irsaliyeid.equals("")) ? "" : " and e.id = " + Integer.valueOf(irsaliyeid) + " ";
        String tarihFilter = (bas_tarih != null && bit_tarih != null) ? " and (date(gonderimtarihi) BETWEEN '"
                + Util.date_tr_to_eng(bas_tarih) + "' AND '" + Util.date_tr_to_eng(bit_tarih) + "')" : "";
        String firmaFilter = (firmaid == null || firmaid.equals("")) ? "" : " and e.firmaid = " + Integer.valueOf(firmaid) + " ";
        String mamulFilter = (mamulid == null || mamulid.equals("")) ? "" : " where ib.mamulid = " + Integer.valueOf(mamulid) + " ";
        String mamulFilterIrs = (mamulid == null || mamulid.equals("")) ? "" : " and exists " +
                "(select 1 from irfan.irsaliyebilesen ib where e.id = ib.irsaliyeid and ib.mamulid = " + Integer.valueOf(mamulid) + ") ";

        /* acik olan irsaliye bilsenlerini getirir */
        String searchQuery = "select ib.*, i.olusturmatarihi, i.gonderimtarihi, i.irsaliyeno, i.firmaid irsaliyefirmaid, "
                + "m.ad mamulad, m.kod mamulkod, f.id firmaid, f.ad firmaad "
                + "from irfan.irsaliyebilesen ib "
                + "join (select * from irfan.irsaliye e "
                + "where e.kapatildi=? and e.onaylandi=? " + firmaFilter
                + irsaliyeFilter + tarihFilter + mamulFilterIrs + " "
                + "order by e.id DESC " + pageFilter
                + " ) i on i.id=ib.irsaliyeid "
                + "join irfan.mamul m on m.id = ib.mamulid "
                + "join irfan.firma f on f.id = m.firmaid "
                + mamulFilter + " "
                + "order by i.id desc, ib.id ";
        // connect to DB
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = conn.prepareStatement(searchQuery);
            pstmt.setInt(1, irsaliyetip.value());
            pstmt.setInt(2, onaylandi);
            logger.info(pstmt.toString());

            rs = pstmt.executeQuery();

            while (rs.next()) {
                temp.add(new IrsaliyeBilesen(rs.getInt("id"), rs.getInt("irsaliyeid"), rs.getString("irsaliyeno"),
                        rs.getInt("firmaid"), rs.getString("firmaad"), rs.getInt("mamulid"), rs.getString("mamulad"),
                        rs.getString("mamulkod"), rs.getInt("gkrno"), rs.getInt("stokid"), rs.getInt("miktar"),
                        rs.getTimestamp("olusturmatarihi"), rs.getTimestamp("gonderimtarihi"), rs.getString("not"),
                        rs.getInt("irsaliyefirmaid")));
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

    public String irsaliyeSil(String irsaliyeid) {

        Connection conn = connectionManager.getConnection();
        String result = "0";
        String updateQuery = "select irfan.irsaliye_sil(?)";

        // connect to DB
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = conn.prepareStatement(updateQuery);
            pstmt.setInt(1, Integer.valueOf(irsaliyeid));
            logger.info(pstmt.toString());

            rs = pstmt.executeQuery();
            if (rs.next()) {
                result = rs.getString(1);
                rs.close();
            }
        } catch (SQLException e) {
            logger.error("Error: " + e);
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

    public Integer irsaliyeBilesenSil(String irsaliyebilesenid) {

        Connection conn = connectionManager.getConnection();
        Integer result = -1;
        String updateQuery = "delete from irfan.irsaliyebilesen where id = ?";

        // connect to DB
        PreparedStatement pstmt = null;
        try {
            pstmt = conn.prepareStatement(updateQuery);
            pstmt.setInt(1, Integer.valueOf(irsaliyebilesenid));
            logger.info(pstmt.toString());

            result = pstmt.executeUpdate();
        } catch (SQLException e) {
            logger.error("Error: " + e);
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

    public String irsaliyeNoKontrol(String irsaliyeno) {

        Connection conn = connectionManager.getConnection();
        String result = "0";
        String updateQuery = "select count(*) from irfan.irsaliye where trim(irsaliyeno) = ?";

        // connect to DB
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = conn.prepareStatement(updateQuery);
            pstmt.setString(1, irsaliyeno);
            logger.info(pstmt.toString());

            rs = pstmt.executeQuery();
            if (rs.next()) {
                result = rs.getString(1);
            }
        } catch (SQLException e) {
            logger.error("Error: " + e);
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

    public String irsaliyeOnayla(String irsaliyeid) {

        Connection conn = connectionManager.getConnection();
        String result = "0";
        String updateQuery = "SELECT irfan.irsaliye_onayla(?)";

        // connect to DB
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = conn.prepareStatement(updateQuery);
            pstmt.setInt(1, Integer.valueOf(irsaliyeid));
            logger.info(pstmt.toString());

            rs = pstmt.executeQuery();
            if (rs.next()) {
                result = rs.getString(1);
                rs.close();
            }
        } catch (SQLException e) {
            logger.error("Error: " + e);
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

    public void irsaliyeKapat(String irsaliyeid) {

        Connection conn = connectionManager.getConnection();
        String updateQuery = "update irfan.irsaliye set kapatildi=1 where id = ?";
        // connect to DB
        PreparedStatement pstmt = null;
        try {
            pstmt = conn.prepareStatement(updateQuery);
            pstmt.setInt(1, Integer.valueOf(irsaliyeid));
            logger.info(pstmt.toString());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            logger.error("Error: " + e);
        } finally {
            try {
                if (pstmt != null) {
                    pstmt.close();
                }
            } catch (SQLException e) {
                logger.error("Error: " + e);
            }
        }
        return;
    }

    public String irsaliyeBilesenGuncelle(int irsaliyeid, int irsaliyebilesenid, int mamulid, int gkrno, int miktar, String not) {
        Connection conn = connectionManager.getConnection();
        String retVal = "-1";
        String updateQuery = "SELECT irfan.irsaliyebilesen_guncelle(?,?,?,?,?,?,?) ";

        // connect to DB
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = conn.prepareStatement(updateQuery);
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
        } catch (SQLException e) {
            logger.error("Error: " + e);
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

    public int irsaliyeEkle(String irsaliyeno, String tarih, String firmaid) {
        Connection conn = connectionManager.getConnection();
        String insertQuery = "insert into irfan.irsaliye (irsaliyeno, olusturmatarihi, firmaid) values (?,?,?) ";

        PreparedStatement pstmt = null;
        ResultSet rs = null;
        int newIrsaliyeId = -1;
        try {
            pstmt = conn.prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, irsaliyeno);
            pstmt.setTimestamp(2, Util.date_tr_to_timestamp(tarih));
            pstmt.setInt(3, Integer.valueOf(firmaid));
            logger.info(pstmt.toString());

            pstmt.executeUpdate();
            rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                newIrsaliyeId = rs.getInt(1);
            }
        } catch (Exception e) {
            logger.error("Error: " + e);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (Exception e) {
                logger.error("Error: " + e);
            }
        }
        return newIrsaliyeId;
    }

    public String irsaliyeBilesenEkle(int irsaliyeid, int mamulid, int miktar, String not) {
        Connection conn = connectionManager.getConnection();
        String retVal = "-1";

        String insertQuery = "SELECT irfan.irsaliyebilesen_ekle (?,?,?,?) ";

        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = conn.prepareStatement(insertQuery);
            pstmt.setInt(1, irsaliyeid);
            pstmt.setInt(2, mamulid);
            pstmt.setInt(3, miktar);
            if (not != null) pstmt.setString(4, not);
            else pstmt.setNull(4, Types.VARCHAR);
            logger.info(pstmt.toString());
            rs = pstmt.executeQuery();
            if (rs.next()) {
                retVal = rs.getString(1);
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
        return retVal;
    }

    public int irsaliyeListeGetirCount(IrsaliyeTip irsaliyetip, String irsaliyeid, String firmaid, String tarih) {
        Connection conn = connectionManager.getConnection();
        int temp = 0;

        String irsaliyeFilter = (irsaliyeid == null) ? "" : " and id = " + Integer.valueOf(irsaliyeid) + " ";
        String tarihFilter = (tarih == null) ? "" : " and date(gonderimtarihi) = '" + Util.date_tr_to_eng(tarih) + "' ";
        String firmaFilter = (firmaid == null) ? ""
                : "" + " and id IN (" + "   select irsaliyeid from irfan.irsaliyebilesen ib "
                + "   join irfan.mamul m on m.id = ib.mamulid "
                + "   join irfan.firma f on f.id = m.firmaid "
                + "   WHERE f.id= " + Integer.valueOf(firmaid) + ")";

        String searchQuery = "select count(*) from irfan.irsaliye where kapatildi=1 and onaylandi = ? "
                + firmaFilter + tarihFilter + irsaliyeFilter;

        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = conn.prepareStatement(searchQuery);
            pstmt.setInt(1, irsaliyetip.value());
            logger.info(pstmt.toString());

            rs = pstmt.executeQuery();
            while (rs.next()) {
                temp = rs.getInt(1);
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

    public List<Irsaliye> irsaliyeListeGetirTum(IrsaliyeTip irsaliyetip, int offset, String irsaliyeid, String firmaid, String tarih) {
        Connection conn = connectionManager.getConnection();
        List<Irsaliye> temp = new ArrayList<Irsaliye>();

        String pageFilter = (offset == 0) ? ""
                : " offset " + (offset - 1) * Genel.ROWPERPAGE + " limit " + Genel.ROWPERPAGE;
        String irsaliyeFilter = (irsaliyeid == null) ? "" : " and i.id = " + Integer.valueOf(irsaliyeid) + " ";
        String tarihFilter = (tarih == null) ? "" : " and date(gonderimtarihi) = '" + Util.date_tr_to_eng(tarih) + "' ";
        String firmaFilter = (firmaid == null) ? "" : " and f.id= " + Integer.valueOf(firmaid) + " ";

        String searchQuery = "select i.*, f.ad firmaad from irfan.irsaliye i "
                + "left join irfan.firma f on f.id=i.firmaid where i.kapatildi=1 and i.onaylandi = ? " + firmaFilter
                + irsaliyeFilter + tarihFilter + "order by i.id DESC " + pageFilter;

        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {

            pstmt = conn.prepareStatement(searchQuery);
            pstmt.setInt(1, irsaliyetip.value());
            logger.info(pstmt.toString());
            rs = pstmt.executeQuery();

            while (rs.next()) {
                temp.add(new Irsaliye(rs.getInt("id"), rs.getString("irsaliyeno"), rs.getTimestamp("olusturmatarihi"),
                        rs.getTimestamp("gonderimtarihi"), rs.getInt("firmaid"), rs.getString("firmaad")));
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

    public List<Irsaliye> irsaliyeListeGetirKapaliTum(IrsaliyeTip irsaliyetip, int offset) {
        Connection conn = connectionManager.getConnection();
        List<Irsaliye> temp = new ArrayList<Irsaliye>();

        String pageFilter = (offset == 0) ? ""
                : " offset " + (offset - 1) * Genel.ROWPERPAGE + " limit " + Genel.ROWPERPAGE;

        String searchQuery = "select i.*, f.ad firmaad from irfan.irsaliye i "
                // + "join irfan.irsaliyebilesen ib on ib.irsaliyeid = i.id "
                + "left join irfan.firma f on f.id=i.firmaid " + "where i.kapatildi= ? " + "order by i.id DESC " + pageFilter;

        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {

            pstmt = conn.prepareStatement(searchQuery);
            pstmt.setInt(1, irsaliyetip.value());
            logger.info(pstmt.toString());
            rs = pstmt.executeQuery();

            while (rs.next()) {
                temp.add(new Irsaliye(rs.getInt("id"), rs.getString("irsaliyeno"), rs.getTimestamp("olusturmatarihi"),
                        rs.getTimestamp("gonderimtarihi"), rs.getInt("firmaid"), rs.getString("firmaad")));
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

    public List<IrsaliyeBilesen> irsaliyeBilesenListeGetirTum(IrsaliyeTip irsaliyetip, int onaylandi, String irsaliyeid, String firmaid, String tarih, int offset) {
        Connection conn = connectionManager.getConnection();
        List<IrsaliyeBilesen> temp = new ArrayList<IrsaliyeBilesen>();

        String pageFilter = (offset == 0) ? "" : " offset " + (offset - 1) * Genel.ROWPERPAGE + " limit " + Genel.ROWPERPAGE;
        String irsaliyeFilter = (irsaliyeid == null) ? "" : " and x.id = " + Integer.valueOf(irsaliyeid) + " ";
        String tarihFilter = (tarih == null) ? "" : " and date(gonderimtarihi) = '" + Util.date_tr_to_eng(tarih) + "' ";
        String firmaFilter = (firmaid == null) ? "" : " and f.id = " + Integer.valueOf(firmaid) + " ";

        /* acik olan irsaliye bilsenlerini getirir */
        String searchQuery = "select ib.*, i.olusturmatarihi, i.gonderimtarihi, i.irsaliyeno, i.firmaid irsaliyefirmaid, "
                + "m.ad mamulad, m.kod mamulkod, fb.id firmaid, fb.ad firmaad "
                + "from irfan.irsaliyebilesen ib "
                + "join (select x.* from irfan.irsaliye x left join irfan.firma f on f.id = x.firmaid where x.kapatildi=? and x.onaylandi=? "
                + firmaFilter + irsaliyeFilter + tarihFilter
                + " order by x.id DESC "
                + pageFilter
                + " ) i on i.id=ib.irsaliyeid "
                + "join irfan.mamul m on m.id = ib.mamulid "
                + "join irfan.firma fb on fb.id = m.firmaid "
                + "order by i.id desc, ib.id ";
        // connect to DB
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = conn.prepareStatement(searchQuery);
            pstmt.setInt(1, irsaliyetip.value());
            pstmt.setInt(2, onaylandi);
            logger.info(pstmt.toString());
            rs = pstmt.executeQuery();

            while (rs.next()) {
                temp.add(new IrsaliyeBilesen(rs.getInt("id"), rs.getInt("irsaliyeid"), rs.getString("irsaliyeno"),
                        rs.getInt("firmaid"), rs.getString("firmaad"), rs.getInt("mamulid"), rs.getString("mamulad"),
                        rs.getString("mamulkod"), rs.getInt("gkrno"), rs.getInt("stokid"), rs.getInt("miktar"),
                        rs.getTimestamp("olusturmatarihi"), rs.getTimestamp("gonderimtarihi"), rs.getString("not"),
                        rs.getInt("irsaliyefirmaid")));
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
}
