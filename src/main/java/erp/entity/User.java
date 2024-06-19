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
public class User {
    private Integer id;
    private String name;
    private String surname;
    private String username;
    private String password;
    private Integer admin;
    private String role;

    public String getFullName() {
        return this.name + " " + this.surname;
    }

    public User(ResultSet rs) throws SQLException {
        this.id = rs.getInt(Fields.ID);
        this.name = rs.getString("name");
        this.surname = rs.getString("surname");
        this.username = rs.getString("username");
        this.password = rs.getString("password");
        this.admin = rs.getInt("admin");
        this.role = rs.getString("role");
    }
}