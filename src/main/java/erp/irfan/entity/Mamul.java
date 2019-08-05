package erp.irfan.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Mamul {
    private Integer id;
    private String ad;
    private String kod;
    private Integer firmaid;
    private String firmaad;
    private Integer cevrimsuresi;
    private Float agirlik;
    private Integer figur;
    private Date eklemetarihi;
    private String hammadde;
    private String hammaddegkrno;
    private String hammaddemiktar;
    private String hammaddemamulbasi;
    private String yarimamul;
    private String yarimamulgkrno;
    private String yarimamulmiktar;
    private String yarimamulmamulbasi;
}
