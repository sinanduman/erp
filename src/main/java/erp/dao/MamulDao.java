package erp.dao;

import erp.servlet.ConnectionManager;
import erp.model.BilesenTip;
import erp.entity.Mamul;
import erp.entity.MamulBilesen;
import erp.util.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.Serializable;
import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Singleton
public class MamulDao implements Serializable, BaseDao {
    @Inject
    ConnectionManager connectionManager;
    private static final Logger logger = LoggerFactory.getLogger(MamulDao.class);

    public String mamulEkle(String ad, String kod, String cevrimSuresi, String firmaid, String figur, String mamulBaskiSayisi, String bakimTarihi, String calisanId) {
        String insertQuery = "insert into " + BaseDao.MAMUL + "(ad, kod, cevrimsuresi, bilesentipid, firmaid, figur, baskisayisi) values (?,?,?,?,?,?,?)";
        String newMamulId = "-1";
        ResultSet rs;
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, ad);
            pstmt.setString(2, kod);
            pstmt.setInt(3, Integer.parseInt(cevrimSuresi));
            pstmt.setInt(4, BilesenTip.MAMUL.value());
            pstmt.setInt(5, Integer.parseInt(firmaid));
            pstmt.setInt(6, Integer.parseInt(figur));
            pstmt.setInt(7, Integer.parseInt(mamulBaskiSayisi));
            logger.info(pstmt.toString());
            pstmt.executeUpdate();
            rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                newMamulId = String.valueOf(rs.getInt(1));
                if(rs.getInt(1) > 0){
                    int retVal = mamulBakimTarihEkle(Integer.parseInt(newMamulId), Integer.parseInt(calisanId), bakimTarihi);
                }
            }
        } catch (SQLException e) {
            logger.error("Error: " + e);
            newMamulId = e.getMessage();
        }
        return newMamulId;
    }

    public void mamulBilesenEkle(int mamulid, int bilesen, int birim, float miktar) {
        String insertQuery = "insert into " + BaseDao.MAMULBILESEN + " (mamulid, bilesenid, birimid, miktar) values (?,?,?,?)";
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(insertQuery)) {
            pstmt.setInt(1, mamulid);
            pstmt.setInt(2, bilesen);
            pstmt.setInt(3, birim);
            pstmt.setFloat(4, miktar);
            logger.info(pstmt.toString());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            logger.error("Error: " + e);
        }
    }

    public String mamulSil(String mamulid) {
        String result = "0";
        String updateQuery = "select " + BaseDao.F_MAMUL_SIL + "(?)";
        // connect to DB
        ResultSet rs;
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(updateQuery)) {
            pstmt.setInt(1, Integer.parseInt(mamulid));
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

    public String mamulGuncelle(String mamulid, String firmaid, String kod, String ad, String figur, String cevrimsuresi, String esmamulid, String durum, String kritikEsik, String bakimTarihi, String baskiSayisi, String calisanId) {
        String result = "0";
        String updateQuery = "update " + BaseDao.MAMUL + " set firmaid=?,kod=?,ad=?,figur=?,cevrimsuresi=?,esmamulid =?,durum =?,kritikesik=?,baskisayisi=? where id=?";
        // connect to DB
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(updateQuery)) {
            pstmt.setInt(1, Integer.parseInt(firmaid));
            pstmt.setString(2, kod);
            pstmt.setString(3, ad);
            pstmt.setInt(4, Integer.parseInt(figur));
            pstmt.setInt(5, Integer.parseInt(cevrimsuresi));
            if(esmamulid == null || esmamulid.isEmpty() || esmamulid.equals("0"))
                pstmt.setNull(6, Types.INTEGER);
            else
                pstmt.setInt(6, Integer.parseInt(esmamulid));
            pstmt.setInt(7, Integer.parseInt(durum));
            pstmt.setInt(8, Integer.parseInt(kritikEsik));
            pstmt.setInt(9, Integer.parseInt(baskiSayisi));
            pstmt.setInt(10, Integer.parseInt(mamulid));
            logger.info(pstmt.toString());
            int retVal = pstmt.executeUpdate();
            if(retVal > 0){
                retVal = mamulBakimTarihEkle(Integer.parseInt(mamulid), Integer.parseInt(calisanId), bakimTarihi);
            }
        } catch (SQLException e) {
            logger.error("Error: " + e);
            result = e.getMessage();
        }
        return result;
    }

    public String mamulBilesenGuncelle(String mamulid, String bilesenid, String miktar) {
        String result = "0";
        String updateQuery = "update " + BaseDao.MAMULBILESEN + " set miktar =? where mamulid =? and bilesenid =?";
        // connect to DB
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(updateQuery)) {
            pstmt.setFloat(1, Float.parseFloat(miktar));
            pstmt.setInt(2, Integer.parseInt(mamulid));
            pstmt.setInt(3, Integer.parseInt(bilesenid));
            logger.info(pstmt.toString());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            logger.error("Error: " + e);
            result = e.getMessage();
        }
        return result;
    }

    public List<Mamul> mamulListeGetir(Integer mamulid, int offset, Integer durum) {
        List<Mamul> temp = new ArrayList<Mamul>();
        String pageFilter = Util.getPageFilter(offset);
        String baseFilter = " where 1=1";
        String queryFilter0 = (mamulid == null) ? "" : " and m.id = " + mamulid;
        String queryFilter1 = (durum == null) ? "" : " and m.durum = " + durum;
        String queryFilter = baseFilter + queryFilter0 + queryFilter1;
        String searchQuery= "select * from ("
                + " select m.*, b.bakimtarihi, b.calisanid, rank() over(partition by b.mamulid order by b.id desc) ranko"
                + " from " + BaseDao.V_MAMUL_TUM + " m"
                + " left join " + BaseDao.BAKIM + " b on b.mamulid = m.id"
                + queryFilter
                + " ) x"
                + " where ranko = 1"
                + " ORDER BY ad"
                + pageFilter;

        // connect to DB
        ResultSet rs;
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(searchQuery)) {
            long start = System.currentTimeMillis();
            rs = pstmt.executeQuery();
            logger.info(pstmt.toString() + (System.currentTimeMillis() - start) + "ms.");
            while (rs.next()) {
                temp.add(new Mamul(rs));
            }
        } catch (SQLException e) {
            logger.error("Error: " + e);
        }
        return temp;
    }

    public List<Mamul> mamulListeStokGetir(Integer mamulid, int offset, Integer durum) {
        List<Mamul> temp = new ArrayList<Mamul>();
        String pageFilter = Util.getPageFilter(offset);
        String baseFilter = " where 1=1";
        String queryFilter0 = (mamulid == null) ? "" : " and id = " + mamulid;
        String queryFilter1 = (durum == null) ? "" : " and durum = " + durum;
        String queryFilter = baseFilter + queryFilter0 + queryFilter1;
        String searchQuery = "select * from " + BaseDao.V_MAMUL_STOK + queryFilter + " ORDER BY ad " + pageFilter; /* view */

        // connect to DB
        ResultSet rs;
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(searchQuery)) {
            long start = System.currentTimeMillis();
            rs = pstmt.executeQuery();
            logger.info(pstmt.toString() + (System.currentTimeMillis() - start) + "ms.");
            while (rs.next()) {
                temp.add(new Mamul(rs));
            }
        } catch (SQLException e) {
            logger.error("Error: " + e);
        }
        return temp;
    }

    public List<MamulBilesen> mamulBilesenListeGetirTum(Integer mamulid) {
        List<MamulBilesen> temp = new ArrayList<MamulBilesen>();
        String queryFilter = (mamulid == null) ? "" : " where mamulid = " + mamulid;
        String searchQuery = "select * from  " + BaseDao.V_MAMULBILESEN_TUM + queryFilter;
        // connect to DB
        ResultSet rs;
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(searchQuery)) {
            long start = System.currentTimeMillis();
            rs = pstmt.executeQuery();
            logger.info(pstmt.toString() + (System.currentTimeMillis() - start) + "ms.");
            while (rs.next()) {
                temp.add(new MamulBilesen(rs));
            }
        } catch (SQLException e) {
            logger.error("Error: " + e);
        }
        return temp;
    }

    public int mamulBakimTarihEkle(int mamulId, int calisanId, String bakimTarihi) {
        int retVal = -1;
        String insertQuery = "insert into " + BaseDao.BAKIM + " (mamulid, calisanid, bakimtarihi, islemtarihi) values (?,?,?,?)";
        ResultSet rs;
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(insertQuery)) {
            pstmt.setInt(1, mamulId);
            pstmt.setInt(2, calisanId);
            pstmt.setDate (3, Util.convertToSqlDate(LocalDate.parse(bakimTarihi, DateTimeFormatter.ofPattern("dd-MM-yyyy"))));
            pstmt.setDate(4, Util.convertToSqlDate(LocalDate.now()));
            logger.info(pstmt.toString());
            retVal = pstmt.executeUpdate();
        } catch (SQLException e) {
            logger.error("Error: " + e);
        }
        return retVal;
    }
}