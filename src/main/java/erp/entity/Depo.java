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
public class Depo {
    private Integer id;
    private String ad;

    public Depo(ResultSet rs) throws SQLException {
        this.id = rs.getInt(Fields.ID);
        this.ad = rs.getString(Fields.AD);
    }
}
