package erp.servlet;

import erp.dao.*;
import erp.entity.*;
import erp.model.UretimDurum;
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
import java.util.List;

@WebServlet("/uretimtakip")
public class UretimTakipServlet extends HttpServlet implements Serializable {

    private static final Logger logger = LoggerFactory.getLogger(UretimTakipServlet.class);

    @Inject
    private StokDao stokDao;
    @Inject
    private UretimDao uretimDao;
    @Inject
    private HataDao hataDao;
    @Inject
    private MakinaDao makinaDao;
    @Inject
    private CalisanDao calisanDao;
    @Inject
    private FirmaDao firmaDao;
    @Inject
    private MamulDao mamulDao;
    @Inject
    private BilesenDao bilesenDao;
    @Inject
    private IzlemeNoDao izlemeNoDao;
    @Inject
    private DAOFunctions daoFunctions;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        response.setContentType("text/html; charset=UTF-8");

        String message = "";
        boolean ajaxInAction = false;

        logger.info("islemid: {}", request.getParameter(Fields.ISLEMID));
        if (request.getParameter(Fields.ISLEMID) != null) {
            int islemid = request.getParameter(Fields.ISLEMID) == null ? -1 : Integer.parseInt(request.getParameter(Fields.ISLEMID));
            String uretimplanid = request.getParameter(Fields.URETIMPLANID);
            String mamulid = request.getParameter(Fields.MAMULID);
            String uret_miktar = request.getParameter(Fields.URETILENMIKTAR);
            String hata_miktar = request.getParameter("hatalimiktar");
            String plan_miktar = request.getParameter("tahminiuretim");

            if (islemid == 3) {  /* Stok Yeterli mi ? */
                ajaxInAction = true;
                String result = stokDao.stokYeterli(
                        Integer.parseInt(uretimplanid),
                        Integer.parseInt(mamulid),
                        Integer.parseInt(uret_miktar)
                );
                PrintWriter out = response.getWriter();
                out.print(result);
            } else if (islemid == 2) {  /* Silme */
                ajaxInAction = true;
                String result = uretimDao.uretimPlanSil(Integer.parseInt(uretimplanid));
                PrintWriter out = response.getWriter();
                out.print(result);
            } else if (islemid == 1) {  /* Onaylama */
                ajaxInAction = true;
                String result = uretimDao.uretimPlanOk(Integer.parseInt(uretimplanid));
                PrintWriter out = response.getWriter();
                out.print(result);
            } else if (islemid == 0) { /* Takip ekleme */
                logger.info("islemid: {}", request.getParameter(Fields.ISLEMID));
                String baszaman = request.getParameter("bassaat").toString() + ":" + request.getParameter("basdakika").toString() + ":" + "00";
                String bitzaman = request.getParameter("bitsaat").toString() + ":" + request.getParameter("bitdakika").toString() + ":" + "00";
                String hataliad = (request.getParameter(Fields.HATALIADET).trim().isEmpty()) ? "0" : request.getParameter(Fields.HATALIADET);
                int result = uretimDao.uretimTakipEkleGuncelle(
                        request.getParameter(Fields.MAMULID),
                        request.getParameter(Fields.URETIMADET),
                        hataliad,
                        request.getParameter(Fields.MAKINAID),
                        request.getParameter(Fields.CALISANID),
                        request.getParameter(Fields.TARIH),
                        baszaman,
                        bitzaman,
                        request.getParameter(Fields.HATASEBEPID),
                        request.getParameter(Fields.DURUSSEBEPID),
                        request.getParameter(Fields.DURUSZAMAN),
                        Integer.parseInt(request.getParameter(Fields.MAMULIZLENO)),
                        Integer.parseInt(uretimplanid),
                        Integer.parseInt(plan_miktar)
                );
                /* Ilk mamul uretim takip basarili
                Es mamul uretim takip yapiliyor */
                if (result != -1
                        && request.getParameter("cxb_esmamul") != null
                        && !request.getParameter(Fields.ESMAMULID).equals("")
                        && !request.getParameter(Fields.ESMAMULID).equals("0")
                        && Integer.parseInt(request.getParameter(Fields.ESMAMULID)) > 0) {
                    int result2 = uretimDao.uretimTakipEkleGuncelle(
                            request.getParameter(Fields.ESMAMULID),
                            request.getParameter(Fields.URETIMADET),
                            hataliad,
                            request.getParameter(Fields.MAKINAID),
                            request.getParameter(Fields.CALISANID),
                            request.getParameter(Fields.TARIH),
                            baszaman,
                            bitzaman,
                            request.getParameter(Fields.HATASEBEPID),
                            request.getParameter(Fields.DURUSSEBEPID),
                            request.getParameter(Fields.DURUSZAMAN),
                            Integer.parseInt(request.getParameter("esmamulizleno")),
                            Integer.parseInt(uretimplanid),
                            Integer.parseInt(plan_miktar)
                    );
                    message = (result2 == -1) ? "Hata oluştu" : "";
                }
                message = (result == -1) ? "Hata oluştu" : "";
            }
        }
        if (!ajaxInAction) {
            List<HataSebep> hatasebep = hataDao.hataSebepListeGetir();
            List<DurusSebep> durussebep = hataDao.durusSebepListeGetir();
            List<Makina> makina = makinaDao.makinaListeGetirTum(0);
            List<Calisan> calisan = calisanDao.calisanListeGetirTum(0, 1);
            List<Firma> firma = firmaDao.firmaListeGetirTum(0);
            List<Mamul> mamul = mamulDao.mamulListeGetir(null, 0, 1);
            List<Bilesen> bilesen = bilesenDao.bilesenListeGetirTum(null, 0, null, null);

            int totalrecord = daoFunctions.recordAgg(BaseDao.V_URETIMPLAN_TUM_TAKIP, Fields.COUNT, "*", "AND tamamlandi=0 ");
            int page = 1;
            if (request.getParameter(Fields.PAGE) != null)
                page = Integer.parseInt(request.getParameter(Fields.PAGE));

            int noofpages = (int) Math.ceil(totalrecord * 1.0 / Genel.ROWPERPAGE);

            List<UretimPlan> uretimplan = uretimDao.uretimPlanListeGetir(UretimDurum.BEKLEYEN, page, "", BaseDao.V_URETIMPLAN_TUM_TAKIP);

            request.setAttribute(Fields.HATASEBEP, hatasebep);
            request.setAttribute(Fields.DURUSSEBEP, durussebep);
            request.setAttribute(Fields.MAKINA, makina);
            request.setAttribute(Fields.CALISAN, calisan);
            request.setAttribute(Fields.FIRMA, firma);
            request.setAttribute(Fields.MAMUL, mamul);
            request.setAttribute(Fields.BILESEN, bilesen);
            request.setAttribute(Fields.URETIMPLAN, uretimplan);
            request.setAttribute(Fields.TOTALRECORD, totalrecord);
            request.setAttribute(Fields.CURRENTPAGE, page);
            request.setAttribute(Fields.NOOFPAGES, noofpages);
            request.setAttribute(Fields.MESSAGE, message);
            request.getRequestDispatcher("uretimtakip.jsp").forward(request, response);
        }
    }
}