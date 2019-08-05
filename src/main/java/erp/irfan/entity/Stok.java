package erp.irfan.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Stok {
    private Integer id;
    private Integer bilesenid;
    private String bilesenad;
    private String bilesenkod;
    private Integer bilesentipid;
    private String bilesentipad;
    private Integer birimid;
    private String birimad;
    private Integer firmaid;
    private String firmaad;
    private Integer miktar;
    private Integer kalan;
    private String gkrno;
    private String irsaliyeno;
    private String lot;
    private Timestamp giristarihi;
    private Timestamp cikistarihi;
    private String not;
    private String miktarstr;
    private String gkrnostr;
    private String stokidstr;
    private Integer islemyonu;
    private String mamulkod;

}
