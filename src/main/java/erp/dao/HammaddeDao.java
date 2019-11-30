package erp.dao;

import erp.servlet.ConnectionManager;
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

    public String hammaddeEkle(String kod, String ad, String birimid, String firmaid, String bilesentip, String fiyatId, String fiyat, String kritikEsik) {
        String result = "0";
        String insertQuery = "insert into " + BaseDao.BILESEN + " (kod, ad, birimid, bilesentipid, firmaid, fiyatid, fiyat, kritikesik) values (?,?,?,?,?,?,?,?) ";
        // connect to DB
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(insertQuery)) {
            pstmt.setString(1, kod);
            pstmt.setString(2, ad);
            pstmt.setInt(3, Integer.parseInt(birimid));
            pstmt.setInt(4, Integer.parseInt(bilesentip));
            pstmt.setInt(5, Integer.parseInt(firmaid));
            pstmt.setInt(6, Integer.parseInt(fiyatId));
            pstmt.setDouble(7, Double.parseDouble(fiyat));
            pstmt.setInt(8, Integer.parseInt(kritikEsik));
            logger.info(pstmt.toString());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            logger.error("Error: " + e);
            result = e.getMessage();
        }
        return result;
    }
}
