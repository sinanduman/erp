package erp.servlet;

import erp.dao.BaseDao;
import erp.dao.FirmaDao;
import erp.entity.Firma;
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

@WebServlet("/firma")
public class FirmaServlet extends HttpServlet implements Serializable {
    private static final Logger logger = LoggerFactory.getLogger(FirmaServlet.class);

    @Inject
    private FirmaDao firmaDao;
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
        if (islemid != null && islemid != "") {
            String result = "";
            switch (Integer.parseInt(islemid)) {
                case 1:
                    ajaxIncluded = true;
                    result = firmaDao.firmaGuncelle(
                            new String(request.getParameter(Fields.ID).getBytes(StandardCharsets.UTF_8)),
                            new String(request.getParameter(Fields.AD).getBytes(StandardCharsets.UTF_8)),
                            new String(request.getParameter(Fields.TEL).getBytes(StandardCharsets.UTF_8)),
                            new String(request.getParameter(Fields.ADRES).getBytes(StandardCharsets.UTF_8)),
                            new String(request.getParameter(Fields.EMAIL).getBytes(StandardCharsets.UTF_8))
                    );
                    break;
                case 3:
                    ajaxIncluded = true;
                    result = firmaDao.firmaSil(new String(request.getParameter(Fields.ID).getBytes(StandardCharsets.UTF_8)));
                    break;
            }
            PrintWriter out = response.getWriter();
            out.print(result);
        } else {
            if (request.getParameter(Fields.FIRMA_AD) != null && request.getParameter(Fields.FIRMA_TEL) != null && request.getParameter(Fields.FIRMA_ADRES) != null) {
                message = firmaDao.firmaEkle(
                        new String(request.getParameter(Fields.FIRMA_AD).getBytes(StandardCharsets.UTF_8)),
                        new String(request.getParameter(Fields.FIRMA_TEL).getBytes(StandardCharsets.UTF_8)),
                        new String(request.getParameter(Fields.FIRMA_ADRES).getBytes(StandardCharsets.UTF_8)),
                        new String(request.getParameter(Fields.FIRMA_EMAIL).getBytes(StandardCharsets.UTF_8))
                );
            }
        }

        if (!ajaxIncluded) {
            int totalrecord = daoFunctions.recordAgg(BaseDao.FIRMA, Fields.COUNT, "*", "");
            int page = 1;
            if (request.getParameter(Fields.PAGE) != null)
                page = Integer.parseInt(request.getParameter(Fields.PAGE));

            int noofpages = (int) Math.ceil(totalrecord * 1.0 / Genel.ROWPERPAGE);

            List<Firma> firma = firmaDao.firmaListeGetirTum(page);

            request.setAttribute(Fields.FIRMA, firma);
            request.setAttribute(Fields.MESSAGE, message);
            request.setAttribute(Fields.TOTALRECORD, totalrecord);
            request.setAttribute(Fields.NOOFPAGES, noofpages);
            request.setAttribute(Fields.CURRENTPAGE, page);
            request.getRequestDispatcher("firma.jsp").forward(request, response);
        }
    }
}
