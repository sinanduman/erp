package erp.dao;

import erp.servlet.ConnectionManager;
import erp.entity.DurusSebep;
import erp.entity.HataSebep;
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
public class HataDao implements Serializable, BaseDao {
    @Inject
    ConnectionManager connectionManager;
    private static final Logger logger = LoggerFactory.getLogger(HataDao.class);

    public List<DurusSebep> durusSebepListeGetir() {
        List<DurusSebep> temp = new ArrayList<DurusSebep>();
        String searchQuery = "select * from " + BaseDao.DURUSSEBEP + " order by ad";

        // connect to DB
        ResultSet rs;
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(searchQuery)) {
            logger.info(pstmt.toString());
            rs = pstmt.executeQuery();
            while (rs.next()) {
                temp.add(new DurusSebep(rs));
            }
        } catch (SQLException e) {
            logger.error("Error: " + e);
        }
        return temp;
    }

    public List<HataSebep> hataSebepListeGetir() {
        List<HataSebep> temp = new ArrayList<HataSebep>();
        String searchQuery = "select * from " + BaseDao.HATASEBEP + " order by ad";

        // connect to DB
        ResultSet rs;
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(searchQuery)) {
            logger.info(pstmt.toString());
            rs = pstmt.executeQuery();
            while (rs.next()) {
                temp.add(new HataSebep(rs));
            }
        } catch (SQLException e) {
            logger.error("Error: " + e);
        }
        return temp;
    }
}
