package erp.irfan.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
}
