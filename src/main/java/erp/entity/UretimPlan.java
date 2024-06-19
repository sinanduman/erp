package erp.entity;

import erp.util.Util;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UretimPlan {
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
        String date = (this.baszaman != null) ? (new SimpleDateFormat("HH:mm").format(baszaman)) : "";
        return date;
    }

    public String getBitzaman() {
        String date = (this.bitzaman != null) ? (new SimpleDateFormat("HH:mm").format(bitzaman)) : "";
        return date;
    }

    public String getTarih() {
        String date = (this.tarih != null) ? (new SimpleDateFormat("yyyy-MM-dd").format(tarih)) : "";
        return date;
    }

    public String getTarihTR() {
        String date = (this.tarih != null) ? (new SimpleDateFormat("dd-MM-yyyy").format(tarih)) : "";
        return date;
    }

    public String getTarihTRShort() {
        String date = (this.tarih != null) ? (new SimpleDateFormat("dd.MM.yy").format(tarih)) : "";
        return date;
    }

    public String getBasZaman() {
        String date = (this.baszaman != null) ? (new SimpleDateFormat("HH:mm").format(baszaman)) : "";
        return date;
    }

    public String getBasSaat() {
        String date = (this.baszaman != null) ? (new SimpleDateFormat("HH").format(baszaman)) : "";
        return date;
    }

    public String getBasDakika() {
        String date = (this.baszaman != null) ? (new SimpleDateFormat("mm").format(baszaman)) : "";
        return date;
    }

    public String getBitZaman() {
        String date = (this.bitzaman != null) ? (new SimpleDateFormat("HH:mm").format(bitzaman)) : "";
        return date;
    }

    public String getBitSaat() {
        String date = (this.bitzaman != null) ? (new SimpleDateFormat("HH").format(bitzaman)) : "";
        return date;
    }

    public String getBitDakika() {
        String date = (this.bitzaman != null) ? (new SimpleDateFormat("mm").format(bitzaman)) : "";
        return date;
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
