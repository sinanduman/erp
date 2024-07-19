package erp.servlet;

import erp.dao.BaseDao;
import erp.dao.FirmaDao;
import erp.dao.MamulDao;
import erp.dao.StokDao;
import erp.entity.Firma;
import erp.entity.Genel;
import erp.entity.Mamul;
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

@WebServlet("/mamulstok")
public class MamulStokServlet extends HttpServlet implements Serializable {
    private static final Logger logger = LoggerFactory.getLogger(MamulStokServlet.class);

    @Inject
    private MamulDao mamulDao;
    @Inject
    private FirmaDao firmaDao;
    @Inject
    private StokDao stokDao;
    @Inject
    private DAOFunctions daoFunctions;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        List<Mamul> mamul = mamulDao.mamulListeStokGetir(null, 0, 1);
        List<Firma> firma = firmaDao.firmaListeGetirTum(0);

        List<Stok> stok = new ArrayList<>();
        String stoklisteid = request.getParameter(Fields.STOKLISTEID);
        String mamulid = request.getParameter(Fields.MAMULID);

        int totalrecord = 0;
        int page = 1;
        int noofpages = 0;
        int sumagg = 0;

        if (stoklisteid != null && Integer.parseInt(stoklisteid) == 1) {

            // PAGING
            // totalrecord = DAOFunctions.recordCount("stok"," where id= " +
            // stoklisteid);
            totalrecord = daoFunctions.recordAgg(BaseDao.STOK, Fields.COUNT, "*", "AND bilesenid=" + Integer.parseInt(mamulid));
            sumagg = daoFunctions.recordAgg(BaseDao.V_STOKMIKTAR, Fields.SUM, Fields.MIKTAR, "AND bilesenid=" + Integer.parseInt(mamulid));
            if (request.getParameter(Fields.PAGE) != null)
                page = Integer.parseInt(request.getParameter(Fields.PAGE));
            noofpages = (int) Math.ceil(totalrecord * 1.0 / Genel.ROWPERPAGE);
            // PAGING
            stok = stokDao.stokListeGetirTum(BilesenTip.MAMUL, Integer.parseInt(mamulid), page, null);

            logger.info("{}", totalrecord);
            logger.info("{}", noofpages);
        }

        request.setAttribute(Fields.MAMUL, mamul);
        request.setAttribute(Fields.FIRMA, firma);
        request.setAttribute(Fields.STOK, stok);
        request.setAttribute(Fields.TOTALRECORD, totalrecord);
        request.setAttribute(Fields.SUMAGG, sumagg);
        request.setAttribute(Fields.CURRENTPAGE, page);
        request.setAttribute(Fields.NOOFPAGES, noofpages);
        request.setAttribute(Fields.MAMULID, mamulid);
        request.setAttribute(Fields.EXCELSQL, mamulid);

        request.getRequestDispatcher("mamulstok.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        response.setContentType("text/html; charset=UTF-8");

        String mamulid = request.getParameter(Fields.MAMULID);
        String mamulekleid = request.getParameter("mamulekleid");
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
                String result = stokDao.stokKontrol(tarih, gkrno, miktar, iade, mamulid, bilesentipid);
                PrintWriter out = response.getWriter();
                out.print(result);
            }
            logger.info("tarih: {}, gkrno: {}, miktar: {}, iade: {}", tarih, gkrno, miktar, iade);
        }

        if (!ajaxInAction) {
            int totalrecord = 0;
            int page = 1;
            int noofpages = 0;
            int sumagg = 0;

            if (mamulekleid.equals("1")) {
                String result = "";
                String iade = request.getParameter("iade");
                if (iade == null) {
                    result = stokDao.stokEkle(
                            request.getParameter(Fields.MAMULID),
                            request.getParameter(Fields.MIKTAR),
                            request.getParameter(Fields.IRSALIYENO),
                            request.getParameter(Fields.LOT),
                            Util.gkrnoNumberCheck(request.getParameter(Fields.LOT)), /* gkrno */
                            request.getParameter(Fields.TARIH),
                            request.getParameter(Fields.NOT)
                    );
                } else {
                    result = stokDao.stokDus(
                            request.getParameter(Fields.MAMULID),
                            request.getParameter(Fields.MIKTAR),
                            request.getParameter(Fields.IRSALIYENO),
                            request.getParameter(Fields.LOT),
                            request.getParameter(Fields.LOT), /* gkrno */
                            request.getParameter(Fields.TARIH),
                            request.getParameter(Fields.NOT)
                    );
                }
                message = result.equals("0") ? "Hata oluştu!.." : "";
            }

            totalrecord = daoFunctions.recordAgg(BaseDao.STOK, Fields.COUNT, "*", "AND bilesenid=" + Integer.parseInt(mamulid));
            sumagg = daoFunctions.recordAgg(BaseDao.V_STOKMIKTAR, Fields.SUM, Fields.MIKTAR, "AND bilesenid=" + Integer.parseInt(mamulid));
            if (request.getParameter(Fields.PAGE) != null)
                page = Integer.parseInt(request.getParameter(Fields.PAGE));
            noofpages = (int) Math.ceil(totalrecord * 1.0 / Genel.ROWPERPAGE);
            List<Stok> stok = stokDao.stokListeGetirTum(BilesenTip.MAMUL, Integer.parseInt(mamulid), page, null);

            List<Mamul> mamul = mamulDao.mamulListeStokGetir(null, 0, 1);

            List<Firma> firma = firmaDao.firmaListeGetirTum(0);

            request.setAttribute(Fields.MAMUL, mamul);
            request.setAttribute(Fields.FIRMA, firma);
            request.setAttribute(Fields.STOK, stok);
            request.setAttribute(Fields.MESSAGE, message);
            request.setAttribute(Fields.TOTALRECORD, totalrecord);
            request.setAttribute(Fields.SUMAGG, sumagg);
            request.setAttribute(Fields.CURRENTPAGE, page);
            request.setAttribute(Fields.NOOFPAGES, noofpages);
            request.setAttribute(Fields.MAMULID, mamulid);
            request.setAttribute(Fields.EXCELSQL, mamulid);

            request.getRequestDispatcher("mamulstok.jsp").forward(request, response);
        }
    }
}
