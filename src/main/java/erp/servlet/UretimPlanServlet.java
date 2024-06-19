package erp.servlet;

import erp.dao.*;
import erp.entity.*;
import erp.model.UretimDurum;
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
import java.util.Date;
import java.util.List;

@WebServlet("/uretimplan")
public class UretimPlanServlet extends HttpServlet implements Serializable {
    private static final Logger logger = LoggerFactory.getLogger(UretimPlanServlet.class);

    @Inject
    private UretimDao uretimDao;
    @Inject
    private MamulDao mamulDao;
    @Inject
    private FirmaDao firmaDao;
    @Inject
    private HataDao hataDao;
    @Inject
    private MakinaDao makinaDao;
    @Inject
    private CalisanDao calisanDao;
    @Inject
    private DAOFunctions daoFunctions;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        response.setContentType("text/html; charset=UTF-8");

        String uretimplanid = request.getParameter(Fields.URETIMPLANID) == null ? "0" : request.getParameter(Fields.URETIMPLANID);
        String excelsql = "";
        String message = "";
        String tarih = (request.getParameter(Fields.TARIH) == null || request.getParameter(Fields.TARIH).equals("")) ? Util.getTarih(new Date()) : Util.date_tr_to_eng(request.getParameter(Fields.TARIH));
        String makinaid = (request.getParameter(Fields.MAKINAID) == null || request.getParameter(Fields.MAKINAID).equals("")) ? "" : request.getParameter(Fields.MAKINAID);
        String filter1 = "";
        String filter2 = "";
        String filter0 = "";

        filter1 = " and tarih='" + Util.date_tr_to_eng(tarih) + "'";
        if (!makinaid.equals("")) {
            filter2 = " and makinaid=" + makinaid;
        }
        filter0 = filter1 + filter2;


        if (Integer.parseInt(uretimplanid) > 0) {
            String gercekuretim = request.getParameter(Fields.GERCEKURETIM);
            String hata = request.getParameter(Fields.HATA);
            String hataid = request.getParameter(Fields.HATASEBEPID);
            message = uretimDao.uretimPlanGuncelle(
                    Integer.parseInt(gercekuretim),
                    (hata == null || hata.isEmpty()) ? 0 : Integer.parseInt(hata),
                    Integer.parseInt(hataid),
                    Integer.parseInt(uretimplanid)
            );
        }

        excelsql = "SELECT * FROM " + BaseDao.V_URETIMPLAN_TUM_PLAN + " WHERE tamamlandi=1 " + filter0;
        logger.info("planservlet: {}", excelsql);

        List<HataSebep> hatasebep = hataDao.hataSebepListeGetir();
        List<Makina> makina = makinaDao.makinaListeGetirTum(0);
        List<Calisan> calisan = calisanDao.calisanListeGetirTum(0, null);
        List<Firma> firma = firmaDao.firmaListeGetirTum(0);
        List<Mamul> mamul = mamulDao.mamulListeGetir(null, 0, null);

        int totalrecord = daoFunctions.recordAgg(BaseDao.V_URETIMPLAN_TUM, Fields.COUNT, "*", "AND tamamlandi=1 " + filter0);
        int page = 1;
        if (request.getParameter(Fields.PAGE) != null)
            page = Integer.parseInt(request.getParameter(Fields.PAGE));

        int noofpages = (int) Math.ceil(totalrecord * 1.0 / Genel.ROWPERPAGE);

        List<UretimPlan> uretimplan = uretimDao.uretimPlanListeGetir(UretimDurum.TAMAMLANMIS, page, filter0, BaseDao.V_URETIMPLAN_TUM_PLAN);

        request.setAttribute(Fields.HATASEBEP, hatasebep);
        request.setAttribute(Fields.MAKINA, makina);
        request.setAttribute(Fields.CALISAN, calisan);
        request.setAttribute(Fields.FIRMA, firma);
        request.setAttribute(Fields.MAMUL, mamul);
        request.setAttribute(Fields.URETIMPLAN, uretimplan);
        request.setAttribute(Fields.TOTALRECORD, totalrecord);
        request.setAttribute(Fields.CURRENTPAGE, page);
        request.setAttribute(Fields.NOOFPAGES, noofpages);
        request.setAttribute(Fields.TARIH, tarih);
        request.setAttribute(Fields.MAKINAID, makinaid);
        request.setAttribute(Fields.MESSAGE, message);
        request.setAttribute(Fields.EXCELSQL, excelsql);
        request.getRequestDispatcher("uretimplan.jsp").forward(request, response);
    }
}