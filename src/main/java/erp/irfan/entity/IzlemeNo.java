package erp.irfan.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class IzlemeNo {
    private Integer id;
    private Integer mamulid;
    private Integer gkrno;
    private Integer kullanildi;
    private Date    kullanildi_tarih;
    private String  mamulkod;
    private String  mamulad;
    private Integer miktar;

    public IzlemeNo(Integer mamulid, Integer gkrno, Date kullanildi_tarih,
                    String mamulkod, String mamulad, Integer miktar, Integer kullanildi) {
        this.mamulid = mamulid;
        this.gkrno = gkrno;
        this.kullanildi_tarih = kullanildi_tarih;
        this.mamulkod = mamulkod;
        this.mamulad = mamulad;
        this.miktar = miktar;
        this.kullanildi = kullanildi;
    }

}
