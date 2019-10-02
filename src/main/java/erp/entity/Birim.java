package erp.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.ResultSet;
import java.sql.SQLException;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Birim {
    private Integer id;
    private String ad;

    public Birim(ResultSet rs) throws SQLException {
        this.id = rs.getInt("id");
        this.ad = rs.getString("ad");
    }
}
