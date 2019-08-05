package erp.irfan.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MamulBilesen {
    private Integer id;
    private Integer mamulid;
    private Integer bilesenid;
    private String bilesenad;
    private String bilesenkod;
    private Integer bilesentipid;
    private String bilesentipad;
    private Integer birimid;
    private String birimad;
    private Integer firmaid;
    private String firmaad;
    private Float miktar;
}
