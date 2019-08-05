package erp.dao;

import erp.irfan.ConnectionManager;
import erp.irfan.entity.DurusSebep;
import erp.irfan.entity.HataSebep;
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
        Connection conn = connectionManager.getConnection();
        List<DurusSebep> temp = new ArrayList<DurusSebep>();

        String searchQuery = "select * from irfan.durussebep order by ad";

        // connect to DB
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = conn.prepareStatement(searchQuery);
            logger.info(pstmt.toString());
            rs = pstmt.executeQuery();
            while (rs.next()) {
                temp.add(new DurusSebep(rs.getInt("id"), rs.getString("ad"), rs.getString("kod")));
            }

        } catch (SQLException e) {
            logger.error("Error: " + e);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException e) {
                logger.error("Error: " + e);
            }
        }
        return temp;
    }

    public List<HataSebep> hataSebepListeGetir() {
        Connection conn = connectionManager.getConnection();
        List<HataSebep> temp = new ArrayList<HataSebep>();

        String searchQuery = "select * from irfan.hatasebep order by ad";

        // connect to DB
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = conn.prepareStatement(searchQuery);
            logger.info(pstmt.toString());
            rs = pstmt.executeQuery();
            while (rs.next()) {
                temp.add(new HataSebep(rs.getInt("id"), rs.getString("ad"), rs.getString("kod")));
            }
        } catch (SQLException e) {
            logger.error("Error: " + e);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException e) {
                logger.error("Error: " + e);
            }
        }
        return temp;
    }
}
