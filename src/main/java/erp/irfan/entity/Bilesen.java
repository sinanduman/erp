package erp.irfan.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Bilesen {
    private Integer id;
    private String ad;
    private String kod;
    private Integer bilesentipid;
    private String bilesentipad;
    private Integer birimid;
    private String birimad;
    private Integer firmaid;
    private String firmaad;
    private Date eklemetarihi;
}
