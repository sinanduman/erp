package erp.servlet;

import erp.dao.BakimDao;
import erp.dao.BaseDao;
import erp.dao.LoginDao;
import erp.dao.StokDao;
import erp.entity.Greeting;
import erp.entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Serializable;

@WebServlet("/login")
public class LoginServlet extends HttpServlet implements Serializable {
    @Inject
    private StokDao stokDao;
    @Inject
    private BakimDao bakimDao;
    @Inject
    private LoginDao loginDao;
    @Inject
    private Greeting greeting;
    private static final Logger logger = LoggerFactory.getLogger(LoginServlet.class);

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = (request.getParameter("username") != null) ? request.getParameter("username") : "";
        String password = (request.getParameter("password") != null) ? request.getParameter("password") : "";

        User user = loginDao.login(username, password);
        logRedirect(user, request, response);
    }

    private void logRedirect(User user, HttpServletRequest request, HttpServletResponse response) {

        String errorPage = "login.jsp";
        String successPage = "index.jsp";

        if (loginDao.loginResult(user)) {
            if ((User) request.getSession().getAttribute("user") == null) {

                int bakimBadge = bakimDao.bakimZamaniGelmis(BaseDao.V_BAKIM_MAMUL, " and kalan_baski < 2000 ");
                int hammaddeBadge = stokDao.stokEsikHammadde();
                int yarimamulBadge = stokDao.stokEsikYarimamul();
                int mamulBadge = stokDao.stokEsikMamul();

                request.getSession().setAttribute("loggedin", (user != null));
                request.getSession().setAttribute("user", user);
                request.getSession().setAttribute("userid", (user != null) ? user.getId() : "0");
                request.getSession().setAttribute("username", (user != null) ? user.getUsername() : "");
                request.getSession().setAttribute("admin", (user != null) ? user.getAdmin().toString() : "");
                request.getSession().setAttribute("role", (user != null) ? user.getRole() : "");
                request.getSession().setAttribute("message", "");
                request.getSession().setAttribute("bakimbadge", bakimBadge);
                request.getSession().setAttribute("hammaddebadge", hammaddeBadge);
                request.getSession().setAttribute("yarimamulbadge", yarimamulBadge);
                request.getSession().setAttribute("mamulbadge", mamulBadge);
                try {
                    request.getRequestDispatcher(successPage).forward(request, response);
                } catch (ServletException | IOException e) {
                    logger.error("Error: " + e);
                }
            }
        } else {
            try {
                request.getSession().setAttribute("message", "Kullanıcı adı veya Şifre yanlış!");
                request.getRequestDispatcher(errorPage).forward(request, response);
            } catch (ServletException | IOException e) {
                logger.error("Error: " + e);
            }
        }
    }
}
