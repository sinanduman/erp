package erp.dao;

import erp.entity.UretimPlan;
import erp.model.UretimDurum;
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
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Singleton
public class UretimDao implements Serializable, BaseDao {
    @Inject
    private ConnectionManager connectionManager;
    private static final Logger logger = LoggerFactory.getLogger(UretimDao.class);

    public String uretimPlanSil(Integer uretimplanid) {
        String retVal = "0";
        String deleteQuery = Fields.SELECT + BaseDao.F_PLAN_SIL + "(?)";
        // connect to DB
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(deleteQuery)) {
            pstmt.setInt(1, uretimplanid);
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

    public String uretimPlanOk(Integer uretimplanid) {
        String retVal = "0";
        String planQuery = "SELECT " + BaseDao.F_PLAN_OK + "(?) ";
        // connect to DB
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(planQuery)) {
            pstmt.setInt(1, uretimplanid);
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

    public int uretimTakipEkleGuncelle(String mamulid, String uretilenmiktar, String hatalimiktar, String makinaid, String calisanid, String tarih, String baszaman, String bitzaman, String hataid, String durusid, String duruszaman, Integer mamulizleno, Integer uretimplanid, Integer planlananmiktar) {
        int result = -1;
        String insertQuery;

        if (uretimplanid == 0) {
            insertQuery = Fields.INSERT_INTO + BaseDao.URETIMPLAN + " (mamulid, uretilenmiktar, hatalimiktar,"
                    + " makinaid, calisanid, tarih, baszaman, bitzaman, hataid, durusid,"
                    + " duruszaman, mamulizleno, planlananmiktar)"
                    + " values (?,?,?,?,?,?,?,?,?,?,?,?,?)";
        } else {
            insertQuery = Fields.UPDATE + BaseDao.URETIMPLAN + " set mamulid =?, uretilenmiktar =?,"
                    + " hatalimiktar =?, makinaid =?, calisanid =?, tarih =?, baszaman =?,"
                    + " bitzaman =?, hataid =?, durusid =?, duruszaman =?, mamulizleno =?, planlananmiktar=?"
                    + " where id =?";
        }
        // connect to DB
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(insertQuery)) {
            pstmt.setInt(1, Integer.parseInt(mamulid));
            pstmt.setInt(2, Integer.parseInt(uretilenmiktar));

            if (hatalimiktar == null || hatalimiktar.trim().isEmpty())
                pstmt.setNull(3, Types.INTEGER);
            else
                pstmt.setInt(3, Integer.parseInt(hatalimiktar));

            pstmt.setInt(4, Integer.parseInt(makinaid));
            pstmt.setInt(5, Integer.parseInt(calisanid));

            pstmt.setDate(6, Date.valueOf(Util.date_tr_to_eng(tarih)));
            pstmt.setTime(7, Time.valueOf(baszaman));
            pstmt.setTime(8, Time.valueOf(bitzaman));

            if (hataid == null || hataid.trim().isEmpty())
                pstmt.setNull(9, Types.INTEGER);
            else
                pstmt.setInt(9, Integer.parseInt(hataid));

            if (durusid == null || durusid.trim().isEmpty())
                pstmt.setNull(10, Types.INTEGER);
            else
                pstmt.setInt(10, Integer.parseInt(durusid));

            if (duruszaman == null || duruszaman.trim().isEmpty())
                pstmt.setNull(11, Types.INTEGER);
            else
                pstmt.setInt(11, Integer.parseInt(duruszaman));

            pstmt.setInt(12, mamulizleno);
            pstmt.setInt(13, planlananmiktar);

            if (uretimplanid != 0) { /* Guncelleme */
                pstmt.setInt(14, uretimplanid);
            }

            logger.info("{}", pstmt);
            result = pstmt.executeUpdate();
        } catch (Exception e) {
            logger.error(ExceptionUtils.getStackTrace(e));
        }
        return result;
    }

    public String uretimPlanGuncelle(Integer gerceklesenmiktar, Integer hatalimiktar, Integer hataid, Integer uretimplanid) {
        String result = "0";
        String updateQuery = Fields.UPDATE + BaseDao.URETIMPLAN
                + " set uretilenmiktar =?, hatalimiktar =?, hataid =?"
                + " where id =?";
        // connect to DB
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(updateQuery)) {
            pstmt.setInt(1, gerceklesenmiktar);
            pstmt.setInt(2, hatalimiktar);
            pstmt.setInt(3, hataid);
            pstmt.setInt(4, uretimplanid);
            logger.info("{}", pstmt);

            pstmt.executeUpdate();
        } catch (Exception e) {
            result = e.getMessage();
            logger.error(ExceptionUtils.getStackTrace(e));
        }
        return result;
    }

    public List<UretimPlan> uretimPlanListeGetir(UretimDurum uretimdurum, int offset, String filter, final String view) {
        List<UretimPlan> temp = new ArrayList<UretimPlan>();

        String pageFilter = Util.getPageFilter(offset);
        String filter0 = " where tamamlandi=" + uretimdurum.value() + " " + filter;
        String searchQuery = "select * from " + view + " v " /* view */
                + filter0
                + " order by v.id "
                + pageFilter;
        ResultSet rs;
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(searchQuery)) {
            long start = System.currentTimeMillis();
            rs = pstmt.executeQuery();
            logger.info(Constants.LOG_FORMAT, pstmt, (System.currentTimeMillis() - start));
            while (rs.next()) {
                temp.add(new UretimPlan(
                        rs.getInt(Fields.ID),
                        rs.getInt(Fields.MAMULID),
                        rs.getString(Fields.MAMULAD),
                        rs.getString(Fields.MAMULKOD),
                        Util.mamulInfoDecompose(rs.getString(Fields.HAMMADDE), 1), /* -- 1: hammadde adi icin */
                        rs.getString(Fields.YARIMAMUL),
                        rs.getInt("planlananmiktar"),
                        rs.getInt("uretilenmiktar"),
                        rs.getInt("hatalimiktar"),
                        rs.getInt(Fields.MAKINAID),
                        rs.getString("makinaad"),
                        rs.getInt(Fields.FIRMAID),
                        rs.getString(Fields.FIRMAAD),
                        rs.getInt(Fields.CALISANID),
                        rs.getString("calisanad"),
                        rs.getString("calisansoyad"),
                        rs.getInt("hataid"),
                        rs.getString("hataad"),
                        (rs.getString("hatakodu") == null) ? "" : (rs.getString("hatakodu")),
                        rs.getInt("durusid"),
                        rs.getString("durusad"),
                        (rs.getString("duruskodu") == null) ? "" : (rs.getString("duruskodu")),
                        rs.getInt("duruszaman"),
                        rs.getDate(Fields.TARIH),
                        rs.getTime(Fields.BASZAMAN),
                        rs.getTime(Fields.BITZAMAN),
                        rs.getInt(Fields.MAMULIZLENO),
                        (rs.getString(Fields.HAMMADDE) == null) ? "" : Util.mamulInfoDecompose(rs.getString(Fields.HAMMADDE), 4), /** -- 1: hammadde izle no adi icin */
                        (rs.getString(Fields.YARIMAMUL) == null) ? "" : Util.mamulInfoDecompose(rs.getString(Fields.YARIMAMUL), 4), /** -- 1: yarimamul izle no adi icin */
                        rs.getInt("tamamlandi"),
                        (rs.getString(Fields.YARIMAMUL) == null) ? "" : Util.mamulInfoDecompose(rs.getString(Fields.HAMMADDE), 3), /** -- 3: hammadde toplam agirlik icin */
                        rs.getInt("cevrimsuresi"))
                );
            }
        } catch (Exception e) {
            logger.error(ExceptionUtils.getStackTrace(e));
        }
        return temp;
    }

    public List<UretimPlan> uretimPlanListeGetirExcel(UretimDurum uretimdurum, String tarih, final String excelsql) {
        List<UretimPlan> temp = new ArrayList<UretimPlan>();

        logger.info("excel: " + excelsql);
        // String searchQuery ="SELECT * FROM irfan.uretimplantum WHERE
        // tamamlandi=? AND tarih=? "; /* view */
        ResultSet rs;
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(excelsql)) {
            long start = System.currentTimeMillis();
            rs = pstmt.executeQuery();
            logger.info(Constants.LOG_FORMAT, pstmt, (System.currentTimeMillis() - start));
            while (rs.next()) {
                temp.add(new UretimPlan(
                        rs.getInt(Fields.ID),
                        rs.getInt(Fields.MAMULID),
                        rs.getString(Fields.MAMULAD),
                        rs.getString(Fields.MAMULKOD),
                        Util.mamulInfoDecompose(rs.getString(Fields.HAMMADDE), 1), /* -- 1: hammadde adi icin */
                        rs.getString(Fields.YARIMAMUL),
                        rs.getInt("planlananmiktar"),
                        rs.getInt("uretilenmiktar"),
                        rs.getInt("hatalimiktar"),
                        rs.getInt(Fields.MAKINAID),
                        rs.getString("makinaad"),
                        rs.getInt(Fields.FIRMAID),
                        rs.getString(Fields.FIRMAAD),
                        rs.getInt(Fields.CALISANID),
                        rs.getString("calisanad"),
                        rs.getString("calisansoyad"),
                        rs.getInt(Fields.HATAID),
                        rs.getString("hataad"),
                        (rs.getString("hatakodu") == null) ? "" : (rs.getString("hatakodu")),
                        rs.getInt(Fields.DURUSID),
                        rs.getString("durusad"),
                        (rs.getString("duruskodu") == null) ? "" : (rs.getString("duruskodu")),
                        rs.getInt("duruszaman"),
                        rs.getDate(Fields.TARIH),
                        rs.getTime(Fields.BASZAMAN),
                        rs.getTime(Fields.BITZAMAN),
                        rs.getInt(Fields.MAMULIZLENO),
                        (rs.getString(Fields.HAMMADDE) == null) ? "" : Util.mamulInfoDecompose(rs.getString(Fields.HAMMADDE), 4), /** -- 1: hammadde izle no adi icin */
                        (rs.getString(Fields.YARIMAMUL) == null) ? "" : Util.mamulInfoDecompose(rs.getString(Fields.YARIMAMUL), 4), /** -- 1: yarimamul izle no adi icin */
                        rs.getInt("tamamlandi"),
                        (rs.getString(Fields.YARIMAMUL) == null) ? "" : Util.mamulInfoDecompose(rs.getString(Fields.HAMMADDE), 3), /** -- 3: hammadde toplam agirlik icin */
                        rs.getInt("cevrimsuresi"))
                );
            }
        } catch (Exception e) {
            logger.error(ExceptionUtils.getStackTrace(e));
        }
        return temp;
    }

    public List<UretimPlan> uretimBilesenRapor(String tablename, int offset, String filterPage) {
        List<UretimPlan> temp = new ArrayList<UretimPlan>();
        String pageFilter = Util.getPageFilter(offset);
        String searchQuery = "SELECT * FROM " + tablename
                + " WHERE 1=1 " + filterPage
                + " ORDER BY tarih, id " + pageFilter;

        ResultSet rs;
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(searchQuery)) {
            long start = System.currentTimeMillis();
            rs = pstmt.executeQuery();
            logger.info(Constants.LOG_FORMAT, pstmt, (System.currentTimeMillis() - start));
            while (rs.next()) {
                temp.add(new UretimPlan(
                        rs.getInt(Fields.ID),
                        rs.getInt(Fields.MAMULID),
                        rs.getString(Fields.MAMULAD),
                        rs.getString(Fields.MAMULKOD),
                        Util.mamulInfoDecompose(rs.getString(Fields.HAMMADDE), 1), /* -- 1: hammadde adi icin */
                        rs.getString(Fields.YARIMAMUL),
                        rs.getInt("planlananmiktar"),
                        rs.getInt("uretilenmiktar"),
                        rs.getInt("hatalimiktar"),
                        rs.getInt(Fields.MAKINAID),
                        rs.getString("makinaad"),
                        rs.getInt(Fields.FIRMAID),
                        rs.getString(Fields.FIRMAAD),
                        rs.getInt(Fields.CALISANID),
                        rs.getString("calisanad"),
                        rs.getString("calisansoyad"),
                        rs.getInt(Fields.HATAID),
                        rs.getString("hataad"),
                        rs.getString("hatakodu"),
                        rs.getInt(Fields.DURUSID),
                        rs.getString("durusad"),
                        rs.getString("duruskodu"),
                        rs.getInt("duruszaman"),
                        rs.getDate(Fields.TARIH),
                        rs.getTime(Fields.BASZAMAN),
                        rs.getTime(Fields.BITZAMAN),
                        rs.getInt(Fields.MAMULIZLENO),
                        (rs.getString(Fields.HAMMADDE) == null) ? "" : Util.mamulInfoDecompose(rs.getString(Fields.HAMMADDE), 4), /** -- 1: hammadde izle no adi icin */
                        (rs.getString(Fields.YARIMAMUL) == null) ? "" : Util.mamulInfoDecompose(rs.getString(Fields.YARIMAMUL), 4), /** -- 1: yarimamul izle no adi icin */
                        rs.getInt("tamamlandi"),
                        (rs.getString(Fields.YARIMAMUL) == null) ? "" : Util.mamulInfoDecompose(rs.getString(Fields.HAMMADDE), 3), /** -- 3: hammadde toplam agirlik icin */
                        rs.getInt("cevrimsuresi"))
                );
            }
        } catch (Exception e) {
            logger.error(ExceptionUtils.getStackTrace(e));
        }
        return temp;
    }
}