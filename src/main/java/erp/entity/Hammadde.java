package erp.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Hammadde implements Serializable {
    private Integer id;
    private String kod;
    private String ad;
    private Integer birimid;
    private String birimad;
    private Integer firmaid;
    private String firmaad;
}
