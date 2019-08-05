package erp.irfan.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Makina {
    private Integer id;
    private String ad;
    private Integer makinatipid;
    private String makinatipad;
}
