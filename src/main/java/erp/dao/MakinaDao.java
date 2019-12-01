package erp.dao;

import erp.servlet.ConnectionManager;
import erp.entity.Makina;
import erp.util.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Singleton
public class MakinaDao implements Serializable, BaseDao {
    @Inject
    ConnectionManager connectionManager;
    private static final Logger logger = LoggerFactory.getLogger(MakinaDao.class);

    public List<Makina> makinaListeGetirTum(Integer offset) {
        List<Makina> temp = new ArrayList<Makina>();
        String pageFilter = Util.getPageFilter(offset);
        String searchQuery = "select m.*, mt.ad makinatipad"
                + " from " + BaseDao.MAKINA + " m"
                + " join " + BaseDao.MAKINATIP + " mt on mt.id=m.makinatipid"
                + " order by m.ad "
                + pageFilter;

        // connect to DB
        ResultSet rs = null;
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(searchQuery)) {
            rs = pstmt.executeQuery();
            logger.info(pstmt.toString());
            while (rs.next()) {
                temp.add(new Makina(rs));
            }
        } catch (SQLException e) {
            logger.error("Error: " + e);
        }
        return temp;
    }

    public String makinaEkle(String ad, String makinatip) {
        String retVal = "0";
        String insertQuery = "insert into " + BaseDao.MAKINA + " (ad, makinatipid) values (?,?)";

        // connect to DB
        ResultSet rs = null;
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(insertQuery)) {
            pstmt.setString(1, ad);
            pstmt.setInt(2, Integer.parseInt(makinatip));
            logger.info(pstmt.toString());
            pstmt.executeUpdate();

        } catch (SQLException e) {
            logger.error("Error: " + e);
            retVal = e.getMessage();
        }
        return retVal;
    }

    public String makinaSil(String id) {
        String result = "0";
        String updateQuery = "select " + BaseDao.F_MAKINA_SIL + "(?)";
        // connect to DB
        ResultSet rs = null;
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(updateQuery)) {
            pstmt.setInt(1, Integer.parseInt(id));
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

    public String makinaGuncelle(String id, String ad, String tip) {
        String result = "0";
        String updateQuery = "update " + BaseDao.MAKINA + " set ad =?, makinatipid =? where id =?";

        // connect to DB
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(updateQuery)) {
            pstmt.setString(1, ad);
            pstmt.setInt(2, Integer.parseInt(tip));
            pstmt.setInt(3, Integer.parseInt(id));
            logger.info(pstmt.toString());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            logger.error("Error: " + e);
            result = e.getMessage();
        }
        return result;
    }
}