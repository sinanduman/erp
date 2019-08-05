package erp.dao;

import erp.irfan.ConnectionManager;
import erp.irfan.entity.BilesenTip;
import erp.irfan.entity.Genel;
import erp.irfan.entity.Mamul;
import erp.irfan.entity.MamulBilesen;
import erp.util.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.Serializable;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Singleton
public class MamulDao implements Serializable, BaseDao {
    @Inject
    ConnectionManager connectionManager;
    private static final Logger logger = LoggerFactory.getLogger(MamulDao.class);

    public List<Mamul> mamulListeGetir(Integer mamulid, int offset) {
        Connection conn = connectionManager.getConnection();
        List<Mamul> temp = new ArrayList<Mamul>();

        String queryFilter = (mamulid == null) ? "" : " where id = " + mamulid;
        String pageFilter = (offset == 0) ? "" : " offset " + (offset - 1) * Genel.ROWPERPAGE + " limit " + Genel.ROWPERPAGE;
        String searchQuery = "select * from irfan.mamultum" + queryFilter + " ORDER BY kod ASC " + pageFilter; /* view */

        // connect to DB
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = conn.prepareStatement(searchQuery);
            logger.info(pstmt.toString());
            rs = pstmt.executeQuery();

            while (rs.next()) {
                temp.add(new Mamul(
                        rs.getInt("id"),
                        rs.getString("ad"),
                        rs.getString("kod"),
                        rs.getInt("firmaid"),
                        rs.getString("firmaad"),
                        rs.getInt("cevrimsuresi"),
                        rs.getFloat("agirlik"),
                        rs.getInt("figur"),
                        rs.getDate("eklemetarihi"),
                        Util.mamulInfoDecompose(rs.getString("hammadde"),1), /* -- 1: hammadde adi icin */
                        Util.mamulInfoDecompose(rs.getString("hammadde"),4), /* -- 1: hammadde gkrno icin */
                        Util.mamulInfoDecompose(rs.getString("hammadde"),3), /* -- 1: hammadde agirlik icin */
                        Util.mamulInfoDecompose(rs.getString("hammadde"),5), /* -- 1: hammadde mamul basi icin */
                        Util.mamulInfoDecompose(rs.getString("yarimamul"),1), /* -- 1: yarimamul adi icin */
                        Util.mamulInfoDecompose(rs.getString("yarimamul"),4), /* -- 1: yarimamul gkrno icin */
                        Util.mamulInfoDecompose(rs.getString("yarimamul"),3), /* -- 1: yarimamul agirlik icin */
                        Util.mamulInfoDecompose(rs.getString("yarimamul"),5) /* -- 1: yarimamul mamul basi icin */
                ));
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

    public String mamulEkle(String ad, String kod, String cevrimSuresi, String firmaid, String figur) {
        Connection conn = connectionManager.getConnection();
        BilesenTip bilesen = BilesenTip.MAMUL;
        String insertQuery = "insert into irfan.mamul (ad, kod, cevrimsuresi, bilesentipid, firmaid, figur) values (?,?,?,?,?,?) ";
        String newMamulId = "-1";
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = conn.prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, ad);
            pstmt.setString(2, kod);
            pstmt.setInt(3, Integer.valueOf(cevrimSuresi));
            pstmt.setInt(4, bilesen.value());
            pstmt.setInt(5, Integer.valueOf(firmaid));
            pstmt.setInt(6, Integer.valueOf(figur));
            logger.info(pstmt.toString());
            pstmt.executeUpdate();

            rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                newMamulId = String.valueOf(rs.getInt(1));
            }
        } catch (SQLException e) {
            logger.error("Error: " + e);
            newMamulId = e.getMessage();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException e) {
                logger.error("Error: " + e);
                newMamulId = e.getMessage();
            }
        }
        return newMamulId;
    }

    public void mamulBilesenEkle(int mamulid, int bilesen, int birim, float miktar) {
        Connection conn = connectionManager.getConnection();
        String insertQuery = "insert into irfan.mamulbilesen (mamulid, bilesenid, birimid, miktar) values (?,?,?,?)";
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = conn.prepareStatement(insertQuery);
            pstmt.setInt(1, mamulid);
            pstmt.setInt(2, bilesen);
            pstmt.setInt(3, birim);
            pstmt.setFloat(4, miktar);
            logger.info(pstmt.toString());
            pstmt.executeUpdate();
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
        return;
    }

    public List<MamulBilesen> mamulBilesenListeGetirTum(Integer mamulid) {
        Connection conn = connectionManager.getConnection();
        List<MamulBilesen> temp = new ArrayList<MamulBilesen>();

        String queryFilter = (mamulid == null) ? "" : " where mamulid = " + mamulid;
        String searchQuery = "select * from irfan.mamulbilesentum" + queryFilter;

        // connect to DB
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = conn.prepareStatement(searchQuery);
            logger.info(pstmt.toString());
            rs = pstmt.executeQuery();

            while (rs.next()) {
                temp.add(new MamulBilesen(rs.getInt("id"), rs.getInt("mamulid"), rs.getInt("bilesenid"),
                        rs.getString("bilesenad"), rs.getString("bilesenkod"), rs.getInt("bilesentipid"),
                        rs.getString("bilesentipad"), rs.getInt("birimid"), rs.getString("birimad"),
                        rs.getInt("firmaid"), rs.getString("firmaad"), rs.getFloat("miktar")));
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

    public String mamulSil(String mamulid) {
        Connection conn = connectionManager.getConnection();
        String result = "0";
        String updateQuery = "select irfan.mamul_sil(?)";
        // connect to DB
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = conn.prepareStatement(updateQuery);
            pstmt.setInt(1, Integer.valueOf(mamulid));
            logger.info(pstmt.toString());

            rs = pstmt.executeQuery();
            if (rs.next()) {
                result = rs.getString(1);
            }
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
            }
        }
        return result;
    }

    public String mamulGuncelle(String mamulid, String firmaid, String kod, String ad, String figur, String cevrimsuresi) {
        Connection conn = connectionManager.getConnection();
        String result = "0";
        String updateQuery = "update irfan.mamul set firmaid = ? , kod = ? , ad = ? , figur = ? , cevrimsuresi = ? where id = ?";

        // connect to DB
        PreparedStatement pstmt = null;
        try {
            pstmt = conn.prepareStatement(updateQuery);
            pstmt.setInt(1, Integer.valueOf(firmaid));
            pstmt.setString(2, kod);
            pstmt.setString(3, ad);
            pstmt.setInt(4, Integer.valueOf(figur));
            pstmt.setInt(5, Integer.valueOf(cevrimsuresi));
            pstmt.setInt(6, Integer.valueOf(mamulid));
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
            }
        }
        return result;
    }

    public String mamulBilesenGuncelle(String mamulid, String bilesenid, String miktar) {
        Connection conn = connectionManager.getConnection();
        String result = "0";
        String updateQuery = "update irfan.mamulbilesen set miktar = ? where mamulid = ? and bilesenid = ? ";

        // connect to DB
        PreparedStatement pstmt = null;
        try {
            pstmt = conn.prepareStatement(updateQuery);
            pstmt.setFloat(1, Float.valueOf(miktar));
            pstmt.setInt(2, Integer.valueOf(mamulid));
            pstmt.setInt(3, Integer.valueOf(bilesenid));
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
            }
        }
        return result;
    }
}
