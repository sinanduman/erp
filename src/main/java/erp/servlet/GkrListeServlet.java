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
import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/gkrliste")
public class GkrListeServlet extends HttpServlet implements Serializable {
    private static final Logger logger = LoggerFactory.getLogger(GkrListeServlet.class);

    @Inject
    private BilesenDao bilesenDao;
    @Inject
    private FirmaDao firmaDao;
    @Inject
    private StokDao stokDao;
    @Inject
    private DAOFunctions daoFunctions;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html; charset=UTF-8");

        int totalrecord = 0;
        int page0 = 1;
        int noofpages = 0;
        int bilesenid = 0;

        String bas_tarih = request.getParameter(Fields.BAS_TARIH);
        String bit_tarih = request.getParameter(Fields.BIT_TARIH);
        String page = request.getParameter(Fields.PAGE);
        String siralama = request.getParameter(Fields.SIRALAMA);
        String siralamayon = request.getParameter(Fields.SIRALAMAYON);
        String excelsql = "";
        String filter0 = "";
        String filter1 = "";
        String filter2 = "";
        String andYes = " AND ";

        if (Util.isNotEmptyOrNull(request.getParameter(Fields.BILESENID))) {
            bilesenid = Integer.parseInt(request.getParameter(Fields.BILESENID));
        }

        List<Bilesen> hammadde = bilesenDao.bilesenListeGetirTum(BilesenTip.HAMMADDEYARIMAMUL, 0, null, null);
        List<Firma> firma = firmaDao.firmaListeGetirTum(0);

        Map<String, String> param = new HashMap<>();
        param.put(Fields.ISLEMYONU, String.valueOf(Genel.IslemYonuGiris));
        param.put(Fields.ROWPERPAGE, String.valueOf(Genel.ROWPERLONGPAGE));

        filter1 = (bilesenid == 0) ? "" : " and bilesenid=" + bilesenid;

        if (bas_tarih == null || bit_tarih == null || bas_tarih.isEmpty() || bit_tarih.isEmpty()) {
            bas_tarih = LocalDate.now().minusDays(30).format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
            bit_tarih = LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        }

        if (bas_tarih != null && bit_tarih != null && !bas_tarih.isEmpty() && !bit_tarih.isEmpty()) {
            filter2 = andYes +
                    " ( giristarihi BETWEEN '" + Util.date_tr_to_eng(bas_tarih) +
                    "' AND '" + Util.date_tr_to_eng(bit_tarih) +
                    "')";
            param.put(Fields.BAS_TARIH, bas_tarih);
            param.put(Fields.BIT_TARIH, bit_tarih);
        }
        if (siralama != null && !siralama.isEmpty() && siralamayon != null && !siralamayon.isEmpty()) {
            param.put(Fields.SIRALAMA, siralama);
            param.put(Fields.SIRALAMAYON, siralamayon);
        }

        filter0 = filter1 + filter2;
        excelsql = filter0;

        // PAGING        
        totalrecord = daoFunctions.recordAgg(BaseDao.V_STOKBILESEN, Fields.COUNT, "*", "AND bilesentipid IN (1,2) and islemyonu=" + Genel.IslemYonuGiris + filter0);
        if (request.getParameter(Fields.PAGE) != null)
            page0 = Integer.parseInt(page);
        noofpages = (int) Math.ceil(totalrecord * 1.0 / Genel.ROWPERLONGPAGE);
        // PAGING

        List<Stok> stok = stokDao.stokListeGetirTum(BilesenTip.HAMMADDEYARIMAMUL, bilesenid, page0, param);

        request.setAttribute(Fields.HAMMADDE, hammadde);
        request.setAttribute(Fields.FIRMA, firma);
        request.setAttribute(Fields.STOK, stok);
        request.setAttribute(Fields.BAS_TARIH, bas_tarih);
        request.setAttribute(Fields.BIT_TARIH, bit_tarih);
        request.setAttribute(Fields.SIRALAMA, siralama);
        request.setAttribute(Fields.SIRALAMAYON, siralamayon);
        request.setAttribute(Fields.TOTALRECORD, totalrecord);
        request.setAttribute(Fields.CURRENTPAGE, page0);
        request.setAttribute(Fields.NOOFPAGES, noofpages);
        request.setAttribute(Fields.BILESENID, bilesenid);
        request.setAttribute(Fields.EXCELSQL, excelsql);
        request.getRequestDispatcher("gkrliste.jsp").forward(request, response);
    }
}
