package erp.entity;

import erp.util.Fields;
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
        this.id = rs.getInt(Fields.ID);
        this.irsaliyeid = rs.getInt(Fields.IRSALIYEID);
        this.irsaliyeno = rs.getString(Fields.IRSALIYENO);
        this.firmaid = rs.getInt(Fields.FIRMAID);
        this.firmaad = rs.getString(Fields.FIRMAAD);
        this.mamulid = rs.getInt(Fields.MAMULID);
        this.mamulad = rs.getString(Fields.MAMULAD);
        this.mamulkod = rs.getString(Fields.MAMULKOD);
        this.gkrno = rs.getInt(Fields.GKRNO);
        this.stokid = rs.getInt(Fields.STOKID);
        this.miktar = rs.getInt(Fields.MIKTAR);
        this.olusturmatarihi = rs.getTimestamp("olusturmatarihi");
        this.gonderimtarihi = rs.getTimestamp("gonderimtarihi");
        this.not = rs.getString(Fields.NOT);
        this.irsaliyefirmaid = rs.getInt("irsaliyefirmaid");
    }
}