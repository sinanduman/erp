package erp.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.ResultSet;
import java.sql.SQLException;

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

    public MamulBilesen(ResultSet rs) throws SQLException {
        this.id = rs.getInt("id");
        this.mamulid = rs.getInt("mamulid");
        this.bilesenid = rs.getInt("bilesenid");
        this.bilesenad = rs.getString("bilesenad");
        this.bilesenkod = rs.getString("bilesenkod");
        this.bilesentipid = rs.getInt("bilesentipid");
        this.bilesentipad = rs.getString("bilesentipad");
        this.birimid = rs.getInt("birimid");
        this.birimad = rs.getString("birimad");
        this.firmaid = rs.getInt("firmaid");
        this.firmaad = rs.getString("firmaad");
        this.miktar = rs.getFloat("miktar");
    }
}
