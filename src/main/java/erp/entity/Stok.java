package erp.entity;

import erp.util.Fields;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Stok implements Serializable {
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
        this.id = rs.getInt(Fields.ID);
        this.bilesenid = rs.getInt(Fields.BILESENID);
        this.bilesenad = rs.getString(Fields.BILESENAD);
        this.bilesenkod = rs.getString(Fields.BILESENKOD);
        this.bilesentipid = rs.getInt(Fields.BILESENTIPID);
        this.bilesentipad = rs.getString(Fields.BILESENTIPAD);
        this.birimid = rs.getInt(Fields.BIRIMID);
        this.birimad = rs.getString(Fields.BIRIMAD);
        this.firmaid = rs.getInt(Fields.FIRMAID);
        this.firmaad = rs.getString(Fields.FIRMAAD);
        this.miktar = rs.getLong(Fields.MIKTAR);
        this.kalan = rs.getLong(Fields.KALAN);
        this.gkrno = rs.getString(Fields.GKRNO);
        this.irsaliyeno = rs.getString(Fields.IRSALIYENO);
        this.lot = rs.getString(Fields.LOT);
        this.giristarihi = rs.getTimestamp(Fields.GIRISTARIHI);
        this.cikistarihi = rs.getTimestamp(Fields.CIKISTARIHI);
        this.not = rs.getString(Fields.NOT);
        if (!rs.getString(Fields.MAMULDETAY).isEmpty()) {
            this.miktarstr = (rs.getString(Fields.MAMULDETAY)).split("\\|")[0]; /* 0: Miktar */
            this.gkrnostr = (rs.getString(Fields.MAMULDETAY)).split("\\|")[1]; /* 1: GkrNo */
            this.stokidstr = (rs.getString(Fields.MAMULDETAY)).split("\\|")[2]; /* 1: StokId */
        }
        this.islemyonu = rs.getInt(Fields.ISLEMYONU);
        this.mamulkod = rs.getString(Fields.MAMULKOD);
    }
}