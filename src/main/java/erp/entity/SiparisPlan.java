package erp.entity;

import erp.util.Fields;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.ResultSet;
import java.sql.SQLException;
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

    public SiparisPlan(ResultSet rs) throws SQLException {
        this.id = rs.getInt(Fields.ID);
        this.siparisid = rs.getInt(Fields.SIPARISID);
        this.bilesenad = rs.getString(Fields.BILESENAD);
        this.miktar = rs.getInt(Fields.MIKTAR);
        this.makinaid = rs.getInt(Fields.MAKINAID);
        this.makinaad = rs.getString(Fields.MAKINAAD);
        this.calisanid = rs.getInt(Fields.CALISANID);
        this.calisanad = rs.getString("calisanad");
        this.calisansoyad = rs.getString("calisansoyad");
        this.tarih = rs.getDate(Fields.TARIH);
        this.baszaman = rs.getTime(Fields.BASSAAT);
        this.bitzaman = rs.getTime(Fields.BITSAAT);
        this.hataid = rs.getInt(Fields.HATAID);
        this.hatamiktar = rs.getInt("hatamiktar");
        this.durusid = rs.getInt(Fields.DURUSID);
        this.not = rs.getString(Fields.NOT);
    }

    public String getCalisanFullName() {
        return calisanad + " " + calisansoyad;
    }

    public String getCalisanShortName() {
        return calisanad.substring(0, 1) + "." + calisansoyad;
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

    public String getNot() {
        return (not != null) ? not : "";
    }

}
