package erp.dao;

import erp.entity.Makina;
import erp.servlet.ConnectionManager;
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
public class MakinaDao implements Serializable, BaseDao {
    @Inject
    private ConnectionManager connectionManager;
    private static final Logger logger = LoggerFactory.getLogger(MakinaDao.class);

    public List<Makina> makinaListeGetirTum(Integer offset) {
        List<Makina> temp = new ArrayList<>();
        String pageFilter = Util.getPageFilter(offset);
        String searchQuery = Fields.SELECT + "m.*, mt.ad makinatipad"
                + Fields.FROM + BaseDao.MAKINA + " m"
                + Fields.JOIN + BaseDao.MAKINATIP + " mt on mt.id=m.makinatipid"
                + Fields.ORDER_BY + "m.ad "
                + pageFilter;

        // connect to DB
        ResultSet rs;
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(searchQuery)) {
            logger.info("{}", pstmt);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                temp.add(new Makina(rs));
            }
        } catch (Exception e) {
            logger.error(ExceptionUtils.getStackTrace(e));
        }
        return temp;
    }

    public String makinaEkle(String ad, String makinatip) {
        String retVal = "0";
        String insertQuery = Fields.INSERT_INTO + BaseDao.MAKINA + " (ad, makinatipid) values (?,?)";

        // connect to DB
        ResultSet rs;
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(insertQuery)) {
            pstmt.setString(1, ad);
            pstmt.setInt(2, Integer.parseInt(makinatip));
            logger.info("{}", pstmt);
            pstmt.executeUpdate();

        } catch (Exception e) {
            logger.error(ExceptionUtils.getStackTrace(e));
            retVal = e.getMessage();
        }
        return retVal;
    }

    public String makinaSil(String id) {
        String result = "0";
        String updateQuery = Fields.SELECT + BaseDao.F_MAKINA_SIL + "(?)";
        // connect to DB
        ResultSet rs;
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(updateQuery)) {
            pstmt.setInt(1, Integer.parseInt(id));
            logger.info("{}", pstmt);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                result = rs.getString(1);
            }
        } catch (Exception e) {
            logger.error(ExceptionUtils.getStackTrace(e));
            result = e.getMessage();
        }
        return result;
    }

    public String makinaGuncelle(String id, String ad, String tip) {
        String result = "0";
        String updateQuery = Fields.UPDATE + BaseDao.MAKINA + " set ad =?, makinatipid =? where id =?";

        // connect to DB
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(updateQuery)) {
            pstmt.setString(1, ad);
            pstmt.setInt(2, Integer.parseInt(tip));
            pstmt.setInt(3, Integer.parseInt(id));
            logger.info("{}", pstmt);
            pstmt.executeUpdate();
        } catch (Exception e) {
            logger.error(ExceptionUtils.getStackTrace(e));
            result = e.getMessage();
        }
        return result;
    }
}