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

import erp.dao.BaseDao;
import erp.dao.MakinaDao;
import erp.irfan.entity.Genel;
import erp.irfan.entity.Makina;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@WebServlet("/makina")
public class MakinaServlet extends HttpServlet implements Serializable {
    private static final Logger logger = LoggerFactory.getLogger(MakinaServlet.class);

    @Inject
    MakinaDao makinaDao;
    @Inject
    DAOFunctions daoFunctions;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost( request,  response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        response.setContentType("text/html; charset=UTF-8");

        Boolean ajaxIncluded= false;
        String message      = "";
        
        String islemid = request.getParameter("islemid");
        if(islemid != null && !islemid.isEmpty() ) {
            String result = "";
            switch (Integer.valueOf(islemid)) {
                case 1:
                    ajaxIncluded = true;
                    result = makinaDao.makinaGuncelle(
                                    new String(request.getParameter("id").getBytes("UTF-8")),
                                    new String(request.getParameter("ad").getBytes("UTF-8")),
                                    new String(request.getParameter("tip").getBytes("UTF-8"))
                                    );
                    break;
                case 3:
                    ajaxIncluded = true;
                    result = makinaDao.makinaSil(new String(request.getParameter("id").getBytes("UTF-8")));
                    break;
            }
            PrintWriter out = response.getWriter();
            out.print(result);
        }
        else {
            if(request.getParameter("makina_ad")!=null && request.getParameter("makina_tip")!=null) {
                message = makinaDao.makinaEkle(
                            new String(request.getParameter("makina_ad").getBytes("UTF-8")),
                            new String(request.getParameter("makina_tip").getBytes("UTF-8"))
                            );
            }
        }
        if(!ajaxIncluded) {
            List<Makina> makina = new ArrayList<Makina>();
            
            int totalrecord = daoFunctions.recordCount(BaseDao.MAKINA,"");
            int page = 1;
            if(request.getParameter("page") != null)
                page = Integer.parseInt(request.getParameter("page"));
            makina = makinaDao.makinaListeGetirTum(page);
            
            int noofpages = (int) Math.ceil(totalrecord * 1.0 / Genel.ROWPERPAGE);

            request.setAttribute("makina", makina);
            request.setAttribute("message", message);
            request.setAttribute("totalrecord", totalrecord);
            request.setAttribute("currentpage", page);
            request.setAttribute("noofpages", noofpages);
            request.getRequestDispatcher("makina.jsp").forward(request, response);
        }
    }
}
