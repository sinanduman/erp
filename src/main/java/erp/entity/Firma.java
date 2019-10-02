package erp.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.ResultSet;
import java.sql.SQLException;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Firma {
    private Integer id;
    private String ad;
    private String telefon;
    private String adres;

    public Firma(ResultSet rs) throws SQLException {
        this.id = rs.getInt("id");
        this.ad = rs.getString("ad");
        this.telefon = rs.getString("telefon");
        this.adres = rs.getString("adres");
    }
}
