package erp.entity;

import erp.util.Fields;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StokRapor {
    private Integer stokid;
    private Integer bilesenid;
    private Integer islemyonu;
    private String bilesenad;
    private String bilesenkod;
    private Integer bilesentipid;
    private String bilesentipad;
    private Integer firmaid;
    private String firmaad;
    private Integer miktar;
    private Integer kalan;
    private Timestamp tarih;

    public StokRapor(ResultSet rs) throws SQLException {
        this.stokid = rs.getInt(Fields.ID);
        this.bilesenid = rs.getInt(Fields.BILESENID);
        this.islemyonu = rs.getInt(Fields.ISLEMYONU);
        this.bilesenad = rs.getString(Fields.AD);
        this.bilesenkod = rs.getString(Fields.KOD);
        this.bilesentipid = rs.getInt(Fields.BILESENTIPID);
        this.bilesentipad = rs.getString(Fields.BILESENTIPAD);
        this.firmaid = rs.getInt(Fields.FIRMAID);
        this.firmaad = rs.getString(Fields.FIRMAAD);
        this.miktar = rs.getInt(Fields.MIKTAR);
        this.kalan = rs.getInt(Fields.KALAN);
        this.tarih = rs.getTimestamp(Fields.TARIH);
    }

    public String getTarih() {
        return (this.tarih != null) ? (new SimpleDateFormat("yyyy-MM-dd HH:mm").format(tarih)) : "";
    }
}