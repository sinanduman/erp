package erp.dao;

import erp.entity.Depo;
import erp.servlet.ConnectionManager;
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
public class DepoDao implements Serializable, BaseDao {
    private static final Logger logger = LoggerFactory.getLogger(DepoDao.class);
    @Inject
    private ConnectionManager connectionManager;

    public List<Depo> depoListeGetirTum() {
        List<Depo> temp = new ArrayList<>();
        String searchQuery = Fields.SELECT + "* "
                + Fields.FROM + BaseDao.DEPO
                + Fields.ORDER_BY + "ad";

        // connect to DB
        ResultSet rs;
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(searchQuery)) {
            logger.info("{}", pstmt);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                temp.add(new Depo(rs));
            }
        } catch (Exception e) {
            logger.error(ExceptionUtils.getStackTrace(e));
        }
        return temp;
    }
}
