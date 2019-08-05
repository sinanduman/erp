package erp.dao;

import erp.irfan.ConnectionManager;
import erp.irfan.entity.BilesenTip;
import erp.irfan.entity.Genel;
import erp.irfan.entity.Stok;
import erp.irfan.entity.StokRapor;
import erp.util.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Singleton
public class StokDao implements Serializable, BaseDao {
    @Inject
    ConnectionManager connectionManager;
    private static final Logger logger = LoggerFactory.getLogger(StokDao.class);

    public String stokKontrol(String tarih, String gkrno, String miktar, String iade, String bilesentipid) {
        Connection conn = connectionManager.getConnection();
        Integer sum = null;
        String result = "0";
        String updateQuery = "select sum(case when islemyonu = 0 then miktar else -1*miktar end) "
                + "from irfan.stok "
                + "where gkrno = ? and exists "
                + "(select 1 from irfan.stok where gkrno = ? and islemyonu = 0 and (date(giristarihi) = ? or date(cikistarihi) = ?)) "
                + "group by gkrno ";

        // connect to DB
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = conn.prepareStatement(updateQuery);
            pstmt.setInt(1, Integer.valueOf(gkrno));
            pstmt.setInt(2, Integer.valueOf(gkrno));
            pstmt.setDate(3, Date.valueOf(Util.date_tr_to_eng(tarih)));
            pstmt.setDate(4, Date.valueOf(Util.date_tr_to_eng(tarih)));
            rs = pstmt.executeQuery();

            logger.info(pstmt.toString());

            if (rs.next()) {
                sum = rs.getInt(1);
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
        if (Boolean.valueOf(iade)) {
            if (sum == null) sum = 0;
            String resStr = "";
            Double hammaddeMiktar = 0.0;
            if (Integer.valueOf(miktar) > sum) {
                resStr = sum.toString();
                /* Donus degerinin KG olarak verilmesi icin */
                if (bilesentipid.equals("1")) {
                    hammaddeMiktar = Util.Round((Double.valueOf(sum) / 1000.0), 3.0);
                    resStr = hammaddeMiktar.toString() + " Kg";
                }
                result = Util.getTarihTRShort(Date.valueOf(Util.date_tr_to_eng(tarih))) + " tarihli stok miktarı "
                        + resStr + ". İade işlemini gözden geçirin!";
            }
        } else {
            if (sum == null) {
                result = Util.getTarihTRShort(Date.valueOf(Util.date_tr_to_eng(tarih))) + " tarihinde bir üretim yok!";
            }
        }
        return result;
    }

    public String stokEkle(String bilesenid, String miktar, String irsaliyeno, String lot, String gkrno, String tarih, String not) {
        Connection conn = connectionManager.getConnection();
        String retVal = "0";

        String insertQuery = "SELECT irfan.stok_ekle(?,?,?,?,?,?,?,?) ";

        PreparedStatement pstmt = null;
        try {
            logger.info(tarih);

            pstmt = conn.prepareStatement(insertQuery);
            pstmt.setNull(1, Types.INTEGER); // planid
            pstmt.setInt(2, Integer.valueOf(bilesenid));
            pstmt.setBigDecimal(3, BigDecimal.valueOf(Double.valueOf(miktar)));
            if (gkrno != null) pstmt.setInt(4, Integer.valueOf(gkrno));
            else pstmt.setNull(4, Types.INTEGER);
            if (irsaliyeno != null) pstmt.setString(5, irsaliyeno);
            else pstmt.setNull(5, Types.VARCHAR);
            if (lot != null) pstmt.setString(6, lot);
            else pstmt.setNull(6, Types.VARCHAR);
            if (not != null) pstmt.setString(7, not);
            else pstmt.setNull(7, Types.VARCHAR);
            if (tarih != null) pstmt.setTimestamp(8, Util.date_tr_to_timestamp(tarih));
            else pstmt.setNull(8, Types.TIMESTAMP);

            logger.info(pstmt.toString());
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                retVal = rs.getString(1);
            }
        } catch (SQLException e) {
            retVal = e.getMessage();
            logger.error("Error: " + e);
        } finally {
            try {
                if (pstmt != null) {
                    pstmt.close();
                }
            } catch (SQLException e) {
                retVal = e.getMessage();
                logger.error("Error: " + e);
            }
        }
        return retVal;
    }

    public String stokDus(String bilesenid, String miktar, String irsaliyeno, String lot, String gkrno, String tarih, String not) {
        Connection conn = connectionManager.getConnection();
        String retVal = "-1";
        String updateQuery = "SELECT irfan.stok_dus(?,?,?,?,?,?,?,?) ";

        PreparedStatement pstmt = null;
        try {
            pstmt = conn.prepareStatement(updateQuery);
            pstmt.setNull(1, Types.INTEGER); // Planid
            pstmt.setInt(2, Integer.valueOf(bilesenid));
            pstmt.setBigDecimal(3, BigDecimal.valueOf(Double.valueOf(miktar)));
            if (gkrno != null) pstmt.setInt(4, Integer.valueOf(gkrno));
            else pstmt.setNull(4, Types.INTEGER);
            if (irsaliyeno != null) pstmt.setString(5, irsaliyeno);
            else pstmt.setNull(5, Types.VARCHAR);
            if (lot != null) pstmt.setString(6, lot);
            else pstmt.setNull(6, Types.VARCHAR);
            if (not != null) pstmt.setString(7, not);
            else pstmt.setNull(7, Types.VARCHAR);
            if (tarih != null) pstmt.setTimestamp(8, Util.date_tr_to_timestamp(tarih));
            else pstmt.setNull(8, Types.TIMESTAMP);

            logger.info(pstmt.toString());
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                retVal = rs.getString(1);
            }
        } catch (Exception e) {
            logger.error("Error: " + e);
        }
        return retVal;
    }

    public String stokYeterli(Integer uretimplanid, Integer mamulid, Integer miktar) {
        Connection conn = connectionManager.getConnection();
        String retVal = "0";

        String insertQuery = "SELECT irfan.stok_yeterli(?,?,?) ";

        PreparedStatement pstmt = null;
        try {
            pstmt = conn.prepareStatement(insertQuery);
            pstmt.setInt(1, uretimplanid);
            pstmt.setInt(2, mamulid);
            pstmt.setInt(3, miktar);

            logger.info(pstmt.toString());
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                retVal = rs.getString(1);
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

    public String hammaddeStokSil(Integer stokid) {
        Connection conn = connectionManager.getConnection();
        String retVal = "0";
        String deleteQuery = "select irfan.hammaddestok_sil(?)";
        // connect to DB
        PreparedStatement pstmt = null;
        try {
            pstmt = conn.prepareStatement(deleteQuery);
            pstmt.setInt(1, stokid);
            logger.info(pstmt.toString());
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                retVal = rs.getString(1);
                logger.info("Sonuc: " + retVal);
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

    public List<Stok> stokMamulListeGetirTum(Integer mamulid) {
        Connection conn = connectionManager.getConnection();
        List<Stok> temp = new ArrayList<Stok>();

        String filter = (mamulid == null) ? "" : "where bilesenid = " + mamulid;
        String searchQuery = "select * from irfan.stokmamultum " + filter;

        logger.info(searchQuery);
        // connect to DB
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = conn.prepareStatement(searchQuery);
            logger.info(pstmt.toString());
            rs = pstmt.executeQuery();
            while (rs.next()) {
                temp.add(new Stok(rs.getInt("id"),
                        rs.getInt("bilesenid"),
                        rs.getString("bilesenad"),
                        rs.getString("bilesenkod"),
                        rs.getInt("bilesentipid"),
                        rs.getString("bilesentipad"),
                        rs.getInt("birimid"),
                        rs.getString("birimad"),
                        rs.getInt("firmaid"), rs.getString("firmaad"),
                        rs.getInt("miktar"),
                        null, // Stok: Kalan
                        rs.getString("gkrno"),
                        rs.getString("irsaliyeno"),
                        rs.getString("lot"),
                        rs.getTimestamp("giristarihi"),
                        rs.getTimestamp("cikistarihi"),
                        rs.getString("not"),
                        (rs.getString("mamuldetay")).split("\\|")[0], /* 0: Miktar */
                        (rs.getString("mamuldetay")).split("\\|")[1], /* 1: GkrNo */
                        (rs.getString("mamuldetay")).split("\\|")[2], /* 1: StokId */
                        rs.getInt("islemyonu"),
                        rs.getString("bilesenkod"))
                );
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

    public List<Stok> stokListeRapor(String tablename, int offset, String filterPage) {
        Connection conn = connectionManager.getConnection();
        List<Stok> temp = new ArrayList<Stok>();

        String pageFilter = (offset == 0) ? "" : " offset " + (offset - 1) * Genel.ROWPERPAGE + " limit " + Genel.ROWPERPAGE;
        String filter0 = filterPage;

        String searchQuery = "SELECT e.*, f.ad firmaad FROM irfan." + tablename
                + " e JOIN irfan.firma f on f.id = e.firmaid" + " WHERE 1=1 " + filter0 + " and islemyonu = 0"
                + " ORDER BY e.giristarihi, e.id " + pageFilter;

        logger.info(searchQuery);
        // connect to DB
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = conn.prepareStatement(searchQuery);
            logger.info(pstmt.toString());
            rs = pstmt.executeQuery();
            while (rs.next()) {
                temp.add(new Stok(rs.getInt("id"), rs.getInt("bilesenid"), rs.getString("bilesenad"),
                        rs.getString("bilesenkod"), rs.getInt("bilesentipid"), rs.getString("bilesentipad"),
                        rs.getInt("birimid"), rs.getString("birimad"), rs.getInt("firmaid"), rs.getString("firmaad"),
                        rs.getInt("miktar"), null, // rs.getInt("kalan"),
                        rs.getString("gkrno"), rs.getString("irsaliyeno"), rs.getString("lot"),
                        rs.getTimestamp("giristarihi"), rs.getTimestamp("cikistarihi"), rs.getString("not"), null, // rs.getString("miktarstr"),
                        null, // rs.getString("gkrnostr"),
                        null, // rs.getString("stokidstr"),
                        rs.getInt("islemyonu"), null // rs.getString("mamulkod")
                ));
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

    public List<Stok> stokListeGetirTum(BilesenTip bilesentip, Integer bilesenid, int offset, Map<String, String> param) {
        Connection conn = connectionManager.getConnection();
        List<Stok> temp = new ArrayList<Stok>();

        Integer rowperpage = Genel.ROWPERPAGE;
        String where = " where 1=1 ";

        if (param != null) {
            rowperpage = Integer.valueOf(param.get("rowperpage"));
        }

        String pageFilter = (offset == 0) ? "" : " offset " + (offset - 1) * rowperpage + " limit " + rowperpage;

        String join0 = (bilesentip != null && bilesentip.value() == BilesenTip.MAMUL.value())
                ? "join irfan.mamul b on b.id=s.bilesenid " : "join irfan.bilesen b on b.id=s.bilesenid ";
        String filter0 = (bilesentip == null) ? "" : "and b.bilesentipid IN (" + bilesentip.id() + ") ";

        if (bilesenid != 0) {
            filter0 = filter0 + " and s.bilesenid = " + bilesenid + " ";
        }

        String filter1 = (param == null) ? " " : " and islemyonu=" + param.get("islemyonu");

        String filter2 = (param == null) ? " "
                : (param.get("bas_tarih") != null)
                ? " and ( giristarihi BETWEEN '" + Util.date_tr_to_eng(param.get("bas_tarih")) + "' AND '"
                + Util.date_tr_to_eng(param.get("bit_tarih")) + "')"
                : "";

        String searchQuery = "select m2.kod mamulkod, s.*, b.ad bilesenad, b.kod bilesenkod, "
                + "bt.id bilesentipid, bt.ad bilesentipad, br.id birimid, br.ad birimad, "
                + "f.id firmaid, f.ad firmaad, (sum(CASE WHEN s.islemyonu = 0 THEN s.miktar ELSE -1*miktar END) "
                + "OVER (ORDER BY CASE WHEN s.islemyonu=0 THEN date(s.giristarihi) ELSE date(s.cikistarihi) END, s.id)) kalan "
                + "from irfan.stok s "
                + "left join irfan.uretimplan p on p.id=s.planid "
                + "left join irfan.mamul m2 on m2.id = p.mamulid " + join0
                + "join irfan.bilesentip bt on bt.id = b.bilesentipid "
                + "left join irfan.birim br on br.id = b.birimid " + "join irfan.firma f on f.id = b.firmaid " + where
                + filter0 + filter1 + filter2
                + "order by case when s.islemyonu=0 then date(s.giristarihi) else date(s.cikistarihi) end desc, id desc "
                + pageFilter;

        // connect to DB
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = conn.prepareStatement(searchQuery);
            logger.info(pstmt.toString());
            rs = pstmt.executeQuery();
            while (rs.next()) {
                temp.add(new Stok(rs.getInt("id"), rs.getInt("bilesenid"), rs.getString("bilesenad"),
                        rs.getString("bilesenkod"), rs.getInt("bilesentipid"), rs.getString("bilesentipad"),
                        rs.getInt("birimid"), rs.getString("birimad"), rs.getInt("firmaid"), rs.getString("firmaad"),
                        rs.getInt("miktar"), rs.getInt("kalan"), rs.getString("gkrno"), rs.getString("irsaliyeno"),
                        rs.getString("lot"), rs.getTimestamp("giristarihi"), rs.getTimestamp("cikistarihi"),
                        rs.getString("not"), null, null, null, rs.getInt("islemyonu"), rs.getString("mamulkod")));
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

    public List<StokRapor> stokBilesenRapor(String tablename, String filter, int offset) {
        Connection conn = connectionManager.getConnection();
        List<StokRapor> temp = new ArrayList<StokRapor>();

        String pageFilter = (offset == 0) ? "" : " offset " + (offset - 1) * Genel.ROWPERPAGE + " limit " + Genel.ROWPERPAGE;

        String searchQuery = "select t.*, f.ad firmaad, b.ad bilesentipad, -1::integer as islemyonu from ("
                + " select s.bilesenid, m.ad, m.kod, m.firmaid, m.bilesentipid, "
                + " sum(case when islemyonu = 0 then miktar else -1*miktar end) miktar, "
                + " max(case when islemyonu = 0 then giristarihi else cikistarihi end) tarih "
                + " from irfan.stok s "
                + " join irfan." + tablename + " on s.bilesenid =m.id "
                + " where 1=1 " + filter
                + " group by s.bilesenid, m.ad, m.kod, m.firmaid, m.bilesentipid) t "
                + " join irfan.firma f on f.id = t.firmaid "
                + " join irfan.bilesentip b on b.id=t.bilesentipid "
                + " order by t.ad " + pageFilter;

        // connect to DB
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = conn.prepareStatement(searchQuery);
            logger.info(pstmt.toString());
            rs = pstmt.executeQuery();
            while (rs.next()) {
                temp.add(new StokRapor(
                        null, //rs.getInt("id"),
                        null, //rs.getInt("stokid"),
                        rs.getInt("islemyonu"),
                        rs.getString("ad"),
                        rs.getString("kod"),
                        rs.getInt("bilesentipid"),
                        rs.getString("bilesentipad"),
                        rs.getInt("firmaid"),
                        rs.getString("firmaad"),
                        rs.getInt("miktar"),
                        null,// kalan
                        rs.getTimestamp("tarih")));
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

    public List<StokRapor> stokBilesenDetayRapor(String tablename, String filter, int offset, int bilesenid) {
        Connection conn = connectionManager.getConnection();
        List<StokRapor> temp = new ArrayList<StokRapor>();

        String pageFilter = (offset == 0) ? "" : " offset " + (offset - 1) * Genel.ROWPERPAGE + " limit " + Genel.ROWPERPAGE;

        String searchQuery = "SELECT m.*, s.id stokid, s.islemyonu, s.miktar, f.ad firmaad, bt.ad bilesentipad, "
                + " s.islemyonu, case when s.islemyonu=0 THEN s.giristarihi ELSE s.cikistarihi END as tarih, "
                + " sum(CASE s.islemyonu WHEN 1 THEN (-1) * s.miktar ELSE 1 * s.miktar END) OVER "
                + " (PARTITION BY s.bilesenid ORDER BY s.id) kalan "
                + " FROM irfan." + tablename
                + " LEFT JOIN irfan.stok s on s.bilesenid=m.id "
                + " JOIN irfan.firma f ON f.id=m.firmaid "
                + " JOIN irfan.bilesentip bt ON bt.id=m.bilesentipid"
                + " WHERE 1=1 " + filter
                + " ORDER BY (case when s.islemyonu=0 THEN s.giristarihi ELSE s.cikistarihi END) desc, stokid DESC "
                + pageFilter;

        // connect to DB
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = conn.prepareStatement(searchQuery);
            logger.info(pstmt.toString());
            rs = pstmt.executeQuery();
            while (rs.next()) {
                temp.add(new StokRapor(
                        rs.getInt("id"),
                        rs.getInt("stokid"),
                        rs.getInt("islemyonu"),
                        rs.getString("ad"),
                        rs.getString("kod"),
                        rs.getInt("bilesentipid"),
                        rs.getString("bilesentipad"),
                        rs.getInt("firmaid"),
                        rs.getString("firmaad"),
                        rs.getInt("miktar"),
                        rs.getInt("kalan"),
                        rs.getTimestamp("tarih")));
            }
        } catch (SQLException e) {
            logger.error(e.getMessage());
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException e) {
                logger.error(e.getMessage());
            }
        }
        return temp;
    }
}
