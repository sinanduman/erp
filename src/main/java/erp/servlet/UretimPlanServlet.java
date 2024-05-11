package erp.servlet;

import erp.dao.*;
import erp.entity.*;
import erp.model.UretimDurum;
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
    UretimDao uretimDao;
    @Inject
    MamulDao mamulDao;
    @Inject
    FirmaDao firmaDao;
    @Inject
    HataDao hataDao;
    @Inject
    MakinaDao makinaDao;
    @Inject
    CalisanDao calisanDao;
    @Inject
    DAOFunctions daoFunctions;
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost( request,  response);
    }
    
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        response.setContentType("text/html; charset=UTF-8");

        String uretimplanid = request.getParameter("uretimplanid") == null ? "0" : request.getParameter("uretimplanid");
        String excelsql     = "";
        String message      = "";
        String tarih        = (request.getParameter("tarih")==null || request.getParameter("tarih").equals("")) ? Util.getTarih(new Date()) : Util.date_tr_to_eng(request.getParameter("tarih"));
        String makinaid     = (request.getParameter("makinaid")==null || request.getParameter("makinaid").equals("")) ? "" : request.getParameter("makinaid");
        String filter1      = "";
        String filter2      = "";
        String filter0      = "";
        
        filter1     = " and tarih='" + Util.date_tr_to_eng(tarih)+ "'";
        if(!makinaid.equals("")) {
            filter2 = " and makinaid=" + makinaid;
        }                
        filter0     = filter1 + filter2;
        

        if(Integer.parseInt(uretimplanid)>0) {
            String gercekuretim = request.getParameter("gercekuretim");
            String hata         = request.getParameter("hata");
            String hataid       = request.getParameter("hatasebepid");
            message = uretimDao.uretimPlanGuncelle(
                            Integer.parseInt(gercekuretim),
                            (hata == null || hata.isEmpty()) ? 0 : Integer.parseInt(hata),
                            Integer.parseInt(hataid),
                            Integer.parseInt(uretimplanid)
                            );
        }

        excelsql = "SELECT * FROM " + BaseDao.V_URETIMPLAN_TUM_PLAN + " WHERE tamamlandi=1 " + filter0;
        logger.info("planservlet: " + excelsql);

        List<HataSebep> hatasebep = hataDao.hataSebepListeGetir();
        List<Makina> makina = makinaDao.makinaListeGetirTum(0);
        List<Calisan> calisan = calisanDao.calisanListeGetirTum(0, null);
        List<Firma> firma =  firmaDao.firmaListeGetirTum(0);
        List<Mamul> mamul = mamulDao.mamulListeGetir(null, 0, null);
        
        int totalrecord = daoFunctions.recordCount(BaseDao.V_URETIMPLAN_TUM," where tamamlandi=1 " + filter0);
        int page = 1;
        if(request.getParameter("page") != null)
            page = Integer.parseInt(request.getParameter("page"));
        
        int noofpages = (int) Math.ceil(totalrecord * 1.0 / Genel.ROWPERPAGE);
        
        List<UretimPlan> uretimplan = uretimDao.uretimPlanListeGetir(UretimDurum.TAMAMLANMIS, page, filter0, BaseDao.V_URETIMPLAN_TUM_PLAN);
        
        request.setAttribute("hatasebep", hatasebep);
        request.setAttribute("makina", makina);
        request.setAttribute("calisan", calisan);
        request.setAttribute("firma", firma);
        request.setAttribute("mamul", mamul);
        request.setAttribute("uretimplan", uretimplan);
        request.setAttribute("totalrecord", totalrecord);
        request.setAttribute("currentpage", page);
        request.setAttribute("noofpages", noofpages);
        request.setAttribute("tarih", tarih);
        request.setAttribute("makinaid", makinaid);
        request.setAttribute("message", message);
        request.setAttribute("excelsql", excelsql);        
        request.getRequestDispatcher("uretimplan.jsp").forward(request, response);        
    }
}