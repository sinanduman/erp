package erp.util;

import erp.dao.BaseDao;
import erp.servlet.ConnectionManager;
import erp.entity.Calisan;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class AjaxUtils extends HttpServlet implements Serializable {
    @Inject
    ConnectionManager connectionManager;
    private static final Logger logger = LoggerFactory.getLogger(AjaxUtils.class);

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html; charset=UTF-8");
        PrintWriter out = response.getWriter();

        out.println(request.getParameter("username"));
        out.println(request.getParameter("password"));

        List<Calisan> calisan = new ArrayList<Calisan>();
        calisan = calisanEkle((String) request.getParameter("username"), (String) request.getParameter("password"));

        request.setAttribute("calisan", calisan);
        request.getRequestDispatcher("calisan.jsp").forward(request, response);
    }

    protected List<Calisan> calisanEkle(String adsoy, String gorev) {
        List<Calisan> temp = new ArrayList<Calisan>();
        String insertQuery = "insert into " + BaseDao.CALISAN + " (adsoy, gorev) values (?,?) ";
        String searchQuery = "select * from " + BaseDao.CALISAN + " order by id ";
        // connect to DB
        ResultSet rs;
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(insertQuery)) {
            pstmt.setString(1, adsoy);
            pstmt.setString(2, gorev);
            pstmt.executeUpdate();

            PreparedStatement pstmt2 = conn.prepareStatement(searchQuery);
            rs = pstmt2.executeQuery();

            while (rs.next()) {
                temp.add(new Calisan(rs)
                );
            }
        } catch (Exception e) {
            logger.error(ExceptionUtils.getStackTrace(e));
        }
        return temp;
    }
}
