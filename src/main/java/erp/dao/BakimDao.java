package erp.dao;

import erp.entity.Bakim;
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
public class BakimDao implements Serializable, BaseDao {
    @Inject
    private ConnectionManager connectionManager;
    private static final Logger logger = LoggerFactory.getLogger(BakimDao.class);

    public List<Bakim> bakimRapor(String tablename, int offset, String filterPage, String mamulId) {
        List<Bakim> temp = new ArrayList<>();
        String pageFilter = Util.getPageFilter(offset);
        String searchQuery = Fields.SELECT + "*"
                + Fields.FROM + tablename
                + Fields.WHERE + "1=1 " + filterPage
                + " " + pageFilter;

        ResultSet rs;
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(searchQuery)) {
            long start = System.currentTimeMillis();
            rs = pstmt.executeQuery();
            logger.info(Constants.LOG_FORMAT, pstmt, (System.currentTimeMillis() - start));
            while (rs.next()) {
                temp.add(new Bakim(rs));
            }
        } catch (Exception e) {
            logger.error(ExceptionUtils.getStackTrace(e));
        }
        return temp;
    }

    public int bakimZamaniGelmis(String tablename, String filterPage) {
        int retVal = 0;
        String searchQuery = Fields.SELECT + "count(*) mamul_sayi "
                + Fields.FROM + tablename
                + Fields.WHERE + "1=1 " + filterPage;

        try (Connection conn = connectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(searchQuery)) {
            long start = System.currentTimeMillis();
            ResultSet rs = pstmt.executeQuery();
            logger.info(Constants.LOG_FORMAT, pstmt, (System.currentTimeMillis() - start));
            if (rs.next()) {
                retVal = rs.getInt("mamul_sayi");
            }
        } catch (Exception e) {
            logger.error(ExceptionUtils.getStackTrace(e));
        }
        return retVal;
    }
}