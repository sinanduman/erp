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
public class IzlemeNo {
    private Integer id;
    private Integer mamulid;
    private Integer gkrno;
    private Integer kullanildi;
    private Date kullanildi_tarih;
    private String mamulkod;
    private String mamulad;
    private Integer miktar;

    public IzlemeNo(ResultSet rs) throws SQLException {
        this.id = rs.getInt("id");
        this.mamulid = rs.getInt("mamulid");
        this.gkrno = rs.getInt("gkrno");
        this.kullanildi = rs.getInt("kullanildi");
        this.kullanildi_tarih = rs.getDate("kullanildi_tarih");
        this.mamulkod = rs.getString("kod");
        this.mamulad = rs.getString("ad");
        this.miktar = rs.getInt("miktar");
    }

    public IzlemeNo(Integer mamulid, Integer gkrno, Date kullanildi_tarih,
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
