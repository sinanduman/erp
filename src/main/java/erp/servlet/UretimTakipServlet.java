package erp.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import erp.dao.*;
import erp.entity.*;
import erp.model.UretimDurum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@WebServlet("/uretimtakip")
public class UretimTakipServlet extends HttpServlet implements Serializable {

    private static final Logger logger = LoggerFactory.getLogger(UretimTakipServlet.class);

    @Inject
    StokDao stokDao;
    @Inject
    UretimDao uretimDao;
    @Inject
    HataDao hataDao;
    @Inject
    MakinaDao makinaDao;
    @Inject
    CalisanDao calisanDao;
    @Inject
    FirmaDao firmaDao;
    @Inject
    MamulDao mamulDao;
    @Inject
    BilesenDao bilesenDao;
    @Inject
    IzlemeNoDao izlemeNoDao;
    @Inject
    DAOFunctions daoFunctions;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        response.setContentType("text/html; charset=UTF-8");

        String message      = "";
        Boolean ajaxInAction= false;

        logger.info(request.getParameter("islemid"));
        if(request.getParameter("islemid")!=null) {
            int islemid         = request.getParameter("islemid")==null ? -1 : Integer.parseInt(request.getParameter("islemid"));
            String uretimplanid = request.getParameter("uretimplanid");
            String mamulid      = request.getParameter("mamulid");
            String uret_miktar  = request.getParameter("uretilenmiktar");
            String hata_miktar  = request.getParameter("hatalimiktar");
            String plan_miktar  = request.getParameter("tahminiuretim");

            if(islemid==3) {  /* Stok Yeterli mi ? */
                ajaxInAction    = true;
                String result   = stokDao.stokYeterli(
                                Integer.parseInt(uretimplanid),
                                Integer.parseInt(mamulid),
                                Integer.parseInt(uret_miktar)
                                );
                PrintWriter out = response.getWriter();
                out.print(result);
            }
            else if(islemid==2) {  /* Silme */
                ajaxInAction    = true;
                String result   = uretimDao.uretimPlanSil(Integer.parseInt(uretimplanid));
                PrintWriter out = response.getWriter();
                out.print(result);
            } else if(islemid==1) {  /* Onaylama */
                ajaxInAction    = true;
                String result   = uretimDao.uretimPlanOk(Integer.parseInt(uretimplanid));
                PrintWriter out = response.getWriter();
                out.print(result);
            } else if(islemid==0){ /* Takip ekleme */
                logger.info("islemid: " +request.getParameter("islemid"));
                String baszaman = request.getParameter("bassaat").toString() +":"+ request.getParameter("basdakika").toString()+":"+"00";
                String bitzaman = request.getParameter("bitsaat").toString() +":"+ request.getParameter("bitdakika").toString()+":"+"00";
                String hataliad = (request.getParameter("hataliadet").trim().isEmpty()) ? "0" : request.getParameter("hataliadet");
                int result = uretimDao.uretimTakipEkleGuncelle(
                                request.getParameter("mamulid"),
                                request.getParameter("uretimadet"),
                                hataliad,
                                request.getParameter("makinaid"),
                                request.getParameter("calisanid"),
                                request.getParameter("tarih"),
                                baszaman,
                                bitzaman,
                                request.getParameter("hatasebepid"),
                                request.getParameter("durussebepid"),
                                request.getParameter("duruszaman"),
                                Integer.parseInt(request.getParameter("mamulizleno")),
                                Integer.parseInt(uretimplanid),
                                Integer.parseInt(plan_miktar)
                                );
                /* Ilk mamul uretim takip basarili
                Es mamul uretim takip yapiliyor */
                if(result !=-1
                        && request.getParameter("cxb_esmamul") != null
                        && !request.getParameter("esmamulid").equals("")
                        && !request.getParameter("esmamulid").equals("0")
                        && Integer.parseInt(request.getParameter("esmamulid"))>0){
                        int result2 = uretimDao.uretimTakipEkleGuncelle(
                                request.getParameter("esmamulid"),
                                request.getParameter("uretimadet"),
                                hataliad,
                                request.getParameter("makinaid"),
                                request.getParameter("calisanid"),
                                request.getParameter("tarih"),
                                baszaman,
                                bitzaman,
                                request.getParameter("hatasebepid"),
                                request.getParameter("durussebepid"),
                                request.getParameter("duruszaman"),
                                Integer.parseInt(request.getParameter("esmamulizleno")),
                                Integer.parseInt(uretimplanid),
                                Integer.parseInt(plan_miktar)
                        );
                        message = (result2==-1) ? "Hata oluştu" : "";
                }
                message = (result==-1) ? "Hata oluştu" : "";
            }
        }
        if(!ajaxInAction) {
            List<HataSebep> hatasebep = hataDao.hataSebepListeGetir();
            List<DurusSebep> durussebep = hataDao.durusSebepListeGetir();
            List<Makina> makina = makinaDao.makinaListeGetirTum(0);
            List<Calisan> calisan = calisanDao.calisanListeGetirTum(0, 1);
            List<Firma> firma = firmaDao.firmaListeGetirTum(0);
            List<Mamul> mamul = mamulDao.mamulListeGetir(null, 0, 1);
            List<Bilesen> bilesen = bilesenDao.bilesenListeGetirTum(null, 0, null, null);

            int totalrecord = daoFunctions.recordAgg(BaseDao.V_URETIMPLAN_TUM_TAKIP,"count","*"," where tamamlandi=0 ");
            int page = 1;
            if(request.getParameter("page") != null)
                page = Integer.parseInt(request.getParameter("page"));

            int noofpages = (int) Math.ceil(totalrecord * 1.0 / Genel.ROWPERPAGE);

            List<UretimPlan> uretimplan = uretimDao.uretimPlanListeGetir(UretimDurum.BEKLEYEN, page, "", BaseDao.V_URETIMPLAN_TUM_TAKIP);

            request.setAttribute("hatasebep", hatasebep);
            request.setAttribute("durussebep", durussebep);
            request.setAttribute("makina", makina);
            request.setAttribute("calisan", calisan);
            request.setAttribute("firma", firma);
            request.setAttribute("mamul", mamul);
            request.setAttribute("bilesen", bilesen);
            request.setAttribute("uretimplan", uretimplan);
            request.setAttribute("totalrecord", totalrecord);
            request.setAttribute("currentpage", page);
            request.setAttribute("noofpages", noofpages);
            request.setAttribute("message", message);
            request.getRequestDispatcher("uretimtakip.jsp").forward(request, response);
        }
    }
}