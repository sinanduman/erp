package erp.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Bakim {
    private Integer id;
    private Integer mamulid;
    private Integer calisanid;
    private Date bakimtarihi;
    private Date islemtarihi;

    public Bakim(ResultSet rs) throws SQLException {
        this.id = rs.getInt("id");
        this.mamulid = rs.getInt("mamulid");
        this.calisanid = rs.getInt("calisanid");
        this.bakimtarihi = rs.getDate ("bakimtarihi");
        this.islemtarihi = rs.getDate("islemtarihi");
    }
}
