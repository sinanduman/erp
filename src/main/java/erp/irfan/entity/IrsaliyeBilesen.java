package erp.irfan.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class IrsaliyeBilesen {
    private Integer id;
    private Integer irsaliyeid;
    private String irsaliyeno;
    private Integer firmaid;
    private String firmaad;
    private Integer mamulid;
    private String mamulad;
    private String mamulkod;
    private Integer gkrno;
    private Integer stokid;
    private Integer miktar;
    private Timestamp olusturmatarihi;
    private Timestamp gonderimtarihi;
    private String not;
    private Integer irsaliyefirmaid;
}
