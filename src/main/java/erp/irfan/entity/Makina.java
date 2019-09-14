package erp.irfan.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.ResultSet;
import java.sql.SQLException;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Makina {
    private Integer id;
    private String ad;
    private Integer makinatipid;
    private String makinatipad;

    public Makina(ResultSet rs) throws SQLException {
        this.id = rs.getInt("id");
        this.ad = rs.getString("ad");
        this.makinatipid = rs.getInt("makinatipid");
        this.makinatipad = rs.getString("makinatipad");
    }
}