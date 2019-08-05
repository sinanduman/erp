package erp.dao;

import erp.irfan.ConnectionManager;
import erp.irfan.entity.Genel;
import erp.irfan.entity.Siparis;
import erp.irfan.entity.SiparisPlan;
import erp.irfan.entity.SiparisTip;
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
        Connection conn = connectionManager.getConnection();
        List<Siparis> temp = new ArrayList<Siparis>();

        String insertQuery = "insert into irfan.siparis(bilesenid, miktar, \"not\") values (?,?,?)";

        // connect to DB
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        Integer newSiparisId = 0;
        try {
            pstmt = conn.prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS);
            pstmt.setInt(1, Integer.valueOf(bilesenid));
            pstmt.setInt(2, Integer.valueOf(miktar));
            pstmt.setString(3, not);
            logger.info(pstmt.toString());
            pstmt.executeUpdate();

            rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                newSiparisId = rs.getInt(1);
            }
            temp = siparisListeGetir(SiparisTip.BEKLEYEN);
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

    public List<Siparis> siparisListeGetir(SiparisTip siparis) {
        Connection conn = connectionManager.getConnection();
        List<Siparis> temp = new ArrayList<Siparis>();

        String filter = null;
        if (siparis == SiparisTip.BEKLEYEN) {
            filter = " where s.bitistarih is null ";
        } else if (siparis == SiparisTip.TAMAMLANMIS) {
            filter = " where s.bitistarih is not null ";
        } else {
            filter = " ";
        }

        String searchQuery = "select s.*, b.ad bilesenad, "
                + "coalesce((select sum(miktar) from irfan.siparisplan sp where sp.siparisid=s.id),0) as planmiktar, "
                + "coalesce((select sum(miktar) from irfan.siparisplan sp where sp.siparisid=s.id and sp.tamamlandi=1),0) as tamammiktar "
                + "from irfan.siparis s join irfan.bilesen b on s.bilesenid=b.id "
                + filter
                + "order by s.id";

        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = conn.prepareStatement(searchQuery);
            logger.info(pstmt.toString());
            rs = pstmt.executeQuery();

            while (rs.next()) {
                temp.add(new Siparis(rs.getInt("id"), rs.getInt("bilesenid"), rs.getString("bilesenad"),
                        rs.getInt("miktar"), rs.getInt("planmiktar"), rs.getInt("tamammiktar"),
                        rs.getTimestamp("tarih"), rs.getTimestamp("bitistarih"), rs.getString("not")));
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

    public List<SiparisPlan> siparisPlanEkle(String siparisid, String miktar, String makinaid,
                                                       String calisanid, String tarih, String bassaat, String bitsaat, String not) {
        Connection conn = connectionManager.getConnection();
        List<SiparisPlan> temp = new ArrayList<SiparisPlan>();

        String insertQuery = "insert into irfan.siparisplan (siparisid, miktar, makinaid, calisanid, tarih, bassaat, bitsaat, \"not\") "
                + " values (?,?,?,?,?,?,?,?)";
        String searchQuery = "select sp.*, b.ad bilesenad, m.ad makinaad, c.ad calisanad, c.soyad calisansoyad "
                + "from irfan.siparisplan sp "
                + "join irfan.siparis s on s.id=sp.siparisid "
                + "join irfan.bilesen b on b.id=s.bilesenid "
                + "join irfan.makina m on m.id=sp.makinaid "
                + "join irfan.calisan c on c.id=sp.calisanid "
                + "where sp.tamamlandi=0";
        // connect to DB
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = conn.prepareStatement(insertQuery);
            pstmt.setInt(1, Integer.valueOf(siparisid));
            pstmt.setInt(2, Integer.valueOf(miktar));
            pstmt.setInt(3, Integer.valueOf(makinaid));
            pstmt.setInt(4, Integer.valueOf(calisanid));
            pstmt.setDate(5, Date.valueOf(tarih));
            pstmt.setTime(6, Time.valueOf(bassaat));
            pstmt.setTime(7, Time.valueOf(bitsaat));
            pstmt.setString(8, not);
            logger.info(pstmt.toString());

            pstmt.executeUpdate();

            pstmt = conn.prepareStatement(searchQuery);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                temp.add(new SiparisPlan(rs.getInt("id"), rs.getInt("siparisid"), rs.getString("bilesenad"),
                        rs.getInt("miktar"), rs.getInt("makinaid"), rs.getString("makinaad"), rs.getInt("calisanid"),
                        rs.getString("calisanad"), rs.getString("calisansoyad"), rs.getDate("tarih"),
                        rs.getTime("bassaat"), rs.getTime("bitsaat"), rs.getInt("hataid"), rs.getInt("hatamiktar"),
                        rs.getInt("durusid"), rs.getString("not")));

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

    public List<SiparisPlan> siparisPlanListeGetir(SiparisTip siparistip, int offset) {
        Connection conn = connectionManager.getConnection();
        List<SiparisPlan> temp = new ArrayList<SiparisPlan>();

        String pageFilter = (offset == 0) ? "" : " offset " + (offset - 1) * Genel.ROWPERPAGE + " limit " + Genel.ROWPERPAGE;

        String searchQuery = "select sp.*, mam.ad bilesenad, m.ad makinaad, c.ad calisanad, c.soyad calisansoyad "
                + "from irfan.siparisplan sp "
                + "join irfan.siparis s on s.id=sp.siparisid "
                + "join irfan.mamul mam on mam.id=s.bilesenid "
                + "join irfan.makina m on m.id=sp.makinaid "
                + "join irfan.calisan c on c.id=sp.calisanid "
                + "where sp.tamamlandi=? "
                + "order by s.id, sp.id " + pageFilter;
        // connect to DB
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = conn.prepareStatement(searchQuery);
            pstmt.setInt(1, siparistip.value());
            rs = pstmt.executeQuery();

            while (rs.next()) {
                temp.add(new SiparisPlan(rs.getInt("id"), rs.getInt("siparisid"), rs.getString("bilesenad"),
                        rs.getInt("miktar"), rs.getInt("makinaid"), rs.getString("makinaad"), rs.getInt("calisanid"),
                        rs.getString("calisanad"), rs.getString("calisansoyad"), rs.getDate("tarih"),
                        rs.getTime("bassaat"), rs.getTime("bitsaat"), rs.getInt("hataid"), rs.getInt("hatamiktar"),
                        rs.getInt("durusid"), rs.getString("not")));

            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return temp;
    }

    public Integer siparisPlanGuncelle(String siparisplanid, String miktar, String makinaid, String calisanid,
                                              String tarih, String bassaat, String basdakika, String bitsaat, String bitdakika, String hataid,
                                              String hatamiktar, String durusid, String not) {
        Connection conn = connectionManager.getConnection();
        int result = 0;
        String updateQuery = "update irfan.siparisplan "
                + "set miktar = ?, makinaid = ?, "
                + "calisanid = ?, tarih = ?, bassaat = ?, bitsaat = ?, hataid = ?, "
                + "hatamiktar = ?, durusid = ?, \"not\" = ? "
                + "where id = ?";
        // connect to DB
        PreparedStatement pstmt = null;
        try {
            pstmt = conn.prepareStatement(updateQuery);
            pstmt.setInt(1, Integer.valueOf(miktar));
            pstmt.setInt(2, Integer.valueOf(makinaid));
            pstmt.setInt(3, Integer.valueOf(calisanid));
            pstmt.setDate(4, Date.valueOf(tarih));
            pstmt.setTime(5, Time.valueOf(bassaat + ":" + basdakika + ":00"));
            pstmt.setTime(6, Time.valueOf(bitsaat + ":" + bitdakika + ":00"));
            if (hataid != null && hataid.trim() != "") pstmt.setInt(7, Integer.valueOf(hataid));
            else pstmt.setNull(7, Types.INTEGER);
            if (hatamiktar != null && (hatamiktar.trim() != "" && Integer.valueOf(hatamiktar.trim()) != 0))
                pstmt.setInt(8, Integer.valueOf(hatamiktar));
            else pstmt.setNull(8, Types.INTEGER);
            if (durusid != null && durusid.trim() != "") pstmt.setInt(9, Integer.valueOf(durusid));
            else pstmt.setNull(9, Types.INTEGER);
            if (not != null && not.trim() != "") pstmt.setString(10, not);
            else pstmt.setNull(10, Types.VARCHAR);
            pstmt.setInt(11, Integer.valueOf(siparisplanid));

            logger.info(pstmt.toString());
            result = pstmt.executeUpdate();
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
        return result;
    }

    public Integer siparisPlanTamamla(String siparisplanid) {
        Connection conn = connectionManager.getConnection();
        int result = 0;
        String updateQuery = "update irfan.siparisplan set tamamlandi = ? where id = ?";
        // connect to DB
        PreparedStatement pstmt = null;
        try {
            pstmt = conn.prepareStatement(updateQuery);
            pstmt.setInt(1, SiparisTip.TAMAMLANMIS.value());
            pstmt.setInt(2, Integer.valueOf(siparisplanid));
            logger.info(pstmt.toString());

            result = pstmt.executeUpdate();
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
        return result;
    }

    public Integer siparisPlanSil(String siparisplanid) {
        Connection conn = connectionManager.getConnection();
        int result = 0;
        String updateQuery = "delete from irfan.siparisplan where id = ?";
        // connect to DB
        PreparedStatement pstmt = null;
        try {
            pstmt = conn.prepareStatement(updateQuery);
            pstmt.setInt(1, Integer.valueOf(siparisplanid));
            logger.info(pstmt.toString());

            result = pstmt.executeUpdate();
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
        return result;
    }
}
