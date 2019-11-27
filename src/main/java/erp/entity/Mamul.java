package erp.entity;

import erp.util.Util;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.ResultSet;
import java.sql.SQLException;
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
    private Integer esmamulid;
    private String esmamulad;
    private String esmamulkod;
    private Integer durum;
    private Integer kritikEsik;

    public Mamul(ResultSet rs) throws SQLException {
        this.id = rs.getInt("id");
        this.ad = rs.getString("ad");
        this.kod = rs.getString("kod");
        this.firmaid = rs.getInt("firmaid");
        this.firmaad = rs.getString("firmaad");
        this.cevrimsuresi = rs.getInt("cevrimsuresi");
        this.agirlik = rs.getFloat("agirlik");
        this.figur = rs.getInt("figur");
        this.eklemetarihi = rs.getDate("eklemetarihi");
        this.hammadde = Util.mamulInfoDecompose(rs.getString("hammadde"), 1); /* -- 1: hammadde adi icin */
        this.hammaddegkrno = Util.mamulInfoDecompose(rs.getString("hammadde"), 4); /* -- 1: hammadde gkrno icin */
        this.hammaddemiktar = Util.mamulInfoDecompose(rs.getString("hammadde"), 3); /* -- 1: hammadde agirlik icin */
        this.hammaddemamulbasi = Util.mamulInfoDecompose(rs.getString("hammadde"), 5); /* -- 1: hammadde mamul basi icin */
        this.yarimamul = Util.mamulInfoDecompose(rs.getString("yarimamul"), 1); /* -- 1: yarimamul adi icin */
        this.yarimamulgkrno = Util.mamulInfoDecompose(rs.getString("yarimamul"), 4); /* -- 1: yarimamul gkrno icin */
        this.yarimamulmiktar = Util.mamulInfoDecompose(rs.getString("yarimamul"), 3); /* -- 1: yarimamul agirlik icin */
        this.yarimamulmamulbasi = Util.mamulInfoDecompose(rs.getString("yarimamul"), 5); /* -- 1: yarimamul mamul basi icin */
        this.esmamulid = rs.getInt("esmamulid");
        this.esmamulad = Util.nullToEmpty(rs.getString("esmamulad"));
        this.esmamulkod = Util.nullToEmpty(rs.getString("esmamulkod"));
        this.durum = rs.getInt("durum");
        this.kritikEsik = rs.getInt("kritikesik");
    }
}