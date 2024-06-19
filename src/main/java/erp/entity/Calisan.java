package erp.entity;

import erp.util.Fields;
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
        this.id = rs.getInt(Fields.ID);
        this.ad = rs.getString(Fields.AD);
        this.soyad = rs.getString(Fields.SOYAD);
        this.gorev = rs.getString(Fields.GOREV);
        this.durum = rs.getInt(Fields.DURUM);
    }

    public String getFullName() {
        return ad + " " + soyad;
    }

    public String getShortName() {
        return ad.substring(0, 1) + "." + soyad;
    }
}
