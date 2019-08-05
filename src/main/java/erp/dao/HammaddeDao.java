package erp.dao;

import erp.irfan.ConnectionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@Singleton
public class HammaddeDao implements Serializable, BaseDao {
    @Inject
    ConnectionManager connectionManager;
    private static final Logger logger = LoggerFactory.getLogger(HammaddeDao.class);

    public String hammaddeEkle(String kod, String ad, String birimid, String firmaid, String bilesentip) {
        Connection conn = connectionManager.getConnection();
        String result = "0";
        String insertQuery = "insert into irfan.bilesen (kod, ad, birimid, bilesentipid, firmaid) values (?,?,?,?,?) ";

        // connect to DB
        PreparedStatement pstmt = null;
        try {
            pstmt = conn.prepareStatement(insertQuery);
            pstmt.setString(1, kod);
            pstmt.setString(2, ad);
            pstmt.setInt(3, Integer.valueOf(birimid));
            pstmt.setInt(4, Integer.valueOf(bilesentip));
            pstmt.setInt(5, Integer.valueOf(firmaid));
            logger.info(pstmt.toString());

            pstmt.executeUpdate();
        } catch (SQLException e) {
            logger.error("Error: " + e);
            result = e.getMessage();
        } finally {
            try {
                if (pstmt != null) {
                    pstmt.close();
                }
            } catch (SQLException e) {
                logger.error("Error: " + e);
                result = e.getMessage();
            }
        }
        return result;
    }
}
