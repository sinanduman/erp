package erp.servlet;

import erp.dao.BaseDao;
import erp.dao.CalisanDao;
import erp.entity.Calisan;
import erp.entity.Genel;
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
import java.util.List;

@WebServlet("/calisan")
public class CalisanServlet extends HttpServlet implements Serializable {
    private static final Logger logger = LoggerFactory.getLogger(CalisanServlet.class);

    @Inject
    private CalisanDao calisanDao;
    @Inject
    private DAOFunctions daoFunctions;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        response.setContentType("text/html; charset=UTF-8");

        boolean ajaxIncluded = false;
        String message = "";

        String islemid = request.getParameter(Fields.ISLEMID);
        if (islemid != null && !islemid.isEmpty()) {
            String result;
            switch (Integer.parseInt(islemid)) {
                case 1:
                    ajaxIncluded = true;
                    result = calisanDao.calisanGuncelle(
                            new String(request.getParameter(Fields.ID).getBytes(StandardCharsets.UTF_8)),
                            new String(request.getParameter(Fields.AD).getBytes(StandardCharsets.UTF_8)),
                            new String(request.getParameter(Fields.SOYAD).getBytes(StandardCharsets.UTF_8)),
                            new String(request.getParameter(Fields.GOREV).getBytes(StandardCharsets.UTF_8)),
                            new String(request.getParameter(Fields.DURUM).getBytes(StandardCharsets.UTF_8))
                    );
                    break;
                case 3:
                    ajaxIncluded = true;
                    result = calisanDao.calisanSil(new String(request.getParameter(Fields.ID).getBytes(StandardCharsets.UTF_8)));
                    break;
                default:
                    result = "";
            }
            PrintWriter out = response.getWriter();
            out.print(result);
        } else {
            if (request.getParameter(Fields.AD) != null && request.getParameter(Fields.SOYAD) != null && request.getParameter(Fields.GOREV) != null) {
                message = calisanDao.calisanEkle(
                        new String(request.getParameter(Fields.AD).getBytes(StandardCharsets.UTF_8)).trim(),
                        new String(request.getParameter(Fields.SOYAD).getBytes(StandardCharsets.UTF_8)).trim(),
                        new String(request.getParameter(Fields.GOREV).getBytes(StandardCharsets.UTF_8)).trim()
                );
            }

        }
        if (!ajaxIncluded) {

            int totalrecord = daoFunctions.recordAgg(BaseDao.CALISAN, Fields.COUNT, "*", "");
            int page = 1;
            if (request.getParameter(Fields.PAGE) != null)
                page = Integer.parseInt(request.getParameter(Fields.PAGE));

            int noofpages = (int) Math.ceil(totalrecord * 1.0 / Genel.ROWPERPAGE);

            List<Calisan> calisan = calisanDao.calisanListeGetirTum(page, null);

            request.setAttribute(Fields.CALISAN, calisan);
            request.setAttribute(Fields.MESSAGE, message);
            request.setAttribute(Fields.TOTALRECORD, totalrecord);
            request.setAttribute(Fields.NOOFPAGES, noofpages);
            request.setAttribute(Fields.CURRENTPAGE, page);
            request.getRequestDispatcher("calisan.jsp").forward(request, response);
        }
    }
}