package erp.servlet;

import erp.dao.*;
import erp.entity.Bakim;
import erp.entity.Calisan;
import erp.entity.Genel;
import erp.entity.Mamul;
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
import java.util.List;
import java.util.Map;

@WebServlet("/bakimrapor")
public class BakimRaporServlet extends HttpServlet implements Serializable {
    private static final Logger logger = LoggerFactory.getLogger(BakimRaporServlet.class);

    @Inject
    CalisanDao calisanDao;
    @Inject
    MamulDao mamulDao;
    @Inject
    BakimDao bakimDao;
    @Inject
    DAOFunctions daoFunctions;
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
    
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html; charset=UTF-8");

        String raporgetirid = request.getParameter("raporgetirid");
        String mamulid      = request.getParameter("mamulid");
        String calisanid    = request.getParameter("calisanid");
        String bas_tarih    = request.getParameter("bas_tarih");
        String bit_tarih    = request.getParameter("bit_tarih");
        String page         = request.getParameter("page");
        String excelsql     = "";

        int totalrecord = 0;
        int noofpages   = 0;
        int page0       = 1;

        List<Calisan> calisan = calisanDao.calisanListeGetirTum(0, null);
        Map<Integer, Calisan> calisanmap = Util.calisanListToMap(calisan);

        List<Mamul> mamul = mamulDao.mamulListeGetir(null, 0, null);
        Map<Integer, Mamul> mamulmap = Util.mamulListToMap(mamul);

        String filter0  = "";
        String filter1  = "";
        String filter2  = "";
        String filter3  = "";
        String andYes   = " AND ";
        String tableName = (mamulid == null || mamulid.isEmpty()) ? BaseDao.V_BAKIM_MAMUL : BaseDao.BAKIM;

        if(mamulid!=null && !mamulid.equals("") ) {
            filter1     = andYes + " mamulid = " + Integer.parseInt(mamulid);
        }
        if(calisanid!=null && !calisanid.equals("") ) {
            filter2     = andYes + " calisanid = " + Integer.parseInt(calisanid);
        }
        if(bas_tarih!=null && bit_tarih!=null && !bas_tarih.equals("") && !bit_tarih.equals("") ) {
            filter3     = andYes + " ( bakimtarihi BETWEEN '" + Util.date_tr_to_eng(bas_tarih) + "' AND '" + Util.date_tr_to_eng(bit_tarih) +"')";
        }
        filter0 = filter1 + filter2 + filter3;

        // PAGING
        totalrecord = daoFunctions.recordAgg(tableName, "COUNT", "*", " WHERE 1=1" + filter0 );
        if(request.getParameter("page") != null)
            page0   = Integer.parseInt(page);
        noofpages   = (int) Math.ceil(totalrecord * 1.0 / Genel.ROWPERPAGE);
        // PAGING

        List<Bakim> bakimrapor = bakimDao.bakimRapor(tableName, page0, filter0, mamulid);

        logger.info("noofpages: "+ noofpages + ", totalrecord: "+ totalrecord + ", " + tableName + " WHERE 1=1" + filter0 );
        excelsql = filter0;

        request.setAttribute("calisan", calisan);
        request.setAttribute("calisanmap", calisanmap);
        request.setAttribute("mamul", mamul);
        request.setAttribute("mamulmap", mamulmap);
        request.setAttribute("bakimrapor", bakimrapor);
        request.setAttribute("calisanid", calisanid);
        request.setAttribute("mamulid", mamulid);
        request.setAttribute("bas_tarih", bas_tarih);
        request.setAttribute("bit_tarih", bit_tarih);
        request.setAttribute("totalrecord", totalrecord);
        request.setAttribute("currentpage", page0);
        request.setAttribute("noofpages", noofpages);
        request.setAttribute("excelsql", excelsql);
        request.getRequestDispatcher("bakimrapor.jsp").forward(request, response);
    }
}
