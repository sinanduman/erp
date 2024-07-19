package erp.entity;

import erp.util.Fields;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Bilesen implements Serializable {
    private Integer id;
    private String ad;
    private String kod;
    private Integer bilesentipid;
    private String bilesentipad;
    private Integer birimid;
    private String birimad;
    private Integer firmaid;
    private String firmaad;
    private Date eklemetarihi;
    private Integer fiyatId;
    private Double fiyat;
    private Integer kritikesik;
    private Integer durum;

    public Bilesen(ResultSet rs) throws SQLException {
        this.id = rs.getInt(Fields.ID);
        this.ad = rs.getString(Fields.AD);
        this.kod = rs.getString(Fields.KOD);
        this.bilesentipid = rs.getInt(Fields.BILESENTIPID);
        this.bilesentipad = rs.getString(Fields.BILESENTIPAD);
        this.birimid = rs.getInt(Fields.BIRIMID);
        this.birimad = rs.getString(Fields.BIRIMAD);
        this.firmaid = rs.getInt(Fields.FIRMAID);
        this.firmaad = rs.getString(Fields.FIRMAAD);
        this.fiyatId = rs.getInt(Fields.FIYATID);
        this.fiyat = rs.getDouble(Fields.FIYAT);
        this.kritikesik = rs.getInt(Fields.KRITIKESIK);
        this.eklemetarihi = rs.getDate("eklemetarihi");
        this.durum = rs.getInt(Fields.DURUM);
    }
}