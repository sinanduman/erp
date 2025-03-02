package erp.dao;

import erp.entity.Stok;
import erp.entity.StokRapor;
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
import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Singleton
public class StokDao implements Serializable, BaseDao {
    @Inject
    private ConnectionManager connectionManager;
    private static final Logger logger = LoggerFactory.getLogger(StokDao.class);

    public String stokKontrol(String tarih, String gkrno, String miktar, String iade, String bilesenid, String bilesentipid) {
        Integer sum = null;
        String result = "0";
        String selectQuery = Fields.SELECT + "sum(case when islemyonu =0 then miktar else -1*miktar end)"
                + Fields.FROM + BaseDao.STOK
                + Fields.WHERE + "bilesenid =? and gkrno =?"
                + Fields.GROUP_BY + "gkrno";

        // connect to DB
        ResultSet rs;
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(selectQuery)) {
            pstmt.setInt(1, Integer.parseInt(bilesenid));
            pstmt.setLong(2, Long.parseLong(gkrno));
            long start = System.currentTimeMillis();
            rs = pstmt.executeQuery();
            logger.info(Constants.LOG_FORMAT, pstmt, (System.currentTimeMillis() - start));
            if (rs.next()) {
                sum = rs.getInt(1);
            }
        } catch (Exception e) {
            logger.error(ExceptionUtils.getStackTrace(e));
        }
        if (Boolean.parseBoolean(iade)) {
            if (sum == null) sum = 0;
            String resStr = "";
            Double hammaddeMiktar = 0.0;
            if (Integer.parseInt(miktar) > sum) {
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
        String retVal = "0";
        String insertQuery = Fields.SELECT + BaseDao.F_STOK_EKLE + "(?,?,?,?,?,?,?,?)";

        try (Connection conn = connectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(insertQuery)) {
            pstmt.setNull(1, Types.INTEGER); // planid
            pstmt.setInt(2, Integer.parseInt(bilesenid));
            pstmt.setBigDecimal(3, BigDecimal.valueOf(Double.parseDouble(miktar)));
            if (gkrno != null)
                pstmt.setLong(4, Long.parseLong(gkrno));
            else
                pstmt.setNull(4, Types.BIGINT);
            if (irsaliyeno != null)
                pstmt.setString(5, irsaliyeno);
            else
                pstmt.setNull(5, Types.VARCHAR);
            if (lot != null)
                pstmt.setString(6, lot);
            else
                pstmt.setNull(6, Types.VARCHAR);
            if (not != null)
                pstmt.setString(7, not);
            else
                pstmt.setNull(7, Types.VARCHAR);
            if (tarih != null)
                pstmt.setTimestamp(8, Util.date_tr_to_timestamp(tarih));
            else
                pstmt.setNull(8, Types.TIMESTAMP);
            logger.info("{}", pstmt);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                retVal = rs.getString(1);
            }
        } catch (Exception e) {
            retVal = e.getMessage();
            logger.error(ExceptionUtils.getStackTrace(e));
        }
        return retVal;
    }

    public String stokDus(String bilesenid, String miktar, String irsaliyeno, String lot, String gkrno, String tarih, String not) {
        String retVal = "-1";
        String updateQuery = Fields.SELECT + BaseDao.F_STOK_DUS + "(?,?,?,?,?,?,?,?)";

        try (Connection conn = connectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(updateQuery)) {
            pstmt.setNull(1, Types.INTEGER); // Planid
            pstmt.setInt(2, Integer.parseInt(bilesenid));
            pstmt.setBigDecimal(3, BigDecimal.valueOf(Double.parseDouble(miktar)));
            if (gkrno != null)
                pstmt.setLong(4, Long.parseLong(gkrno));
            else
                pstmt.setNull(4, Types.BIGINT);
            if (irsaliyeno != null)
                pstmt.setString(5, irsaliyeno);
            else
                pstmt.setNull(5, Types.VARCHAR);
            if (lot != null)
                pstmt.setString(6, lot);
            else
                pstmt.setNull(6, Types.VARCHAR);
            if (not != null)
                pstmt.setString(7, not);
            else
                pstmt.setNull(7, Types.VARCHAR);
            if (tarih != null)
                pstmt.setTimestamp(8, Util.date_tr_to_timestamp(tarih));
            else
                pstmt.setNull(8, Types.TIMESTAMP);

            logger.info("{}", pstmt);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                retVal = rs.getString(1);
            }
        } catch (Exception e) {
            logger.error(ExceptionUtils.getStackTrace(e));
        }
        return retVal;
    }

    public String stokYeterli(Integer uretimplanid, Integer mamulid, Integer uretilenmiktar) {
        String retVal = "0";
        String insertQuery = Fields.SELECT + BaseDao.F_STOK_YETERLI + "(?,?,?)";

        try (Connection conn = connectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(insertQuery)) {
            pstmt.setInt(1, uretimplanid);
            pstmt.setInt(2, mamulid);
            pstmt.setInt(3, uretilenmiktar);

            logger.info("{}", pstmt);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                retVal = rs.getString(1);
            }
        } catch (Exception e) {
            logger.error(ExceptionUtils.getStackTrace(e));
        }
        return retVal;
    }

    public List<Stok> stokMamulListeGetirTum(Integer mamulid, Integer durum) {
        List<Stok> temp = new ArrayList<>();
        String filter0 = Fields.WHERE_11;
        String filter1 = (mamulid == null) ? "" : " and bilesenid = " + mamulid;
        String filter2 = (durum == null) ? "" : " and durum = " + durum;
        String searchQuery = Fields.SELECT + "*" + Fields.FROM + BaseDao.V_STOKMAMUL_TUM + filter0 + filter1 + filter2;
        logger.info(searchQuery);
        // connect to DB
        ResultSet rs;
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(searchQuery)) {
            long start = System.currentTimeMillis();
            rs = pstmt.executeQuery();
            logger.info(Constants.LOG_FORMAT, pstmt, (System.currentTimeMillis() - start));
            while (rs.next()) {
                temp.add(new Stok(rs.getInt(Fields.ID),
                        rs.getInt(Fields.BILESENID),
                        rs.getString(Fields.BILESENAD),
                        rs.getString(Fields.BILESENKOD),
                        rs.getInt(Fields.BILESENTIPID),
                        rs.getString(Fields.BILESENTIPAD),
                        rs.getInt(Fields.BIRIMID),
                        rs.getString(Fields.BIRIMAD),
                        rs.getInt(Fields.FIRMAID),
                        rs.getString(Fields.FIRMAAD),
                        rs.getLong(Fields.MIKTAR),
                        null, // Stok: Kalan
                        rs.getString(Fields.GKRNO),
                        rs.getString(Fields.IRSALIYENO),
                        rs.getString(Fields.LOT),
                        rs.getTimestamp(Fields.GIRISTARIHI),
                        rs.getTimestamp(Fields.CIKISTARIHI),
                        rs.getString(Fields.NOT),
                        (rs.getString(Fields.MAMULDETAY)).split("\\|")[0], /* 0: Miktar */
                        (rs.getString(Fields.MAMULDETAY)).split("\\|")[1], /* 1: GkrNo */
                        (rs.getString(Fields.MAMULDETAY)).split("\\|")[2], /* 1: StokId */
                        rs.getInt(Fields.ISLEMYONU),
                        rs.getString(Fields.BILESENKOD))
                );
            }
        } catch (Exception e) {
            logger.error(ExceptionUtils.getStackTrace(e));
        }
        return temp;
    }

    public List<Stok> stokListeRapor(String tablename, int offset, String filterPage) {
        List<Stok> temp = new ArrayList<>();

        String pageFilter = Util.getPageFilter(offset);
        String filter0 = filterPage;

        String searchQuery = "SELECT e.*, f.ad firmaad "
                + Fields.FROM + tablename + " e"
                + Fields.JOIN + BaseDao.FIRMA + " f on f.id = e.firmaid"
                + Fields.WHERE_11 + filter0 + " and islemyonu = 0"
                + Fields.ORDER_BY + " e.giristarihi, e.id "
                + pageFilter;

        logger.info(searchQuery);
        // connect to DB
        ResultSet rs;
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(searchQuery)) {
            logger.info("{}", pstmt);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                temp.add(new Stok(
                        rs.getInt(Fields.ID),
                        rs.getInt(Fields.BILESENID),
                        rs.getString(Fields.BILESENAD),
                        rs.getString(Fields.BILESENKOD),
                        rs.getInt(Fields.BILESENTIPID),
                        rs.getString(Fields.BILESENTIPAD),
                        rs.getInt(Fields.BIRIMID),
                        rs.getString(Fields.BIRIMAD),
                        rs.getInt(Fields.FIRMAID),
                        rs.getString(Fields.FIRMAAD),
                        rs.getLong(Fields.MIKTAR),
                        null, // rs.getInt("kalan"),
                        rs.getString(Fields.GKRNO),
                        rs.getString(Fields.IRSALIYENO),
                        rs.getString(Fields.LOT),
                        rs.getTimestamp(Fields.GIRISTARIHI),
                        rs.getTimestamp(Fields.CIKISTARIHI),
                        rs.getString(Fields.NOT),
                        null, // rs.getString("miktarstr"),
                        null, // rs.getString("gkrnostr"),
                        null, // rs.getString("stokidstr"),
                        rs.getInt(Fields.ISLEMYONU),
                        null // rs.getString("mamulkod")
                ));
            }

        } catch (Exception e) {
            logger.error(ExceptionUtils.getStackTrace(e));
        }
        return temp;
    }

    public List<Stok> stokListeGetirTum(BilesenTip bilesentip, Integer bilesenid, int offset, Map<String, String> param) {
        List<Stok> temp = new ArrayList<>();

        String pageFilter = Util.getPageFilter(offset, param);
        String join0 = Fields.JOIN + bilesentip.tablo() + " b on b.id=s.bilesenid";
        String filter0 = " and b.bilesentipid IN (" + bilesentip.id() + ") ";

        if (bilesenid != 0)
            filter0 = filter0 + " and s.bilesenid = " + bilesenid;

        String filter1 = (param == null) ? "" : " and islemyonu=" + param.get(Fields.ISLEMYONU);
        String filter2 = (param == null) ? ""
                : (param.get(Fields.BAS_TARIH) != null)
                ? " and ( giristarihi BETWEEN '" + Util.date_tr_to_eng(param.get(Fields.BAS_TARIH)) + "' AND '"
                + Util.date_tr_to_eng(param.get(Fields.BIT_TARIH)) + "')"
                : "";
        String order = (param != null && param.get(Fields.SIRALAMA) != null) ? param.get(Fields.SIRALAMA) : " islemtarihi ";
        String ascdesc = (param != null && param.get(Fields.SIRALAMAYON) != null) ? param.get(Fields.SIRALAMAYON) : " desc ";

        String searchQuery = "select m2.kod mamulkod, s.*, b.ad bilesenad, b.kod bilesenkod, "
                + " coalesce(s.giristarihi, s.cikistarihi) as islemtarihi, "
                + " bt.id bilesentipid, bt.ad bilesentipad, br.id birimid, br.ad birimad, "
                + " f.id firmaid, f.ad firmaad, '' as mamuldetay, (sum(CASE WHEN s.islemyonu = 0 THEN s.miktar ELSE -1*miktar END) OVER (ORDER BY coalesce(s.giristarihi, s.cikistarihi), s.id desc)) kalan "
                + Fields.FROM + BaseDao.STOK + " s "
                + Fields.LEFT_JOIN + BaseDao.URETIMPLAN + " p on p.id = s.planid"
                + Fields.LEFT_JOIN + BaseDao.MAMUL + " m2 on m2.id = p.mamulid "
                + join0
                + Fields.JOIN + BaseDao.BILESENTIP + " bt on bt.id = b.bilesentipid"
                + Fields.LEFT_JOIN + BaseDao.BIRIM + " br on br.id = b.birimid"
                + Fields.JOIN + BaseDao.FIRMA + " f on f.id = b.firmaid "
                + Fields.WHERE_11 + filter0 + filter1 + filter2
                + Fields.ORDER_BY + order + ascdesc
                + pageFilter;

        // connect to DB
        ResultSet rs;
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(searchQuery)) {
            logger.info("{}", pstmt);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                temp.add(new Stok(rs));
            }
        } catch (Exception e) {
            logger.error(ExceptionUtils.getStackTrace(e));
        }
        return temp;
    }

    public List<StokRapor> stokBilesenRapor(String tablename, String filter, int offset) {
        List<StokRapor> temp = new ArrayList<>();

        String pageFilter = Util.getPageFilter(offset);
        String searchQuery = "select * from " + tablename
                + filter
                + pageFilter;

        // connect to DB
        ResultSet rs;
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(searchQuery)) {
            logger.info("{}", pstmt);
            rs = pstmt.executeQuery();
            int index = 0;
            while (rs.next()) {
                temp.add(new StokRapor(
                        ++index,
                        rs.getInt(Fields.BILESENID),
                        rs.getInt(Fields.ISLEMYONU),
                        rs.getString(Fields.AD),
                        rs.getString(Fields.KOD),
                        rs.getInt(Fields.BILESENTIPID),
                        rs.getString(Fields.BILESENTIPAD),
                        rs.getInt(Fields.FIRMAID),
                        rs.getString(Fields.FIRMAAD),
                        rs.getInt(Fields.MIKTAR),
                        null,// kalan
                        rs.getTimestamp(Fields.TARIH)));
            }
        } catch (Exception e) {
            logger.error(ExceptionUtils.getStackTrace(e));
        }
        return temp;
    }

    public List<StokRapor> stokBilesenDetayRapor(String tablename, String filter, int offset) {
        List<StokRapor> temp = new ArrayList<>();

        String pageFilter = Util.getPageFilter(offset);
        String searchQuery = Fields.SELECT + "*"
                + Fields.FROM + tablename
                + filter
                + pageFilter;

        // connect to DB
        ResultSet rs;
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(searchQuery)) {
            logger.info("{}", pstmt);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                temp.add(new StokRapor(rs));
            }
        } catch (Exception e) {
            logger.error(ExceptionUtils.getStackTrace(e));
        }
        return temp;
    }

    public int stokEsikHammadde() {
        int retVal = 0;
        String searchQuery = "SELECT count(*) as hammadde_sayi from " + BaseDao.V_STOKESIK_HAMMADDE;

        try (Connection conn = connectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(searchQuery)) {
            logger.info("{}", pstmt);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                retVal = rs.getInt("hammadde_sayi");
            }
        } catch (Exception e) {
            logger.error(ExceptionUtils.getStackTrace(e));
        }
        return retVal;
    }

    public int stokEsikYarimamul() {
        int retVal = 0;
        String searchQuery = "SELECT count(*) as yarimamul_sayi from " + BaseDao.V_STOKESIK_YARIMAMUL;

        try (Connection conn = connectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(searchQuery)) {
            logger.info("{}", pstmt);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                retVal = rs.getInt("yarimamul_sayi");
            }
        } catch (Exception e) {
            logger.error(ExceptionUtils.getStackTrace(e));
        }
        return retVal;
    }

    public int stokEsikMamul() {
        int retVal = 0;
        String searchQuery = "SELECT count(*) as mamul_sayi from " + BaseDao.V_STOKESIK_MAMUL;

        try (Connection conn = connectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(searchQuery)) {
            logger.info("{}", pstmt);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                retVal = rs.getInt("mamul_sayi");
            }
        } catch (Exception e) {
            logger.error(ExceptionUtils.getStackTrace(e));
        }
        return retVal;
    }
}