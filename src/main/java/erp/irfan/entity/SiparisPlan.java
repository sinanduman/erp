package erp.irfan.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SiparisPlan {
    private Integer id;
    private Integer siparisid;
    private String bilesenad;
    private Integer miktar;
    private Integer makinaid;
    private String makinaad;
    private Integer calisanid;
    private String calisanad;
    private String calisansoyad;
    private Date tarih;
    private Time baszaman;
    private Time bitzaman;
    private Integer hataid;
    private Integer hatamiktar;
    private Integer durusid;
    private String not;

    public String getCalisanFullName() {
        return calisanad + " " + calisansoyad;
    }

    public String getCalisanShortName() {
        return calisanad.substring(0, 1) + "." + calisansoyad;
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

    public String getNot() {
        return (not != null) ? not : "";
    }

}
