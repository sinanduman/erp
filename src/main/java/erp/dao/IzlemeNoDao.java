package erp.dao;

import erp.irfan.ConnectionManager;
import erp.irfan.entity.Genel;
import erp.irfan.entity.IzlemeNo;
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
public class IzlemeNoDao implements Serializable, BaseDao {
    @Inject
    ConnectionManager connectionManager;
    private static final Logger logger = LoggerFactory.getLogger(IzlemeNoDao.class);

    public List<IzlemeNo> izlemeNoTum(String tablename, String filter, int offset) {
        Connection conn = connectionManager.getConnection();
        List<IzlemeNo> temp = new ArrayList<IzlemeNo>();

        String pageFilter = (offset == 0) ? "" : " OFFSET " + (offset - 1) * Genel.ROWPERLONGPAGE + " LIMIT " + Genel.ROWPERLONGPAGE;

        String searchQuery = "SELECT * FROM (SELECT * FROM irfan." + tablename + ") e"
                + " WHERE 1=1 " + filter
                + " ORDER BY e.kullanildi_tarih DESC, e.gkrno DESC" + pageFilter;

        // connect to DB
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = conn.prepareStatement(searchQuery);
            logger.info(pstmt.toString());
            rs = pstmt.executeQuery();
            while (rs.next()) {
                temp.add(new IzlemeNo(
                        rs.getInt("id"),
                        rs.getInt("gkrno"),
                        rs.getDate("kullanildi_tarih"),
                        rs.getString("kod"),
                        rs.getString("ad"),
                        rs.getInt("miktar"),
                        rs.getInt("kullanildi"))
                );
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

    public List<IzlemeNo> izlemeNoUret(String mamulid, String kullanildi) {
        Connection conn = connectionManager.getConnection();
        List<IzlemeNo> temp = new ArrayList<IzlemeNo>();

        String updateQuery = "insert into irfan.izlemeno (mamulid) values(?)";

        // connect to DB
        PreparedStatement pstmt = null;
        try {
            pstmt = conn.prepareStatement(updateQuery);
            pstmt.setInt(1, Integer.valueOf(mamulid));
            logger.info(pstmt.toString());
            pstmt.executeUpdate();

            temp = izlemeNoKontrol(mamulid, kullanildi);
        } catch (SQLException e) {
            logger.error("Error: " + e);
        } finally {
            try {
                if (pstmt != null) {
                    pstmt.close();
                }
            } catch (SQLException e) {
                logger.error("Error: " + e);
            }
        }
        return temp;
    }

    public List<IzlemeNo> izlemeNoKontrol(String mamulid, String kullanildi) {
        Connection conn = connectionManager.getConnection();
        List<IzlemeNo> temp = new ArrayList<IzlemeNo>();

        String updateQuery = "select i.*, m.ad mamulad, m.kod mamulkod "
                + "from irfan.izlemeno i "
                + "join irfan.mamul m on m.id=i.mamulid "
                + "where mamulid = ? and kullanildi <= ? order by gkrno desc limit 10";

        // connect to DB
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = conn.prepareStatement(updateQuery);
            pstmt.setInt(1, Integer.valueOf(mamulid));
            pstmt.setInt(2, Integer.valueOf(kullanildi));
            logger.info(pstmt.toString());
            rs = pstmt.executeQuery();

            while (rs.next()) {
                temp.add(new IzlemeNo(
                        rs.getInt("mamulid"),
                        rs.getInt("gkrno"),
                        rs.getDate("kullanildi_tarih"),
                        rs.getString("mamulkod"),
                        rs.getString("mamulad"),
                        Integer.valueOf(0), /* Ilk Uretim */
                        rs.getInt("kullanildi"))
                );
            }
        } catch (SQLException e) {
            logger.error("Error: " + e);
        } finally {
            try {
                if (pstmt != null) {
                    pstmt.close();
                }
            } catch (SQLException e) {
                logger.error("Error: " + e);
            }
        }
        return temp;
    }

    public List<IzlemeNo> izlemeNoSil(String mamulid, String gkrno, String kullanildi) {
        Connection conn = connectionManager.getConnection();
        List<IzlemeNo> temp = new ArrayList<IzlemeNo>();

        String updateQuery = "delete from irfan.izlemeno where mamulid = ? and gkrno = ? and kullanildi = 0";

        // connect to DB
        PreparedStatement pstmt = null;
        try {
            pstmt = conn.prepareStatement(updateQuery);
            pstmt.setInt(1, Integer.valueOf(mamulid));
            pstmt.setInt(2, Integer.valueOf(gkrno));
            pstmt.executeUpdate();
            logger.info(pstmt.toString());
            temp = izlemeNoKontrol(mamulid, kullanildi);
        } catch (SQLException e) {
            logger.error("Error: " + e);
        } finally {
            try {
                if (pstmt != null) {
                    pstmt.close();
                }
            } catch (SQLException e) {
                logger.error("Error: " + e);
            }
        }
        return temp;
    }
}
