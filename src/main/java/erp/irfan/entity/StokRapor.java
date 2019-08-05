package erp.irfan.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StokRapor {
    private Integer id;
    private Integer stokid;
    private Integer islemyonu;
    private String bilesenad;
    private String bilesenkod;
    private Integer bilesentipid;
    private String bilesentipad;
    private Integer firmaid;
    private String firmaad;
    private Integer miktar;
    private Integer kalan;
    private Timestamp tarih;

    public String getTarih() {
        String date = (this.tarih != null) ? (new SimpleDateFormat("yyyy-MM-dd HH:mm").format(tarih)) : "";
        return date;
    }

}