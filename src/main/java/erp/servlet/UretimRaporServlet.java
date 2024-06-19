package erp.servlet;

import erp.dao.*;
import erp.entity.*;
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
import java.util.ArrayList;
import java.util.List;

@WebServlet("/uretimrapor")
public class UretimRaporServlet extends HttpServlet implements Serializable {
    private static final Logger logger = LoggerFactory.getLogger(UretimRaporServlet.class);

    @Inject
    private MakinaDao makinaDao;
    @Inject
    private CalisanDao calisanDao;
    @Inject
    private FirmaDao firmaDao;
    @Inject
    private MamulDao mamulDao;
    @Inject
    private UretimDao uretimDao;
    @Inject
    private DAOFunctions daoFunctions;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html; charset=UTF-8");

        String raporgetirid = request.getParameter("raporgetirid");
        String bilesenid = request.getParameter(Fields.BILESENID);
        String makinaid = request.getParameter(Fields.MAKINAID);
        String calisanid = request.getParameter(Fields.CALISANID);
        String firmaid = request.getParameter(Fields.FIRMAID);
        String bas_tarih = request.getParameter(Fields.BAS_TARIH);
        String bit_tarih = request.getParameter(Fields.BIT_TARIH);
        String page = request.getParameter(Fields.PAGE);
        String excelsql = "";

        List<Makina> makina = makinaDao.makinaListeGetirTum(0);
        List<Calisan> calisan = calisanDao.calisanListeGetirTum(0, null);
        List<Firma> firma = firmaDao.firmaListeGetirTum(0);

        int totalrecord = 0;
        int noofpages = 0;
        int page0 = 1;

        List<Mamul> mamul = mamulDao.mamulListeGetir(null, 0, null);
        List<UretimPlan> stokrapor = new ArrayList<UretimPlan>();

        if (raporgetirid != null || page != null) {
            String filter0 = "";
            String filter1 = "";
            String filter2 = "";
            String filter3 = "";
            String filter4 = "";
            String filter5 = "";
            String andYes = " AND ";

            if (bilesenid != null && !bilesenid.equals("")) {
                filter1 = andYes + " mamulid = " + Integer.parseInt(bilesenid);
            }
            if (makinaid != null && !makinaid.equals("")) {
                filter2 = andYes + " makinaid = " + Integer.parseInt(makinaid);
            }
            if (calisanid != null && !calisanid.equals("")) {
                filter3 = andYes + " calisanid = " + Integer.parseInt(calisanid);
            }
            if (firmaid != null && !firmaid.equals("")) {
                filter4 = andYes + " firmaid = " + Integer.parseInt(firmaid);
            }
            if (bas_tarih != null && bit_tarih != null && !bas_tarih.equals("") && !bit_tarih.equals("")) {
                filter5 = andYes + " ( tarih BETWEEN '" + Util.date_tr_to_eng(bas_tarih) + "' AND '" + Util.date_tr_to_eng(bit_tarih) + "')";
            }
            filter0 = filter1 + filter2 + filter3 + filter4 + filter5;

            // PAGING
            totalrecord = daoFunctions.recordAgg(BaseDao.V_URETIMPLAN_TUM_PLAN, Fields.COUNT, "*", filter0);
            if (request.getParameter(Fields.PAGE) != null)
                page0 = Integer.parseInt(page);
            noofpages = (int) Math.ceil(totalrecord * 1.0 / Genel.ROWPERPAGE);
            // PAGING

            stokrapor = uretimDao.uretimBilesenRapor(BaseDao.V_URETIMPLAN_TUM_PLAN, page0, filter0);
            logger.info("noofpages: " + noofpages + ", totalrecord: " + totalrecord + ", " + BaseDao.V_URETIMPLAN_TUM_PLAN + " WHERE 1=1" + filter0);
            excelsql = filter0;
        }

        request.setAttribute(Fields.MAKINA, makina);
        request.setAttribute(Fields.CALISAN, calisan);
        request.setAttribute(Fields.FIRMA, firma);
        request.setAttribute(Fields.MAMUL, mamul);
        request.setAttribute(Fields.STOKRAPOR, stokrapor);
        request.setAttribute(Fields.BILESENID, bilesenid);
        request.setAttribute(Fields.MAKINAID, makinaid);
        request.setAttribute(Fields.CALISANID, calisanid);
        request.setAttribute(Fields.FIRMAID, firmaid);
        request.setAttribute(Fields.BAS_TARIH, bas_tarih);
        request.setAttribute(Fields.BIT_TARIH, bit_tarih);
        request.setAttribute(Fields.TOTALRECORD, totalrecord);
        request.setAttribute(Fields.CURRENTPAGE, page0);
        request.setAttribute(Fields.NOOFPAGES, noofpages);
        request.setAttribute(Fields.EXCELSQL, excelsql);
        request.getRequestDispatcher("uretimrapor.jsp").forward(request, response);
    }
}
