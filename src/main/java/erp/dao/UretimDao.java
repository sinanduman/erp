package erp.dao;

import erp.servlet.ConnectionManager;
import erp.model.UretimDurum;
import erp.entity.UretimPlan;
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
public class UretimDao implements Serializable, BaseDao {
    @Inject
    ConnectionManager connectionManager;
    private static final Logger logger = LoggerFactory.getLogger(UretimDao.class);

    public String uretimPlanSil(Integer uretimplanid) {
        String retVal = "0";
        String deleteQuery = "select " + BaseDao.F_PLAN_SIL + "(?)";
        // connect to DB
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(deleteQuery)) {
            pstmt.setInt(1, uretimplanid);
            logger.info(pstmt.toString());

            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                retVal = rs.getString(1);
            }
        } catch (SQLException e) {
            logger.error("Error: " + e);
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
            logger.info(pstmt.toString());

            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                retVal = rs.getString(1);
            }
        } catch (SQLException e) {
            logger.error("Error: " + e);
        }
        return retVal;
    }

    public int uretimTakipEkleGuncelle(String mamulid, String uretilenmiktar, String hatalimiktar, String makinaid, String calisanid, String tarih, String baszaman, String bitzaman, String hataid, String durusid, String duruszaman, Integer mamulizleno, Integer uretimplanid, Integer planlananmiktar) {
        int result = -1;
        String insertQuery;

        if (uretimplanid == 0) {
            insertQuery = "insert into " + BaseDao.URETIMPLAN + " (mamulid, uretilenmiktar, hatalimiktar,"
                    + " makinaid, calisanid, tarih, baszaman, bitzaman, hataid, durusid,"
                    + " duruszaman, mamulizleno, planlananmiktar)"
                    + " values (?,?,?,?,?,?,?,?,?,?,?,?,?)";
        } else {
            insertQuery = "update " + BaseDao.URETIMPLAN + " set mamulid =?, uretilenmiktar =?,"
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

            logger.info(pstmt.toString());
            result = pstmt.executeUpdate();
        } catch (SQLException e) {
            logger.error("Error: " + e);
        }
        return result;
    }

    public String uretimPlanGuncelle(Integer gerceklesenmiktar, Integer planlananmiktar, Integer hatalimiktar, Integer hataid, Integer uretimplanid) {
        String result = "0";
        String updateQuery = "update " + BaseDao.URETIMPLAN
                + " set uretilenmiktar =?, planlananmiktar =?, hatalimiktar =?, hataid =?"
                + " where id =?";
        // connect to DB
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(updateQuery)) {
            pstmt.setInt(1, gerceklesenmiktar);
            pstmt.setInt(2, planlananmiktar);
            pstmt.setInt(3, hatalimiktar);
            pstmt.setInt(4, hataid);
            pstmt.setInt(5, uretimplanid);
            logger.info(pstmt.toString());

            pstmt.executeUpdate();
        } catch (SQLException e) {
            result = e.getMessage();
            logger.error("Error: " + e);
        }
        return result;
    }

    public List<UretimPlan> uretimPlanListeGetir(UretimDurum uretimdurum, int offset, String filter, final String view) {
        List<UretimPlan> temp = new ArrayList<UretimPlan>();

        String pageFilter = Util.getPageFilter (offset);
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
            logger.info(pstmt.toString() + (System.currentTimeMillis()-start) +"ms.");
            while (rs.next()) {
                temp.add(new UretimPlan(
                        rs.getInt("id"),
                        rs.getInt("mamulid"),
                        rs.getString("mamulad"),
                        rs.getString("mamulkod"),
                        Util.mamulInfoDecompose(rs.getString("hammadde"),1), /* -- 1: hammadde adi icin */
                        rs.getString("yarimamul"),
                        rs.getInt("planlananmiktar"),
                        rs.getInt("uretilenmiktar"),
                        rs.getInt("hatalimiktar"),
                        rs.getInt("makinaid"),
                        rs.getString("makinaad"),
                        rs.getInt("firmaid"),
                        rs.getString("firmaad"),
                        rs.getInt("calisanid"),
                        rs.getString("calisanad"),
                        rs.getString("calisansoyad"),
                        rs.getInt("hataid"),
                        rs.getString("hataad"),
                        (rs.getString("hatakodu") == null) ? "" : (rs.getString("hatakodu")),
                        rs.getInt("durusid"),
                        rs.getString("durusad"),
                        (rs.getString("duruskodu") == null) ? "" : (rs.getString("duruskodu")),
                        rs.getInt("duruszaman"),
                        rs.getDate("tarih"),
                        rs.getTime("baszaman"),
                        rs.getTime("bitzaman"),
                        rs.getInt("mamulizleno"),
                        (rs.getString("hammadde") == null) ? "" : Util.mamulInfoDecompose(rs.getString("hammadde"),4), /** -- 1: hammadde izle no adi icin */
                        (rs.getString("yarimamul") == null) ? "" : Util.mamulInfoDecompose(rs.getString("yarimamul"),4), /** -- 1: yarimamul izle no adi icin */
                        rs.getInt("tamamlandi"),
                        (rs.getString("yarimamul") == null) ? "" : Util.mamulInfoDecompose(rs.getString("hammadde"),3), /** -- 3: hammadde toplam agirlik icin */
                        rs.getInt("cevrimsuresi"))
                );
            }
        } catch (SQLException e) {
            logger.error("Error: " + e);
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
            logger.info(pstmt.toString() + (System.currentTimeMillis()-start) +"ms.");

            while (rs.next()) {
                temp.add(new UretimPlan(
                        rs.getInt("id"),
                        rs.getInt("mamulid"),
                        rs.getString("mamulad"),
                        rs.getString("mamulkod"),
                        Util.mamulInfoDecompose(rs.getString("hammadde"),1), /* -- 1: hammadde adi icin */
                        rs.getString("yarimamul"),
                        rs.getInt("planlananmiktar"),
                        rs.getInt("uretilenmiktar"),
                        rs.getInt("hatalimiktar"),
                        rs.getInt("makinaid"),
                        rs.getString("makinaad"),
                        rs.getInt("firmaid"),
                        rs.getString("firmaad"),
                        rs.getInt("calisanid"),
                        rs.getString("calisanad"),
                        rs.getString("calisansoyad"),
                        rs.getInt("hataid"),
                        rs.getString("hataad"),
                        (rs.getString("hatakodu") == null) ? "" : (rs.getString("hatakodu")),
                        rs.getInt("durusid"),
                        rs.getString("durusad"),
                        (rs.getString("duruskodu") == null) ? "" : (rs.getString("duruskodu")),
                        rs.getInt("duruszaman"),
                        rs.getDate("tarih"),
                        rs.getTime("baszaman"),
                        rs.getTime("bitzaman"),
                        rs.getInt("mamulizleno"),
                        (rs.getString("hammadde") == null) ? "" : Util.mamulInfoDecompose(rs.getString("hammadde"),4), /** -- 1: hammadde izle no adi icin */
                        (rs.getString("yarimamul") == null) ? "" : Util.mamulInfoDecompose(rs.getString("yarimamul"),4), /** -- 1: yarimamul izle no adi icin */
                        rs.getInt("tamamlandi"),
                        (rs.getString("yarimamul") == null) ? "" : Util.mamulInfoDecompose(rs.getString("hammadde"),3), /** -- 3: hammadde toplam agirlik icin */
                        rs.getInt("cevrimsuresi"))
                );
            }
        } catch (SQLException e) {
            logger.error("Error: " + e);
        }
        return temp;
    }

    public List<UretimPlan> uretimBilesenRapor(String tablename, int offset, String filterPage) {
        List<UretimPlan> temp = new ArrayList<UretimPlan>();
        String pageFilter = Util.getPageFilter (offset);
        String searchQuery = "SELECT * FROM " + tablename
                + " WHERE 1=1 " + filterPage
                + " ORDER BY tarih, id " + pageFilter;

        ResultSet rs;
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(searchQuery)) {
            long start = System.currentTimeMillis();
            rs = pstmt.executeQuery();
            logger.info(pstmt.toString() + (System.currentTimeMillis()-start) +"ms.");
            while (rs.next()) {
                temp.add(new UretimPlan(
                        rs.getInt("id"),
                        rs.getInt("mamulid"),
                        rs.getString("mamulad"),
                        rs.getString("mamulkod"),
                        Util.mamulInfoDecompose(rs.getString("hammadde"), 1), /* -- 1: hammadde adi icin */
                        rs.getString("yarimamul"),
                        rs.getInt("planlananmiktar"),
                        rs.getInt("uretilenmiktar"),
                        rs.getInt("hatalimiktar"),
                        rs.getInt("makinaid"),
                        rs.getString("makinaad"),
                        rs.getInt("firmaid"),
                        rs.getString("firmaad"),
                        rs.getInt("calisanid"),
                        rs.getString("calisanad"),
                        rs.getString("calisansoyad"),
                        rs.getInt("hataid"),
                        rs.getString("hataad"),
                        rs.getString("hatakodu"),
                        rs.getInt("durusid"),
                        rs.getString("durusad"),
                        rs.getString("duruskodu"),
                        rs.getInt("duruszaman"),
                        rs.getDate("tarih"),
                        rs.getTime("baszaman"),
                        rs.getTime("bitzaman"),
                        rs.getInt("mamulizleno"),
                        (rs.getString("hammadde") == null) ? "" : Util.mamulInfoDecompose(rs.getString("hammadde"), 4), /** -- 1: hammadde izle no adi icin */
                        (rs.getString("yarimamul") == null) ? "" : Util.mamulInfoDecompose(rs.getString("yarimamul"), 4), /** -- 1: yarimamul izle no adi icin */
                        rs.getInt("tamamlandi"),
                        (rs.getString("yarimamul") == null) ? "" : Util.mamulInfoDecompose(rs.getString("hammadde"), 3), /** -- 3: hammadde toplam agirlik icin */
                        rs.getInt("cevrimsuresi"))
                );
            }
        } catch (SQLException e) {
            logger.error(e.getMessage());
        }
        return temp;
    }
}