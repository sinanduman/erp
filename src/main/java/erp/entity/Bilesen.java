package erp.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Bilesen {
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
    private Integer durum;

    public Bilesen(ResultSet rs) throws SQLException {
        this.id = rs.getInt("id");
        this.ad = rs.getString("ad");
        this.kod = rs.getString("kod");
        this.bilesentipid = rs.getInt("bilesentipid");
        this.bilesentipad = rs.getString("bilesentipad");
        this.birimid = rs.getInt("birimid");
        this.birimad = rs.getString("birimad");
        this.firmaid = rs.getInt("firmaid");
        this.firmaad = rs.getString("firmaad");
        this.eklemetarihi = rs.getDate("eklemetarihi");
        this.durum = rs.getInt("durum");
    }
}