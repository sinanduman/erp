package erp.entity;

import erp.util.Fields;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class IzlemeNo implements Serializable {
    private Integer id;
    private Integer mamulid;
    private Long gkrno;
    private Integer kullanildi;
    private Date kullanildi_tarih;
    private String mamulkod;
    private String mamulad;
    private Integer miktar;

    public IzlemeNo(ResultSet rs) throws SQLException {
        this.id = rs.getInt(Fields.ID);
        this.mamulid = rs.getInt(Fields.MAMULID);
        this.gkrno = rs.getLong(Fields.GKRNO);
        this.kullanildi = rs.getInt(Fields.KULLANILDI);
        this.kullanildi_tarih = rs.getDate(Fields.KULLANILDI_TARIH);
        this.mamulkod = rs.getString(Fields.KOD);
        this.mamulad = rs.getString(Fields.AD);
        this.miktar = rs.getInt(Fields.MIKTAR);
    }

    public IzlemeNo(Integer mamulid, Long gkrno, Date kullanildi_tarih,
                    String mamulkod, String mamulad, Integer miktar, Integer kullanildi) {
        this.gkrno = gkrno;
        this.mamulid = mamulid;
        this.kullanildi = kullanildi;
        this.kullanildi_tarih = kullanildi_tarih;
        this.mamulkod = mamulkod;
        this.mamulad = mamulad;
        this.miktar = miktar;
    }
}
