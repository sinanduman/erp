package erp.entity;

import erp.util.Fields;
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
        this.id = rs.getInt(Fields.ID);
        this.ad = rs.getString(Fields.AD);
    }
}
