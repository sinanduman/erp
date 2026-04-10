package erp.dao;

import erp.entity.Calisan;
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
import java.util.ArrayList;
import java.util.List;

@Singleton
public class CalisanDao implements Serializable, BaseDao {
    @Inject
    private ConnectionManager connectionManager;
    private static final Logger logger = LoggerFactory.getLogger(CalisanDao.class);

    public List<Calisan> calisanListeGetirTum(int offset, Integer durum) {
        List<Calisan> temp = new ArrayList<>();
        String pageFilter = Util.getPageFilter(offset);
        String durumFilter = (durum != null && durum == 1) ? " where durum = 1 " : "";
        String searchQuery = Fields.SELECT + "*"
                + Fields.FROM + BaseDao.CALISAN
                + durumFilter
                + Fields.ORDER_BY + "durum desc, soyad, ad "
                + pageFilter;

        // connect to DB
        ResultSet rs;
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(searchQuery)) {
            long start = System.currentTimeMillis();
            rs = pstmt.executeQuery();
            logger.info(Constants.LOG_FORMAT, pstmt, (System.currentTimeMillis() - start));
            while (rs.next()) {
                temp.add(new Calisan(rs));
            }
        } catch (Exception e) {
            logger.error(ExceptionUtils.getStackTrace(e));
        }
        return temp;
    }

    public String calisanEkle(String ad, String soyad, String gorev) {
        String retVal = "0";
        String insertQuery = Fields.INSERT_INTO + BaseDao.CALISAN + " (ad, soyad, gorev) values (?,?,?)";

        // connect to DB
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(insertQuery)) {
            long start = System.currentTimeMillis();
            pstmt.setString(1, ad);
            pstmt.setString(2, soyad);
            pstmt.setString(3, gorev);
            pstmt.executeUpdate();
            logger.info(Constants.LOG_FORMAT, pstmt, (System.currentTimeMillis() - start));
        } catch (Exception e) {
            logger.error(ExceptionUtils.getStackTrace(e));
            retVal = e.getMessage();
        }
        return retVal;
    }

    public String calisanGuncelle(String id, String ad, String soyad, String gorev, String durum) {
        String result = "0";
        String updateQuery = Fields.UPDATE + BaseDao.CALISAN
                + " set ad =?, soyad =?, gorev =?, durum =?"
                + " where id =?";

        // connect to DB
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(updateQuery)) {
            long start = System.currentTimeMillis();
            pstmt.setString(1, ad);
            pstmt.setString(2, soyad);
            pstmt.setString(3, gorev);
            pstmt.setInt(4, Integer.parseInt(durum));
            pstmt.setInt(5, Integer.parseInt(id));
            pstmt.executeUpdate();
            logger.info(Constants.LOG_FORMAT, pstmt, (System.currentTimeMillis() - start));
        } catch (Exception e) {
            logger.error(ExceptionUtils.getStackTrace(e));
            result = e.getMessage();
        }
        return result;
    }

    public String calisanSil(String id) {
        String result = "0";
        String updateQuery = Fields.SELECT + BaseDao.F_CALISAN_SIL + "(?)";
        // connect to DB
        ResultSet rs;
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(updateQuery)) {
            pstmt.setInt(1, Integer.parseInt(id));
            long start = System.currentTimeMillis();
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
}
