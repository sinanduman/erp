package erp.irfan;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import erp.dao.*;
import erp.irfan.entity.DurusSebep;
import erp.irfan.entity.HataSebep;
import erp.irfan.entity.Calisan;
import erp.irfan.entity.Firma;
import erp.irfan.entity.Genel;
import erp.irfan.entity.Makina;
import erp.irfan.entity.Mamul;
import erp.irfan.entity.UretimDurum;
import erp.irfan.entity.UretimPlan;
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
            Integer islemid     = (request.getParameter("islemid")==null)?-1:Integer.valueOf(request.getParameter("islemid"));
            String uretimplanid = request.getParameter("uretimplanid");        
            String mamulid      = request.getParameter("mamulid");        
            String uret_miktar  = request.getParameter("uretilenmiktar");
            String hata_miktar  = (request.getParameter("hatalimiktar")==null || request.getParameter("hatalimiktar")=="") ? "0" : request.getParameter("hatalimiktar");
            
            if(islemid==3) {  /* Stok Yeterli mi ? */
                ajaxInAction    = true;
                String result   = stokDao.stokYeterli(
                                Integer.valueOf(uretimplanid), 
                                Integer.valueOf(mamulid),  
                                Integer.valueOf(uret_miktar) + Integer.valueOf(hata_miktar)
                                );
                PrintWriter out = response.getWriter();
                out.print(result);
            }
            else if(islemid==2) {  /* Silme */
                ajaxInAction    = true;
                String result   = uretimDao.uretimPlanSil(Integer.valueOf(uretimplanid));
                PrintWriter out = response.getWriter();
                out.print(result);
            } else if(islemid==1) {  /* Onaylama */
                ajaxInAction    = true;
                String result   = uretimDao.uretimPlanOk(Integer.valueOf(uretimplanid));
                PrintWriter out = response.getWriter();
                out.print(result);
            } else if(islemid==0){ /* Takip ekleme */
                logger.info("islemid: " +request.getParameter("islemid"));
                String baszaman = request.getParameter("bassaat").toString() +":"+ request.getParameter("basdakika").toString()+":"+"00";
                String bitzaman = request.getParameter("bitsaat").toString() +":"+ request.getParameter("bitdakika").toString()+":"+"00";
                String hataliad = (request.getParameter("hataliadet").trim() == "")?"0":request.getParameter("hataliadet");
                int result = uretimDao.uretimTakipEkleGuncelle(
                                new String(request.getParameter("mamulid").getBytes("UTF-8")),
                                new String(request.getParameter("uretimadet").getBytes("UTF-8")),
                                hataliad,
                                new String(request.getParameter("makinaid").getBytes("UTF-8")),
                                new String(request.getParameter("calisanid").getBytes("UTF-8")),
                                new String(request.getParameter("tarih").getBytes("UTF-8")),                        
                                baszaman,
                                bitzaman,
                                new String(request.getParameter("hatasebepid").getBytes("UTF-8")),
                                new String(request.getParameter("durussebepid").getBytes("UTF-8")),
                                new String(request.getParameter("duruszaman").getBytes("UTF-8")),
                                Integer.valueOf(uretimplanid),
                                Integer.valueOf(request.getParameter("mamulizleno"))
                                );
                message = (result==-1) ? "Hata oluştu" : "";
            }
        }
        if(!ajaxInAction) {
            List<HataSebep> hatasebep = new ArrayList<HataSebep>();
            hatasebep = hataDao.hataSebepListeGetir();
            
            List<DurusSebep> durussebep = new ArrayList<DurusSebep>();
            durussebep = hataDao.durusSebepListeGetir();
            
            List<Makina> makina = new ArrayList<Makina>();
            makina = makinaDao.makinaListeGetirTum(0);
            
            List<Calisan> calisan = new ArrayList<Calisan>();
            calisan = calisanDao.calisanListeGetirTum(0, 1);
            
            List<Firma> firma = new ArrayList<Firma>();
            firma = firmaDao.firmaListeGetirTum(0);
            
            List<Mamul> mamul = new ArrayList<Mamul>();
            mamul = mamulDao.mamulListeGetir(null, 0);
            
            int totalrecord = daoFunctions.recordAgg("uretimplantum_takip","count","*"," where tamamlandi=0 ");
            int page = 1;
            if(request.getParameter("page") != null)
                page = Integer.parseInt(request.getParameter("page"));
            
            int noofpages = (int) Math.ceil(totalrecord * 1.0 / Genel.ROWPERPAGE);
            
            List<UretimPlan> uretimplan = new ArrayList<UretimPlan>();
            uretimplan  = uretimDao.uretimPlanListeGetir(UretimDurum.BEKLEYEN, page, "","takip");
            
            request.setAttribute("hatasebep", hatasebep);
            request.setAttribute("durussebep", durussebep);
            request.setAttribute("makina", makina);
            request.setAttribute("calisan", calisan);
            request.setAttribute("firma", firma);
            request.setAttribute("mamul", mamul);
            request.setAttribute("uretimplan", uretimplan);
            request.setAttribute("totalrecord", totalrecord);
            request.setAttribute("currentpage", page);
            request.setAttribute("noofpages", noofpages);
            request.setAttribute("message", message);
            request.getRequestDispatcher("uretimtakip.jsp").forward(request, response);
        }        
    }
}
