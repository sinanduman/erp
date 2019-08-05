package erp.irfan.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Irsaliye {
    private Integer id;
    private String irsaliyeno;
    private Timestamp olusturmatarihi;
    private Timestamp gonderimtarihi;
    private Integer firmaid;
    private String firmaad;

    public String getOlusturmatarihiTR() {
        String date = (this.olusturmatarihi != null) ? (new SimpleDateFormat("dd-MM-yyyy").format(olusturmatarihi)) : "";
        return date;
    }

    public String getGonderimtarihiTR() {
        String date = (this.gonderimtarihi != null) ? (new SimpleDateFormat("dd-MM-yyyy").format(gonderimtarihi)) : "";
        return date;
    }
}
