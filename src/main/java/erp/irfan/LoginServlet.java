package erp.irfan;

import erp.dao.LoginDao;
import erp.irfan.entity.Greeting;
import erp.irfan.entity.User;
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
                request.getSession().setAttribute("loggedin", (user != null));
                request.getSession().setAttribute("user", user);
                request.getSession().setAttribute("admin", user.getAdmin().toString());
                request.getSession().setAttribute("role", user.getRole());
                request.getSession().setAttribute("message", "");
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
