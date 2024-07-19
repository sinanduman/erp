package erp.servlet;

import erp.dao.BaseDao;
import erp.dao.MakinaDao;
import erp.entity.Genel;
import erp.entity.Makina;
import erp.util.Fields;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/makina")
public class MakinaServlet extends HttpServlet implements Serializable {
    private static final Logger logger = LoggerFactory.getLogger(MakinaServlet.class);

    @Inject
    private MakinaDao makinaDao;
    @Inject
    private DAOFunctions daoFunctions;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        response.setContentType("text/html; charset=UTF-8");

        boolean ajaxIncluded = false;
        String message = "";

        String islemid = request.getParameter(Fields.ISLEMID);
        if (islemid != null && !islemid.isEmpty()) {
            String result = "";
            switch (Integer.parseInt(islemid)) {
                case 1:
                    ajaxIncluded = true;
                    result = makinaDao.makinaGuncelle(
                            new String(request.getParameter(Fields.ID).getBytes(StandardCharsets.UTF_8)),
                            new String(request.getParameter(Fields.AD).getBytes(StandardCharsets.UTF_8)),
                            new String(request.getParameter(Fields.TIP).getBytes(StandardCharsets.UTF_8))
                    );
                    break;
                case 3:
                    ajaxIncluded = true;
                    result = makinaDao.makinaSil(new String(request.getParameter(Fields.ID).getBytes(StandardCharsets.UTF_8)));
                    break;
            }
            PrintWriter out = response.getWriter();
            out.print(result);
        } else {
            if (request.getParameter("makina_ad") != null && request.getParameter("makina_tip") != null) {
                message = makinaDao.makinaEkle(
                        new String(request.getParameter("makina_ad").getBytes(StandardCharsets.UTF_8)),
                        new String(request.getParameter("makina_tip").getBytes(StandardCharsets.UTF_8))
                );
            }
        }
        if (!ajaxIncluded) {
            int totalrecord = daoFunctions.recordAgg(BaseDao.MAKINA, Fields.COUNT, "*", "");
            int page = 1;
            if (request.getParameter(Fields.PAGE) != null)
                page = Integer.parseInt(request.getParameter(Fields.PAGE));
            List<Makina> makina = makinaDao.makinaListeGetirTum(page);

            int noofpages = (int) Math.ceil(totalrecord * 1.0 / Genel.ROWPERPAGE);

            request.setAttribute(Fields.MAKINA, makina);
            request.setAttribute(Fields.MESSAGE, message);
            request.setAttribute(Fields.TOTALRECORD, totalrecord);
            request.setAttribute(Fields.CURRENTPAGE, page);
            request.setAttribute(Fields.NOOFPAGES, noofpages);
            request.getRequestDispatcher("makina.jsp").forward(request, response);
        }
    }
}
