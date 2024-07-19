package erp.entity;

import erp.util.Util;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UretimPlan implements Serializable {
    private Integer id;
    private Integer mamulid;
    private String mamulad;
    private String mamulkod;
    private String hammaddead;
    private String yarimamulad;
    private Integer planlananmiktar;
    private Integer uretilenmiktar;
    private Integer hatalimiktar;
    private Integer makinaid;
    private String makinaad;
    private Integer firmaid;
    private String firmaad;
    private Integer calisanid;
    private String calisanad;
    private String calisansoyad;
    private Integer hataid;
    private String hataad;
    private String hatakodu;
    private Integer durusid;
    private String durusad;
    private String duruskodu;
    private Integer duruszaman;
    private Date tarih;
    private Time baszaman;
    private Time bitzaman;
    private Integer mamulizlno;
    private String hammaddeizlno;
    private String yarimamulizlno;
    private Integer tamamlandi;
    private String agirlik;
    private Integer cevrimsuresi;


    public String getHataad() {
        return (hataad == null) ? "" : hataad;
    }

    public String getDurusad() {
        return (durusad == null) ? "" : durusad;
    }

    public String getCalisanShortName() {
        return calisanad.substring(0, 1) + "." + calisansoyad;
    }

    public String getBaszaman() {
        return (this.baszaman != null) ? (new SimpleDateFormat("HH:mm").format(baszaman)) : "";
    }

    public String getBitzaman() {
        return (this.bitzaman != null) ? (new SimpleDateFormat("HH:mm").format(bitzaman)) : "";
    }

    public String getTarih() {
        return (this.tarih != null) ? (new SimpleDateFormat("yyyy-MM-dd").format(tarih)) : "";
    }

    public String getTarihTR() {
        return (this.tarih != null) ? (new SimpleDateFormat("dd-MM-yyyy").format(tarih)) : "";
    }

    public String getTarihTRShort() {
        return (this.tarih != null) ? (new SimpleDateFormat("dd.MM.yy").format(tarih)) : "";
    }

    public String getBasZaman() {
        return (this.baszaman != null) ? (new SimpleDateFormat("HH:mm").format(baszaman)) : "";
    }

    public String getBasSaat() {
        return (this.baszaman != null) ? (new SimpleDateFormat("HH").format(baszaman)) : "";
    }

    public String getBasDakika() {
        return (this.baszaman != null) ? (new SimpleDateFormat("mm").format(baszaman)) : "";
    }

    public String getBitZaman() {
        return (this.bitzaman != null) ? (new SimpleDateFormat("HH:mm").format(bitzaman)) : "";
    }

    public String getBitSaat() {
        return (this.bitzaman != null) ? (new SimpleDateFormat("HH").format(bitzaman)) : "";
    }

    public String getBitDakika() {
        return (this.bitzaman != null) ? (new SimpleDateFormat("mm").format(bitzaman)) : "";
    }

    public Integer getFark() {
        if (planlananmiktar == null || planlananmiktar == 0) {
            if (hatalimiktar == null || hatalimiktar == 0) {
                return 0;
            } else {
                return hatalimiktar;
            }

        }
        return planlananmiktar - uretilenmiktar;
    }

    public double getSapma() {
        if (hatalimiktar == null || hatalimiktar == 0) {
            return 0;
        }
        return Util.Round((uretilenmiktar - (uretilenmiktar - hatalimiktar)) * 100, uretilenmiktar);
    }

}
