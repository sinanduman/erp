package erp.dao;

import erp.entity.Genel;
import erp.entity.IzlemeNo;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Singleton
public class IzlemeNoDao implements Serializable, BaseDao {
    @Inject
    private ConnectionManager connectionManager;
    private static final Logger logger = LoggerFactory.getLogger(IzlemeNoDao.class);

    public List<IzlemeNo> izlemeNoTum(String tablename, String filter, int offset) {
        List<IzlemeNo> temp = new ArrayList<>();

        Map<String, String> param = new HashMap<>();
        param.put("rowperpage", String.valueOf(Genel.ROWPERLONGPAGE));

        String pageFilter = Util.getPageFilter(offset, param);
        String searchQuery = Fields.SELECT + "*"
                + Fields.FROM + "(SELECT * FROM " + tablename + ") e"
                + Fields.WHERE + "1=1 " + filter
                + Fields.ORDER_BY + "e.kullanildi_tarih DESC, e.gkrno DESC " + pageFilter;

        // connect to DB
        ResultSet rs;
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(searchQuery)) {
            long start = System.currentTimeMillis();
            rs = pstmt.executeQuery();
            logger.info(Constants.LOG_FORMAT, pstmt, (System.currentTimeMillis() - start));
            while (rs.next()) {
                temp.add(new IzlemeNo(
                        rs.getInt(Fields.ID),
                        rs.getInt(Fields.GKRNO),
                        rs.getDate("kullanildi_tarih"),
                        rs.getString(Fields.KOD),
                        rs.getString(Fields.AD),
                        rs.getInt(Fields.MIKTAR),
                        rs.getInt(Fields.KULLANILDI))
                );
            }
        } catch (Exception e) {
            logger.error(ExceptionUtils.getStackTrace(e));
        }
        return temp;
    }

    public List<IzlemeNo> izlemeNoUret(String mamulid, String kullanildi) {
        List<IzlemeNo> temp = new ArrayList<IzlemeNo>();
        String updateQuery = Fields.INSERT_INTO + BaseDao.IZLEMENO + "(mamulid) values(?)";

        // connect to DB
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(updateQuery)) {
            long start = System.currentTimeMillis();
            pstmt.setInt(1, Integer.parseInt(mamulid));
            pstmt.executeUpdate();
            logger.info(Constants.LOG_FORMAT, pstmt, (System.currentTimeMillis() - start));
            temp = izlemeNoKontrol(mamulid, kullanildi, null);
        } catch (Exception e) {
            logger.error(ExceptionUtils.getStackTrace(e));
        }
        return temp;
    }

    public List<IzlemeNo> izlemeNoSil(String mamulid, String gkrno, String kullanildi) {
        List<IzlemeNo> temp = new ArrayList<>();
        String updateQuery = Fields.DELETE
                + Fields.FROM + BaseDao.IZLEMENO
                + Fields.WHERE + "mamulid =? and gkrno =? and kullanildi = 0";

        // connect to DB
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(updateQuery)) {
            long start = System.currentTimeMillis();
            pstmt.setInt(1, Integer.parseInt(mamulid));
            pstmt.setInt(2, Integer.parseInt(gkrno));
            pstmt.executeUpdate();
            logger.info(Constants.LOG_FORMAT, pstmt, (System.currentTimeMillis() - start));
            temp = izlemeNoKontrol(mamulid, kullanildi, null);
        } catch (Exception e) {
            logger.error(ExceptionUtils.getStackTrace(e));
        }
        return temp;
    }

    public List<IzlemeNo> izlemeNoKontrol(String mamulid, String kullanildi, Integer rownumber) {
        rownumber = (rownumber == null) ? Genel.ROWPERPAGE : rownumber;
        List<IzlemeNo> temp = new ArrayList<>();
        String updateQuery = Fields.SELECT + "i.*, m.ad mamulad, m.kod mamulkod"
                + Fields.FROM + BaseDao.IZLEMENO + " i"
                + Fields.JOIN + BaseDao.MAMUL + " m on m.id=i.mamulid"
                + Fields.WHERE + "mamulid = ? and kullanildi <= ? "
                + Fields.ORDER_BY + "gkrno desc limit ?";

        // connect to DB
        ResultSet rs;
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(updateQuery)) {
            long start = System.currentTimeMillis();
            pstmt.setInt(1, Integer.parseInt(mamulid));
            pstmt.setInt(2, Integer.parseInt(kullanildi));
            pstmt.setInt(3, rownumber);
            rs = pstmt.executeQuery();
            logger.info(Constants.LOG_FORMAT, pstmt, (System.currentTimeMillis() - start));
            while (rs.next()) {
                temp.add(new IzlemeNo(
                        rs.getInt(Fields.MAMULID),
                        rs.getInt(Fields.GKRNO),
                        rs.getDate("kullanildi_tarih"),
                        rs.getString(Fields.MAMULKOD),
                        rs.getString(Fields.MAMULAD),
                        0, /* Ilk Uretim */
                        rs.getInt(Fields.KULLANILDI))
                );
            }
        } catch (Exception e) {
            logger.error(ExceptionUtils.getStackTrace(e));
        }
        return temp;
    }
}
