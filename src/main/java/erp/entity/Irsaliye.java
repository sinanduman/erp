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
public class Irsaliye {
    private Integer id;
    private String irsaliyeno;
    private Timestamp olusturmatarihi;
    private Timestamp gonderimtarihi;
    private Integer firmaid;
    private String firmaad;

    public Irsaliye(ResultSet rs) throws SQLException {
        this.id = rs.getInt(Fields.ID);
        this.irsaliyeno = rs.getString(Fields.IRSALIYENO);
        this.olusturmatarihi = rs.getTimestamp("olusturmatarihi");
        this.gonderimtarihi = rs.getTimestamp("gonderimtarihi");
        this.firmaid = rs.getInt(Fields.FIRMAID);
        this.firmaad = rs.getString(Fields.FIRMAAD);
    }

    public String getOlusturmatarihiTR() {
        return (this.olusturmatarihi != null) ? (new SimpleDateFormat("dd-MM-yyyy").format(olusturmatarihi)) : "";
    }

    public String getGonderimtarihiTR() {
        return (this.gonderimtarihi != null) ? (new SimpleDateFormat("dd-MM-yyyy").format(gonderimtarihi)) : "";
    }
}