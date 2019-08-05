package erp.dao;

import erp.irfan.ConnectionManager;
import erp.irfan.entity.Depo;
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
public class DepoDao implements Serializable, BaseDao {
    @Inject
    ConnectionManager connectionManager;
    private static final Logger logger = LoggerFactory.getLogger(DepoDao.class);

    public List<Depo> depoListeGetirTum() {
        Connection conn = connectionManager.getConnection();
        List<Depo> temp = new ArrayList<Depo>();
        String searchQuery = "select * from irfan.depo order by ad";

        // connect to DB
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = conn.prepareStatement(searchQuery);
            logger.info(pstmt.toString());
            rs = pstmt.executeQuery();
            while (rs.next()) {
                temp.add(new Depo(rs.getInt("id"), rs.getString("ad")));
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
