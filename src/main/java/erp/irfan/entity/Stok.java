package erp.irfan.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Stok {
    private Integer id;
    private Integer bilesenid;
    private String bilesenad;
    private String bilesenkod;
    private Integer bilesentipid;
    private String bilesentipad;
    private Integer birimid;
    private String birimad;
    private Integer firmaid;
    private String firmaad;
    private Long miktar;
    private Long kalan;
    private String gkrno;
    private String irsaliyeno;
    private String lot;
    private Timestamp giristarihi;
    private Timestamp cikistarihi;
    private String not;
    private String miktarstr;
    private String gkrnostr;
    private String stokidstr;
    private Integer islemyonu;
    private String mamulkod;

    public Stok(ResultSet rs) throws SQLException {
        this.id = rs.getInt("id");
        this.bilesenid = rs.getInt("bilesenid");
        this.bilesenad = rs.getString("bilesenad");
        this.bilesenkod = rs.getString("bilesenkod");
        this.bilesentipid = rs.getInt("bilesentipid");
        this.bilesentipad = rs.getString("bilesentipad");
        this.birimid = rs.getInt("birimid");
        this.birimad = rs.getString("birimad");
        this.firmaid = rs.getInt("firmaid");
        this.firmaad = rs.getString("firmaad");
        this.miktar = rs.getLong("miktar");
        this.kalan = rs.getLong("kalan");
        this.gkrno = rs.getString("gkrno");
        this.irsaliyeno = rs.getString("irsaliyeno");
        this.lot = rs.getString("lot");
        this.giristarihi = rs.getTimestamp("giristarihi");
        this.cikistarihi = rs.getTimestamp("cikistarihi");
        this.not = rs.getString("not");
        if (!rs.getString("mamuldetay").isEmpty()) {
            this.miktarstr = (rs.getString("mamuldetay")).split("\\|")[0]; /* 0: Miktar */
            this.gkrnostr = (rs.getString("mamuldetay")).split("\\|")[1]; /* 1: GkrNo */
            this.stokidstr = (rs.getString("mamuldetay")).split("\\|")[2]; /* 1: StokId */
        }
        this.islemyonu = rs.getInt("islemyonu");
        this.mamulkod = rs.getString("mamulkod");
    }
}