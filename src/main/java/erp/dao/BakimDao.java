package erp.dao;

import erp.entity.Bakim;
import erp.entity.UretimPlan;
import erp.model.UretimDurum;
import erp.servlet.ConnectionManager;
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
public class BakimDao implements Serializable, BaseDao {
    @Inject
    ConnectionManager connectionManager;
    private static final Logger logger = LoggerFactory.getLogger(BakimDao.class);

    public List<Bakim> bakimRapor(String tablename, int offset, String filterPage, String mamulId) {
        List<Bakim> temp = new ArrayList<Bakim>();
        String pageFilter = Util.getPageFilter (offset);
        String searchQuery = "SELECT * FROM " + tablename
                + " WHERE 1=1 " + filterPage
                + " " + pageFilter;

        ResultSet rs;
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(searchQuery)) {
            long start = System.currentTimeMillis();
            rs = pstmt.executeQuery();
            logger.info(pstmt.toString() + (System.currentTimeMillis()-start) +"ms.");
            while (rs.next()) {
                temp.add(new Bakim(rs));
            }
        } catch (SQLException e) {
            logger.error(e.getMessage());
        }
        return temp;
    }

    public int bakimZamaniGelmis(String tablename, String filterPage) {
        int retVal = 0;
        String searchQuery = "SELECT count(*) mamul_sayi FROM " + tablename + " WHERE 1=1 " + filterPage;

        try (Connection conn = connectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(searchQuery)) {
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                retVal = rs.getInt("mamul_sayi");
            }
        } catch (SQLException e) {
            logger.error(e.getMessage());
        }
        return retVal;
    }
}