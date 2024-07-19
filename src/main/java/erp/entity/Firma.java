package erp.entity;

import erp.util.Fields;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Firma implements Serializable {
    private Integer id;
    private String ad;
    private String telefon;
    private String adres;
    private String email;

    public Firma(ResultSet rs) throws SQLException {
        this.id = rs.getInt(Fields.ID);
        this.ad = rs.getString(Fields.AD);
        this.telefon = rs.getString(Fields.TELEFON);
        this.adres = rs.getString(Fields.ADRES);
        this.email = rs.getString(Fields.EMAIL);
    }
}
