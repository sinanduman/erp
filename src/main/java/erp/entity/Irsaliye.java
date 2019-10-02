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
public class Irsaliye {
    private Integer id;
    private String irsaliyeno;
    private Timestamp olusturmatarihi;
    private Timestamp gonderimtarihi;
    private Integer firmaid;
    private String firmaad;

    public Irsaliye(ResultSet rs) throws SQLException {
        this.id = rs.getInt("id");
        this.irsaliyeno = rs.getString("irsaliyeno");
        this.olusturmatarihi = rs.getTimestamp("olusturmatarihi");
        this.gonderimtarihi = rs.getTimestamp("gonderimtarihi");
        this.firmaid = rs.getInt("firmaid");
        this.firmaad = rs.getString("firmaad");
    }

    public String getOlusturmatarihiTR() {
        String date = (this.olusturmatarihi != null) ? (new SimpleDateFormat("dd-MM-yyyy").format(olusturmatarihi)) : "";
        return date;
    }

    public String getGonderimtarihiTR() {
        String date = (this.gonderimtarihi != null) ? (new SimpleDateFormat("dd-MM-yyyy").format(gonderimtarihi)) : "";
        return date;
    }
}