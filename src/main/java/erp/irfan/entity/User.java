package erp.irfan.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.ResultSet;
import java.sql.SQLException;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
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
        this.name = rs.getString("name");
        this.surname = rs.getString("surname");
        this.username = rs.getString("username");
        this.username = rs.getString("password");
        this.admin = rs.getInt("admin");
        this.role = rs.getString("role");
    }
}