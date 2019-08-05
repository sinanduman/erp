package erp.irfan.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Hammadde {
    private Integer id;
    private String kod;
    private String ad;
    private Integer birimid;
    private String birimad;
    private Integer firmaid;
    private String firmaad;
}
