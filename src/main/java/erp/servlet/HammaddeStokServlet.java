package erp.servlet;

import erp.dao.BaseDao;
import erp.dao.BilesenDao;
import erp.dao.FirmaDao;
import erp.dao.StokDao;
import erp.entity.Bilesen;
import erp.entity.Firma;
import erp.entity.Genel;
import erp.entity.Stok;
import erp.model.BilesenTip;
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
import java.util.ArrayList;
import java.util.List;

@WebServlet("/hammaddestok")
public class HammaddeStokServlet extends HttpServlet implements Serializable {
    private static final Logger logger = LoggerFactory.getLogger(HammaddeStokServlet.class);

    @Inject
    private BilesenDao bilesenDao;
    @Inject
    private FirmaDao firmaDao;
    @Inject
    private StokDao stokDao;
    @Inject
    private DAOFunctions daoFunctions;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        List<Bilesen> hammadde = bilesenDao.bilesenListeGetirTum(null, 0, null, null);
        List<Firma> firma = firmaDao.firmaListeGetirTum(0);
        List<Stok> stok = new ArrayList<Stok>();

        String stoklisteid = request.getParameter(Fields.STOKLISTEID);
        String bilesenid = request.getParameter(Fields.BILESENID);

        int totalrecord = 0;
        int page = 1;
        int noofpages = 0;
        int sumagg = 0;

        if (stoklisteid != null && Integer.parseInt(stoklisteid) == 1) {
            // PAGING
            totalrecord = daoFunctions.recordAgg(BaseDao.V_STOKBILESEN, Fields.COUNT, "*", "AND bilesenid=" + Integer.parseInt(bilesenid));
            sumagg = daoFunctions.recordAgg(BaseDao.V_STOKMIKTAR, Fields.SUM, Fields.MIKTAR, "AND bilesenid=" + Integer.parseInt(bilesenid));
            if (request.getParameter(Fields.PAGE) != null)
                page = Integer.parseInt(request.getParameter(Fields.PAGE));
            noofpages = (int) Math.ceil(totalrecord * 1.0 / Genel.ROWPERPAGE);
            // PAGING
            stok = stokDao.stokListeGetirTum(BilesenTip.HAMMADDEYARIMAMUL, Integer.parseInt(bilesenid), page, null);
        }

        request.setAttribute(Fields.HAMMADDE, hammadde);
        request.setAttribute(Fields.FIRMA, firma);
        request.setAttribute(Fields.STOK, stok);
        request.setAttribute(Fields.BILESENID, bilesenid);
        request.setAttribute(Fields.TOTALRECORD, totalrecord);
        request.setAttribute(Fields.CURRENTPAGE, page);
        request.setAttribute(Fields.NOOFPAGES, noofpages);
        request.setAttribute(Fields.SUMAGG, sumagg);
        request.getRequestDispatcher("hammaddestok.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html; charset=UTF-8");

        String bilesenid = request.getParameter(Fields.BILESENID);
        String bilesenekleid = request.getParameter("bilesenekleid");
        String message = null;
        boolean ajaxInAction = false;

        if (request.getParameter(Fields.ISLEMID) != null) {
            Integer islemid = Integer.parseInt(request.getParameter(Fields.ISLEMID));
            String tarih = request.getParameter(Fields.TARIH);
            String gkrno = request.getParameter(Fields.GKRNO);
            String miktar = request.getParameter(Fields.MIKTAR);
            String iade = request.getParameter(Fields.IADE);
            String bilesentipid = request.getParameter(Fields.BILESENTIPID);
            /* with ajax */
            if (islemid == 6) {
                ajaxInAction = true;
                String result = stokDao.stokKontrol(tarih, gkrno, miktar, iade, bilesenid, bilesentipid);
                PrintWriter out = response.getWriter();
                out.print(result);
            }
            logger.info("tarih:{}, gkrno: {}, miktar: {}, iade: {}", tarih, gkrno, miktar, iade);
        }

        if (!ajaxInAction) {
            List<Stok> stok;

            int totalrecord;
            int page = 1;
            int noofpages;
            int sumagg;

            if (bilesenekleid.equals("1")) {
                String result;
                String iade = request.getParameter("iade");
                if (iade == null) {
                    result = stokDao.stokEkle(
                            request.getParameter(Fields.BILESENID),
                            request.getParameter(Fields.MIKTAR),
                            request.getParameter(Fields.IRSALIYENO),
                            request.getParameter(Fields.LOT),
                            Util.gkrnoNumberCheck(request.getParameter(Fields.LOT)), /* gkrno */
                            request.getParameter(Fields.TARIH),
                            request.getParameter(Fields.NOT)
                    );
                } else {
                    result = stokDao.stokDus(
                            request.getParameter(Fields.BILESENID),
                            request.getParameter(Fields.MIKTAR),
                            request.getParameter(Fields.IRSALIYENO),
                            request.getParameter(Fields.LOT),
                            request.getParameter(Fields.LOT), /* gkrno*/
                            request.getParameter(Fields.TARIH),
                            request.getParameter(Fields.NOT)
                    );
                }
                message = result.equals("0") ? "Hata oluştu!.." : "";
            }

            // PAGING
            totalrecord = daoFunctions.recordAgg(BaseDao.V_STOKBILESEN, Fields.COUNT, "*", "AND bilesenid=" + Integer.parseInt(bilesenid));
            sumagg = daoFunctions.recordAgg(BaseDao.V_STOKMIKTAR, Fields.SUM, Fields.MIKTAR, "AND bilesenid=" + Integer.parseInt(bilesenid));
            if (request.getParameter(Fields.PAGE) != null)
                page = Integer.parseInt(request.getParameter(Fields.PAGE));
            noofpages = (int) Math.ceil(totalrecord * 1.0 / Genel.ROWPERPAGE);
            // PAGING
            stok = stokDao.stokListeGetirTum(BilesenTip.HAMMADDEYARIMAMUL, Integer.parseInt(bilesenid), page, null);

            List<Bilesen> hammadde = bilesenDao.bilesenListeGetirTum(null, 0, null, null);
            List<Firma> firma = firmaDao.firmaListeGetirTum(0);

            request.setAttribute(Fields.HAMMADDE, hammadde);
            request.setAttribute(Fields.FIRMA, firma);
            request.setAttribute(Fields.STOK, stok);
            request.setAttribute(Fields.BILESENID, bilesenid);
            request.setAttribute(Fields.TOTALRECORD, totalrecord);
            request.setAttribute(Fields.CURRENTPAGE, page);
            request.setAttribute(Fields.NOOFPAGES, noofpages);
            request.setAttribute(Fields.SUMAGG, sumagg);
            request.setAttribute(Fields.MESSAGE, message);
            request.getRequestDispatcher("hammaddestok.jsp").forward(request, response);
        }
    }
}
