package erp.entity;

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
        this.id = rs.getInt("id");
        this.ad = rs.getString("ad");
        this.kod = rs.getString("kod");
    }
}
