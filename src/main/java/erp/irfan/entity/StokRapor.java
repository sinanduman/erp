package erp.irfan.entity;

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
    private Integer id;
    private Integer stokid;
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
        this.id = rs.getInt("id");
        this.stokid = rs.getInt("stokid");
        this.islemyonu = rs.getInt("islemyonu");
        this.bilesenad = rs.getString("ad");
        this.bilesenkod = rs.getString("kod");
        this.bilesentipid = rs.getInt("bilesentipid");
        this.bilesentipad = rs.getString("bilesentipad");
        this.firmaid = rs.getInt("firmaid");
        this.firmaad = rs.getString("firmaad");
        this.miktar = rs.getInt("miktar");
        this.kalan = rs.getInt("kalan");
        this.tarih = rs.getTimestamp("tarih");
    }

    public String getTarih() {
        String date = (this.tarih != null) ? (new SimpleDateFormat("yyyy-MM-dd HH:mm").format(tarih)) : "";
        return date;
    }
}