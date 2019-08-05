package erp.irfan;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import erp.dao.CalisanDao;
import erp.irfan.entity.Calisan;
import erp.irfan.entity.Genel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@WebServlet("/calisan")
public class CalisanServlet extends HttpServlet implements Serializable {
    private static final Logger logger = LoggerFactory.getLogger(CalisanServlet.class);

    @Inject
    CalisanDao calisanDao;
    @Inject
    DAOFunctions daoFunctions;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        response.setContentType("text/html; charset=UTF-8");

        Boolean ajaxIncluded= false;
        String message      = "";
        
        String islemid = request.getParameter("islemid");
        if(islemid != null && islemid != "" ) {
            String result = "";
            switch (Integer.valueOf(islemid)) {
                case 1:
                    ajaxIncluded = true;
                    result = calisanDao.calisanGuncelle(
                                    new String(request.getParameter("id").getBytes("UTF-8")),
                                    new String(request.getParameter("ad").getBytes("UTF-8")),
                                    new String(request.getParameter("soyad").getBytes("UTF-8")),
                                    new String(request.getParameter("gorev").getBytes("UTF-8")),
                                    new String(request.getParameter("durum").getBytes("UTF-8"))
                                    );
                    break;
                case 3:
                    ajaxIncluded = true;
                    result = calisanDao.calisanSil(new String(request.getParameter("id").getBytes("UTF-8")));
                    break;
            }
            PrintWriter out = response.getWriter();
            out.print(result);
        }
        else {
            if(request.getParameter("ad")!=null && request.getParameter("soyad")!=null && request.getParameter("gorev")!=null) {
                message = calisanDao.calisanEkle(
                                new String(request.getParameter("ad").getBytes("UTF-8")).trim(), 
                                new String(request.getParameter("soyad").getBytes("UTF-8")).trim(), 
                                new String(request.getParameter("gorev").getBytes("UTF-8")).trim()
                                );    
            }
            
        }
        if(!ajaxIncluded) {
            
            int totalrecord = daoFunctions.recordCount("calisan","");
            int page = 1;
            if(request.getParameter("page") != null)
                page = Integer.parseInt(request.getParameter("page"));
            
            int noofpages = (int) Math.ceil(totalrecord * 1.0 / Genel.ROWPERPAGE);
            
            List<Calisan> calisan = calisanDao.calisanListeGetirTum(page, null);
            
            request.setAttribute("calisan", calisan);
            request.setAttribute("message", message);
            request.setAttribute("totalrecord", totalrecord);
            request.setAttribute("noofpages", noofpages);
            request.setAttribute("currentpage", page);
            request.getRequestDispatcher("calisan.jsp").forward(request, response);
        }
    }
}