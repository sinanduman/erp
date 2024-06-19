package erp.entity;

import erp.util.Fields;
import erp.util.Util;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;

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
    private Integer baskiSayisi;
    private Date bakimTarihi;
    private Integer calisanId;

    public Mamul(ResultSet rs) throws SQLException {
        this.id = rs.getInt(Fields.ID);
        this.ad = rs.getString(Fields.AD);
        this.kod = rs.getString(Fields.KOD);
        this.firmaid = rs.getInt(Fields.FIRMAID);
        this.firmaad = rs.getString(Fields.FIRMAAD);
        this.cevrimsuresi = rs.getInt(Fields.CEVRIMSURESI);
        this.agirlik = rs.getFloat(Fields.AGIRLIK);
        this.figur = rs.getInt(Fields.FIGUR);
        this.eklemetarihi = rs.getDate(Fields.EKLEMETARIHI);
        this.hammadde = Util.mamulInfoDecompose(rs.getString(Fields.HAMMADDE), 1); /* -- 1: hammadde adi icin */
        this.hammaddegkrno = Util.mamulInfoDecompose(rs.getString(Fields.HAMMADDE), 4); /* -- 1: hammadde gkrno icin */
        this.hammaddemiktar = Util.mamulInfoDecompose(rs.getString(Fields.HAMMADDE), 3); /* -- 1: hammadde agirlik icin */
        this.hammaddemamulbasi = Util.mamulInfoDecompose(rs.getString(Fields.HAMMADDE), 5); /* -- 1: hammadde mamul basi icin */
        this.yarimamul = Util.mamulInfoDecompose(rs.getString(Fields.YARIMAMUL), 1); /* -- 1: yarimamul adi icin */
        this.yarimamulgkrno = Util.mamulInfoDecompose(rs.getString(Fields.YARIMAMUL), 4); /* -- 1: yarimamul gkrno icin */
        this.yarimamulmiktar = Util.mamulInfoDecompose(rs.getString(Fields.YARIMAMUL), 3); /* -- 1: yarimamul agirlik icin */
        this.yarimamulmamulbasi = Util.mamulInfoDecompose(rs.getString(Fields.YARIMAMUL), 5); /* -- 1: yarimamul mamul basi icin */
        this.esmamulid = rs.getInt(Fields.ESMAMULID);
        this.esmamulad = (String) Util.nullToEmpty(rs.getString(Fields.ESMAMULAD));
        this.esmamulkod = (String) Util.nullToEmpty(rs.getString(Fields.ESMAMULKOD));
        this.durum = rs.getInt(Fields.DURUM);
        this.kritikEsik = rs.getInt(Fields.KRITIKESIK);
        this.baskiSayisi = rs.getInt("baskisayisi");
        this.bakimTarihi = rs.getDate("bakimtarihi");
        this.calisanId = rs.getInt(Fields.CALISANID);
    }
}