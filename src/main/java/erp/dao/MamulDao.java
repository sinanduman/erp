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
import java.util.ArrayList;
import java.util.List;

@Singleton
public class MamulDao implements Serializable, BaseDao {
    @Inject
    ConnectionManager connectionManager;
    private static final Logger logger = LoggerFactory.getLogger(MamulDao.class);

    public String mamulEkle(String ad, String kod, String cevrimSuresi, String firmaid, String figur) {
        String insertQuery = "insert into " + BaseDao.MAMUL + "(ad, kod, cevrimsuresi, bilesentipid, firmaid, figur) values (?,?,?,?,?,?)";
        String newMamulId = "-1";
        ResultSet rs = null;
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, ad);
            pstmt.setString(2, kod);
            pstmt.setInt(3, Integer.valueOf(cevrimSuresi));
            pstmt.setInt(4, BilesenTip.MAMUL.value());
            pstmt.setInt(5, Integer.valueOf(firmaid));
            pstmt.setInt(6, Integer.valueOf(figur));
            logger.info(pstmt.toString());
            pstmt.executeUpdate();
            rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                newMamulId = String.valueOf(rs.getInt(1));
            }
        } catch (SQLException e) {
            logger.error("Error: " + e);
            newMamulId = e.getMessage();
        }
        return newMamulId;
    }

    public void mamulBilesenEkle(int mamulid, int bilesen, int birim, float miktar) {
        String insertQuery = "insert into " + BaseDao.MAMULBILESEN + " (mamulid, bilesenid, birimid, miktar) values (?,?,?,?)";
        ResultSet rs = null;
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
        ResultSet rs = null;
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(updateQuery)) {
            pstmt.setInt(1, Integer.valueOf(mamulid));
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

    public String mamulGuncelle(String mamulid, String firmaid, String kod, String ad, String figur, String cevrimsuresi, String esmamulid, String durum) {
        String result = "0";
        String updateQuery = "update " + BaseDao.MAMUL + " set firmaid =?, kod =?, ad =?, figur =?, cevrimsuresi =?, esmamulid =?, durum =? where id =?";
        // connect to DB
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(updateQuery)) {
            pstmt.setInt(1, Integer.valueOf(firmaid));
            pstmt.setString(2, kod);
            pstmt.setString(3, ad);
            pstmt.setInt(4, Integer.valueOf(figur));
            pstmt.setInt(5, Integer.valueOf(cevrimsuresi));
            if(esmamulid == null || esmamulid.isEmpty() || esmamulid.equals("0"))
                pstmt.setNull(6, Types.INTEGER);
            else
                pstmt.setInt(6, Integer.valueOf(esmamulid));
            pstmt.setInt(7, Integer.valueOf(durum));
            pstmt.setInt(8, Integer.valueOf(mamulid));
            logger.info(pstmt.toString());
            pstmt.executeUpdate();
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
            pstmt.setFloat(1, Float.valueOf(miktar));
            pstmt.setInt(2, Integer.valueOf(mamulid));
            pstmt.setInt(3, Integer.valueOf(bilesenid));
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
        String queryFilter0 = (mamulid == null) ? "" : " and id = " + mamulid;
        String queryFilter1 = (durum == null) ? "" : " and durum = " + durum;
        String queryFilter = baseFilter + queryFilter0 + queryFilter1;
        String searchQuery = "select * from " + BaseDao.V_MAMUL_TUM + queryFilter + " ORDER BY kod " + pageFilter; /* view */

        // connect to DB
        ResultSet rs = null;
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
        String searchQuery = "select * from " + BaseDao.V_MAMUL_STOK + queryFilter + " ORDER BY kod " + pageFilter; /* view */

        // connect to DB
        ResultSet rs = null;
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
        ResultSet rs = null;
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
}