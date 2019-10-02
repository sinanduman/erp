package erp.entity;

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
        this.id = rs.getInt("id");
        this.bilesenid = rs.getInt("bilesenid");
        this.bilesenad = rs.getString("bilesenad");
        this.miktar = rs.getInt("miktar");
        this.planmiktar = rs.getInt("planmiktar");
        this.tamammiktar = rs.getInt("tamammiktar");
        this.tarih = rs.getTimestamp("tarih");
        this.bitistarih = rs.getTimestamp("bitistarih");
        this.not = rs.getString("not");
    }

    public String getTarih() {
        String date = (this.tarih != null) ? (new SimpleDateFormat("yyyy-MM-dd HH:mm").format(tarih)) : "";
        return date;
    }

    public String getBitistarih() {
        String date = (this.bitistarih != null) ? (new SimpleDateFormat("yyyy-MM-dd HH:mm").format(bitistarih)) : "";
        return date;
    }

    public String getNot() {
        return (this.not != null) ? (this.not) : "";
    }
}
