package erp.util;

import erp.irfan.ConnectionManager;
import erp.irfan.entity.Calisan;
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
        Connection conn = connectionManager.getConnection();
        List<Calisan> temp = new ArrayList<Calisan>();

        String insertQuery = "insert into irfan.calisan (adsoy, gorev) values (?,?) ";
        String searchQuery = "select * from irfan.calisan order by id ";

        // connect to DB
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = conn.prepareStatement(insertQuery);
            pstmt.setString(1, adsoy);
            pstmt.setString(2, gorev);
            pstmt.executeUpdate();

            pstmt = conn.prepareStatement(searchQuery);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                temp.add(new Calisan(
                                rs.getInt("id"), 
                                rs.getString("ad"), 
                                rs.getString("soyad"), 
                                rs.getString("gorev"),
                                rs.getInt("durum")
                                )
                );
            }
        } catch (SQLException e) {
            logger.error("Error: " + e);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException e) {
                logger.error("Error: " + e);
            }
        }
        return temp;
    }
}
