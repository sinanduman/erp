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
public class DurusSebep {
    private Integer id;
    private String ad;
    private String kod;

    public DurusSebep(ResultSet rs) throws SQLException {
        this.id = rs.getInt(Fields.ID);
        this.ad = rs.getString(Fields.AD);
        this.kod = rs.getString(Fields.KOD);
    }
}
