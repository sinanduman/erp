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
public class Makina implements Serializable {
    private Integer id;
    private String ad;
    private Integer makinatipid;
    private String makinatipad;

    public Makina(ResultSet rs) throws SQLException {
        this.id = rs.getInt(Fields.ID);
        this.ad = rs.getString(Fields.AD);
        this.makinatipid = rs.getInt(Fields.MAKINATIPID);
        this.makinatipad = rs.getString(Fields.MAKINATIPAD);
    }
}