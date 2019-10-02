package erp.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class IrsaliyeBilesen {
    private Integer id;
    private Integer irsaliyeid;
    private String irsaliyeno;
    private Integer firmaid;
    private String firmaad;
    private Integer mamulid;
    private String mamulad;
    private String mamulkod;
    private Integer gkrno;
    private Integer stokid;
    private Integer miktar;
    private Timestamp olusturmatarihi;
    private Timestamp gonderimtarihi;
    private String not;
    private Integer irsaliyefirmaid;

    public IrsaliyeBilesen(ResultSet rs) throws SQLException {
        this.id = rs.getInt("id");
        this.irsaliyeid = rs.getInt("irsaliyeid");
        this.irsaliyeno = rs.getString("irsaliyeno");
        this.firmaid = rs.getInt("firmaid");
        this.firmaad = rs.getString("firmaad");
        this.mamulid = rs.getInt("mamulid");
        this.mamulad = rs.getString("mamulad");
        this.mamulkod = rs.getString("mamulkod");
        this.gkrno = rs.getInt("gkrno");
        this.stokid = rs.getInt("stokid");
        this.miktar = rs.getInt("miktar");
        this.olusturmatarihi = rs.getTimestamp("olusturmatarihi");
        this.gonderimtarihi = rs.getTimestamp("gonderimtarihi");
        this.not = rs.getString("not");
        this.irsaliyefirmaid = rs.getInt("irsaliyefirmaid");
    }
}