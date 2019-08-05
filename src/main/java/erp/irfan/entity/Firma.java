package erp.irfan.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Firma {
    private Integer id;
    private String ad;
    private String telefon;
    private String adres;
}
