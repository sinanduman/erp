package erp.dao;

import erp.servlet.ConnectionManager;
import erp.entity.User;
import erp.util.Util;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

@Singleton
public class LoginDao implements Serializable, BaseDao {
    @Inject
    ConnectionManager connectionManager;

    public User login(String username, String password) {
        ResultSet rs = null;
        User user = null;
        String searchQuery = "select * from " + BaseDao.LOGIN + " where username=? and password=?";

        try (Connection currentCon = connectionManager.getConnection();
             PreparedStatement pstmt = currentCon.prepareStatement(searchQuery)) {
            pstmt.setString(1, username);
            pstmt.setString(2, Util.md5Dao(password));
            rs = pstmt.executeQuery();
            boolean more = rs.next();
            // if user does not exist set the isValid variable to false
            if (!more) {
                System.out.println("Sorry, you are not a registered user! Please sign up first");
            }
            // if user exists set the isValid variable to true
            else {
                user = new User(rs);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return user;
    }

    public boolean loginResult(User user) {
        return (user != null);
    }
}
