package erp.servlet;

import erp.dao.BaseDao;
import erp.dao.BilesenDao;
import erp.dao.FirmaDao;
import erp.dao.HammaddeDao;
import erp.entity.Bilesen;
import erp.entity.Firma;
import erp.entity.Genel;
import erp.util.Fields;
import erp.util.Util;
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

@WebServlet("/hammadde")
public class HammaddeServlet extends HttpServlet implements Serializable {
    private static long token = 1L;
    private static final Logger logger = LoggerFactory.getLogger(HammaddeServlet.class);

    @Inject
    private FirmaDao firmaDao;
    @Inject
    private BilesenDao bilesenDao;
    @Inject
    private HammaddeDao hammaddeDao;
    @Inject
    private DAOFunctions daoFunctions;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String message = "";
        String bilesenid = request.getParameter("bilesenidara");
        String stoklisteid = request.getParameter(Fields.STOKLISTEID);
        List<Firma> firma = firmaDao.firmaListeGetirTum(0);

        int totalrecord = daoFunctions.recordAgg(BaseDao.BILESEN, Fields.COUNT, "*", "");
        int page = 1;
        if (request.getParameter(Fields.PAGE) != null)
            page = Integer.parseInt(request.getParameter(Fields.PAGE));

        int noofpages = (int) Math.ceil(totalrecord * 1.0 / Genel.ROWPERPAGE);

        List<Bilesen> hammaddetum = bilesenDao.bilesenListeGetirTum(null, 0, null, null);
        ;
        List<Bilesen> hammadde = bilesenDao.bilesenListeGetirTum(null, page, (Util.isNotEmptyOrNull(bilesenid)) ? Integer.parseInt(bilesenid) : null, null);
        ;

        token = Util.getToken();
        request.setAttribute(Fields.TOKEN, token);
        request.setAttribute(Fields.FIRMA, firma);
        request.setAttribute(Fields.HAMMADDE, hammadde);
        request.setAttribute(Fields.HAMMADDETUM, hammaddetum);
        request.setAttribute(Fields.MESSAGE, message);
        request.setAttribute(Fields.TOTALRECORD, totalrecord);
        request.setAttribute(Fields.CURRENTPAGE, page);
        request.setAttribute(Fields.NOOFPAGES, noofpages);
        request.setAttribute(Fields.BILESENID, bilesenid);
        request.setAttribute(Fields.STOKLISTEID, stoklisteid);
        request.getRequestDispatcher("hammadde.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        response.setContentType("text/html; charset=UTF-8");

        String islemid = request.getParameter(Fields.ISLEMID);
        String bilesenid = null;
        String stoklisteid = null;
        String message = "-1";
        if (islemid != null && !islemid.isEmpty()) {
            Integer result = 0;
            switch (Integer.parseInt(islemid)) {
                case 1:
                    message = bilesenDao.bilesenGuncelle(
                            new String(request.getParameter(Fields.BILESENID).getBytes(StandardCharsets.UTF_8)),
                            new String(request.getParameter(Fields.BIRIMID).getBytes(StandardCharsets.UTF_8)),
                            new String(request.getParameter(Fields.FIRMAID).getBytes(StandardCharsets.UTF_8)),
                            new String(request.getParameter(Fields.BILESENKOD).getBytes(StandardCharsets.UTF_8)),
                            new String(request.getParameter(Fields.BILESENAD).getBytes(StandardCharsets.UTF_8)),
                            new String(request.getParameter(Fields.DURUM).getBytes(StandardCharsets.UTF_8)),
                            new String(request.getParameter(Fields.FIYATID).getBytes(StandardCharsets.UTF_8)),
                            new String(request.getParameter(Fields.FIYAT).getBytes(StandardCharsets.UTF_8)),
                            new String(request.getParameter(Fields.KRITIKESIK).getBytes(StandardCharsets.UTF_8))
                    );
                    break;
                case 3:
                    message = bilesenDao.bilesenSil(new String(request.getParameter(Fields.BILESENID).getBytes(StandardCharsets.UTF_8)));
                    break;
            }
            PrintWriter out = response.getWriter();
            out.print(result);
        } else {
            /* Prevent Resubmit */
            if (Util.isNotEmptyOrNull(request.getParameter(Fields.STOKLISTEID)) &&
                    Integer.parseInt(request.getParameter(Fields.STOKLISTEID)) == 1) {
                bilesenid = request.getParameter("bilesenidara");
                stoklisteid = request.getParameter(Fields.STOKLISTEID);
            } else if (Long.valueOf(request.getParameter(Fields.TOKEN)) == token) {
                message = hammaddeDao.hammaddeEkle(
                        new String(request.getParameter("hamkod").getBytes(StandardCharsets.UTF_8)),
                        new String(request.getParameter("hamad").getBytes(StandardCharsets.UTF_8)),
                        new String(request.getParameter("hambirim").getBytes(StandardCharsets.UTF_8)),
                        new String(request.getParameter("hamfirma").getBytes(StandardCharsets.UTF_8)),
                        new String(request.getParameter("hamtip").getBytes(StandardCharsets.UTF_8)),
                        new String(request.getParameter("hamfiyatid").getBytes(StandardCharsets.UTF_8)),
                        new String(request.getParameter("hamfiyat").getBytes(StandardCharsets.UTF_8)),
                        new String(request.getParameter("hamkritikesik").getBytes(StandardCharsets.UTF_8))
                );
            } else {
                message = "-1";
            }
            /* PAGING */
            String filter = bilesenid == null ? "" : "AND id=" + bilesenid;
            int totalrecord = daoFunctions.recordAgg(BaseDao.BILESEN, Fields.COUNT, "*", filter);
            int page = 1;
            if (request.getParameter(Fields.PAGE) != null)
                page = Integer.parseInt(request.getParameter(Fields.PAGE));
            int noofpages = (int) Math.ceil(totalrecord * 1.0 / Genel.ROWPERPAGE);
            /* PAGING */

            List<Bilesen> hammaddetum = bilesenDao.bilesenListeGetirTum(null, 0, null, null);
            List<Bilesen> hammadde = bilesenDao.bilesenListeGetirTum(null, page, (Util.isNotEmptyOrNull(bilesenid)) ? Integer.parseInt(bilesenid) : null, null);

            List<Firma> firma = firmaDao.firmaListeGetirTum(0);

            token = Util.getToken();
            request.setAttribute(Fields.TOKEN, token);
            request.setAttribute(Fields.FIRMA, firma);
            request.setAttribute(Fields.HAMMADDE, hammadde);
            request.setAttribute(Fields.HAMMADDETUM, hammaddetum);
            request.setAttribute(Fields.TOTALRECORD, totalrecord);
            request.setAttribute(Fields.CURRENTPAGE, page);
            request.setAttribute(Fields.NOOFPAGES, noofpages);
            request.setAttribute(Fields.MESSAGE, message);
            request.setAttribute(Fields.BILESENID, bilesenid);
            request.setAttribute(Fields.STOKLISTEID, stoklisteid);
            request.getRequestDispatcher("hammadde.jsp").forward(request, response);
        }
    }
}