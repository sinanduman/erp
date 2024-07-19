package erp.entity;

import erp.util.Fields;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MamulBilesen implements Serializable {
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
        this.id = rs.getInt(Fields.ID);
        this.mamulid = rs.getInt(Fields.MAMULID);
        this.bilesenid = rs.getInt(Fields.BILESENID);
        this.bilesenad = rs.getString(Fields.BILESENAD);
        this.bilesenkod = rs.getString(Fields.BILESENKOD);
        this.bilesentipid = rs.getInt(Fields.BILESENTIPID);
        this.bilesentipad = rs.getString(Fields.BILESENTIPAD);
        this.birimid = rs.getInt(Fields.BIRIMID);
        this.birimad = rs.getString(Fields.BIRIMAD);
        this.firmaid = rs.getInt(Fields.FIRMAID);
        this.firmaad = rs.getString(Fields.FIRMAAD);
        this.miktar = rs.getFloat(Fields.MIKTAR);
    }
}
