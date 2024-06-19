package erp.dao;

import erp.entity.User;
import erp.servlet.ConnectionManager;
import erp.util.Constants;
import erp.util.Fields;
import erp.util.Util;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

@Singleton
public class LoginDao implements Serializable, BaseDao {
    @Inject
    private ConnectionManager connectionManager;
    private static final Logger logger = LoggerFactory.getLogger(LoginDao.class);

    public User login(String username, String password) {
        ResultSet rs;
        User user = null;
        String searchQuery = Fields.SELECT + "*"
                + Fields.FROM + BaseDao.LOGIN
                + Fields.WHERE + "username=? and password=?";

        try (Connection currentCon = connectionManager.getConnection();
             PreparedStatement pstmt = currentCon.prepareStatement(searchQuery)) {
            long start = System.currentTimeMillis();
            pstmt.setString(1, username);
            pstmt.setString(2, Util.md5Dao(password));
            rs = pstmt.executeQuery();
            logger.info(Constants.LOG_FORMAT, pstmt, (System.currentTimeMillis() - start));
            boolean more = rs.next();
            // if user does not exist set the isValid variable to false
            if (!more) {
                System.out.println("Sorry, you are not a registered user! Please sign up first");
            }
            // if user exists set the isValid variable to true
            else {
                user = new User(rs);
            }
        } catch (Exception e) {
            logger.error(ExceptionUtils.getStackTrace(e));
        }
        return user;
    }

    public boolean loginResult(User user) {
        return (user != null);
    }
}
