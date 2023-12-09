package erp.dao;

import erp.servlet.ConnectionManager;
import erp.entity.Siparis;
import erp.model.SiparisTip;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.Serializable;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Singleton
public class SiparisDao implements Serializable, BaseDao {
    @Inject
    ConnectionManager connectionManager;
    private static final Logger logger = LoggerFactory.getLogger(SiparisDao.class);

    public List<Siparis> siparisEkle(String bilesenid, String miktar, String not) {
        List<Siparis> temp = new ArrayList<Siparis>();
        String insertQuery = "insert into " + BaseDao.SIPARIS + " (bilesenid, miktar, \"not\") values (?,?,?)";

        // connect to DB
        ResultSet rs;
        int newSiparisId = 0;
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setInt(1, Integer.parseInt(bilesenid));
            pstmt.setInt(2, Integer.parseInt(miktar));
            pstmt.setString(3, not);
            logger.info(pstmt.toString());
            pstmt.executeUpdate();
            rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                newSiparisId = rs.getInt(1);
            }
            temp = siparisListeGetir(SiparisTip.BEKLEYEN);
        } catch (Exception e) {
            logger.error(ExceptionUtils.getStackTrace(e));
        }
        return temp;
    }

    public List<Siparis> siparisListeGetir(SiparisTip siparis) {
        List<Siparis> temp = new ArrayList<Siparis>();

        String filter = "";
        if (siparis == SiparisTip.BEKLEYEN) {
            filter = " where s.bitistarih is null ";
        } else if (siparis == SiparisTip.TAMAMLANMIS) {
            filter = " where s.bitistarih is not null ";
        }

        String searchQuery = "select s.*, b.ad bilesenad,"
                + " coalesce((select sum(miktar) from " + BaseDao.SIPARISPLAN + " sp where sp.siparisid=s.id),0) as planmiktar,"
                + " coalesce((select sum(miktar) from " + BaseDao.SIPARISPLAN + " sp where sp.siparisid=s.id and sp.tamamlandi=1),0) as tamammiktar"
                + " from " + BaseDao.SIPARIS + " s"
                + " join " + BaseDao.BILESEN + " b on b.id=s.bilesenid"
                + filter
                + " order by s.id";

        ResultSet rs;
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(searchQuery)) {
            logger.info(pstmt.toString());
            rs = pstmt.executeQuery();
            while (rs.next()) {
                temp.add(new Siparis(rs));
            }
        } catch (Exception e) {
            logger.error(ExceptionUtils.getStackTrace(e));
        }
        return temp;
    }
}
