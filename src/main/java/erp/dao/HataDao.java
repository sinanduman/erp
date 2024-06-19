package erp.dao;

import erp.entity.DurusSebep;
import erp.entity.HataSebep;
import erp.servlet.ConnectionManager;
import erp.util.Constants;
import erp.util.Fields;
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
public class HataDao implements Serializable, BaseDao {
    private static final Logger logger = LoggerFactory.getLogger(HataDao.class);
    @Inject
    private ConnectionManager connectionManager;

    public List<DurusSebep> durusSebepListeGetir() {
        List<DurusSebep> temp = new ArrayList<>();
        String searchQuery = Fields.SELECT + "*"
                + Fields.FROM + BaseDao.DURUSSEBEP
                + Fields.ORDER_BY + "ad";

        // connect to DB
        ResultSet rs;
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(searchQuery)) {
            long start = System.currentTimeMillis();
            rs = pstmt.executeQuery();
            logger.info(Constants.LOG_FORMAT, pstmt, (System.currentTimeMillis() - start));
            while (rs.next()) {
                temp.add(new DurusSebep(rs));
            }
        } catch (Exception e) {
            logger.error(ExceptionUtils.getStackTrace(e));
        }
        return temp;
    }

    public List<HataSebep> hataSebepListeGetir() {
        List<HataSebep> temp = new ArrayList<>();
        String searchQuery = Fields.SELECT + "*"
                + Fields.FROM + BaseDao.HATASEBEP
                + Fields.ORDER_BY + "ad";

        // connect to DB
        ResultSet rs;
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(searchQuery)) {
            long start = System.currentTimeMillis();
            rs = pstmt.executeQuery();
            logger.info(Constants.LOG_FORMAT, pstmt, (System.currentTimeMillis() - start));
            while (rs.next()) {
                temp.add(new HataSebep(rs));
            }
        } catch (Exception e) {
            logger.error(ExceptionUtils.getStackTrace(e));
        }
        return temp;
    }
}
