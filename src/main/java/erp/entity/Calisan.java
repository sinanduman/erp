package erp.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.ResultSet;
import java.sql.SQLException;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Calisan {
    private Integer id;
    private String ad;
    private String soyad;
    private String gorev;
    private Integer durum;

    public Calisan(ResultSet rs) throws SQLException {
        this.id = rs.getInt("id");
        this.ad = rs.getString("ad");
        this.soyad = rs.getString("soyad");
        this.gorev = rs.getString("gorev");
        this.durum = rs.getInt("durum");
    }

    public String getFullName() {
        return ad + " " + soyad;
    }

    public String getShortName() {
        return ad.substring(0, 1) + "." + soyad;
    }
}
