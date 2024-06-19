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
public class Siparis {
    private Integer id;
    private Integer bilesenid;
    private String bilesenad;
    private Integer miktar;
    private Integer planmiktar;
    private Integer tamammiktar;
    private Timestamp tarih;
    private Timestamp bitistarih;
    private String not;

    public Siparis(ResultSet rs) throws SQLException {
        this.id = rs.getInt(Fields.ID);
        this.bilesenid = rs.getInt(Fields.BILESENID);
        this.bilesenad = rs.getString(Fields.BILESENAD);
        this.miktar = rs.getInt(Fields.MIKTAR);
        this.planmiktar = rs.getInt(Fields.PLANMIKTAR);
        this.tamammiktar = rs.getInt(Fields.TAMAMMIKTAR);
        this.tarih = rs.getTimestamp(Fields.TARIH);
        this.bitistarih = rs.getTimestamp(Fields.BITISTARIH);
        this.not = rs.getString(Fields.NOT);
    }

    public String getTarih() {
        return (this.tarih != null) ? (new SimpleDateFormat("yyyy-MM-dd HH:mm").format(tarih)) : "";
    }

    public String getBitistarih() {
        return (this.bitistarih != null) ? (new SimpleDateFormat("yyyy-MM-dd HH:mm").format(bitistarih)) : "";
    }

    public String getNot() {
        return (this.not != null) ? (this.not) : "";
    }
}
