package erp.servlet;

import java.io.IOException;
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
import erp.entity.Calisan;
import erp.entity.Firma;
import erp.entity.Genel;
import erp.entity.Makina;
import erp.entity.Mamul;
import erp.entity.UretimPlan;
import erp.util.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@WebServlet("/uretimrapor")
public class UretimRaporServlet extends HttpServlet implements Serializable {
    private static final Logger logger = LoggerFactory.getLogger(UretimRaporServlet.class);

    @Inject
    MakinaDao makinaDao;
    @Inject
    CalisanDao calisanDao;
    @Inject
    FirmaDao firmaDao;
    @Inject
    MamulDao mamulDao;
    @Inject
    UretimDao uretimDao;
    @Inject
    DAOFunctions daoFunctions;
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
    
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html; charset=UTF-8");

        String raporgetirid = request.getParameter("raporgetirid");
        String bilesenid    = request.getParameter("bilesenid");
        String makinaid     = request.getParameter("makinaid");
        String calisanid    = request.getParameter("calisanid");
        String firmaid      = request.getParameter("firmaid");
        String bas_tarih    = request.getParameter("bas_tarih");
        String bit_tarih    = request.getParameter("bit_tarih");
        String page         = request.getParameter("page");
        String excelsql     = "";

        List<Makina> makina = makinaDao.makinaListeGetirTum(0);
        List<Calisan> calisan = calisanDao.calisanListeGetirTum(0, null);
        List<Firma> firma = firmaDao.firmaListeGetirTum(0);

        int totalrecord = 0;
        int noofpages   = 0;
        int page0       = 1;
        
        List<Mamul> mamul = mamulDao.mamulListeGetir(null, 0);
        List<UretimPlan> stokrapor = new ArrayList<UretimPlan>();

        if(raporgetirid!=null || page != null ) {
            String tablename        = BaseDao.V_URETIMPLAN_TUM_PLAN;
            String filter0          = "";
            String filter1          = "";
            String filter2          = "";
            String filter3          = "";
            String filter4          = "";
            String filter5          = "";
            String andYes           = " AND ";
            
            if(bilesenid!=null && !bilesenid.equals("") ) {
                filter1     = andYes + " mamulid = " + Integer.valueOf(bilesenid);
            }
            if(makinaid!=null && !makinaid.equals("") ) {
                filter2     = andYes + " makinaid = " + Integer.valueOf(makinaid);
            }
            if(calisanid!=null && !calisanid.equals("") ) {
                filter3     = andYes + " calisanid = " + Integer.valueOf(calisanid);
            }
            if(firmaid!=null && !firmaid.equals("") ) {
                filter4     = andYes + " firmaid = " + Integer.valueOf(firmaid);
            }
            if(bas_tarih!=null && bit_tarih!=null && !bas_tarih.equals("") && !bit_tarih.equals("") ) {
                filter5     = andYes + " ( tarih BETWEEN '" + Util.date_tr_to_eng(bas_tarih) + "' AND '" + Util.date_tr_to_eng(bit_tarih) +"')";
            }
            filter0 = filter1 + filter2 + filter3 + filter4 + filter5;
            
            // PAGING
            totalrecord = daoFunctions.recordAgg(tablename, "COUNT", "*", " WHERE 1=1" + filter0 );
            if(request.getParameter("page") != null)
                page0   = Integer.parseInt(page);
            noofpages   = (int) Math.ceil(totalrecord * 1.0 / Genel.ROWPERPAGE);
            // PAGING
            
            stokrapor   = uretimDao.uretimBilesenRapor(tablename, page0, filter0);
            logger.info("noofpages: "+ noofpages + ", totalrecord: "+ totalrecord + ", " + tablename + " WHERE 1=1" + filter0 );
            excelsql = filter0;
        } 
        
        request.setAttribute("makina", makina);
        request.setAttribute("calisan", calisan);
        request.setAttribute("firma", firma);
        request.setAttribute("mamul", mamul);
        request.setAttribute("stokrapor", stokrapor);
        request.setAttribute("bilesenid", bilesenid);
        request.setAttribute("makinaid", makinaid);
        request.setAttribute("calisanid", calisanid);
        request.setAttribute("firmaid", firmaid);
        request.setAttribute("bas_tarih", bas_tarih);
        request.setAttribute("bit_tarih", bit_tarih);
        request.setAttribute("totalrecord", totalrecord);
        request.setAttribute("currentpage", page0);
        request.setAttribute("noofpages", noofpages);
        request.setAttribute("excelsql", excelsql);
        request.getRequestDispatcher("uretimrapor.jsp").forward(request, response);   
    }
}
