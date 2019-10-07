package erp.servlet;

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

import erp.dao.BaseDao;
import erp.dao.FirmaDao;
import erp.dao.MamulDao;
import erp.dao.StokDao;
import erp.model.BilesenTip;
import erp.entity.Firma;
import erp.entity.Genel;
import erp.entity.Mamul;
import erp.entity.Stok;
import erp.util.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@WebServlet("/mamulstok")
public class MamulStokServlet extends HttpServlet implements Serializable {
    private static final Logger logger = LoggerFactory.getLogger(MamulStokServlet.class);

    @Inject
    MamulDao mamulDao;
    @Inject
    FirmaDao firmaDao;
    @Inject
    StokDao stokDao;
    @Inject
    DAOFunctions daoFunctions;
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        List<Mamul> mamul = mamulDao.mamulListeStokGetir(null, 0, 1);
        List<Firma> firma = firmaDao.firmaListeGetirTum(0);
        
        List<Stok> stok = new ArrayList<Stok>();
        String stoklisteid = request.getParameter("stoklisteid");
        String mamulid = request.getParameter("mamulid");
        
        int totalrecord = 0;
        int page = 1;
        int noofpages = 0;
        int sumagg = 0;
        
        if (stoklisteid != null && Integer.parseInt(stoklisteid) == 1) {
            
            // PAGING
            // totalrecord = DAOFunctions.recordCount("stok"," where id= " +
            // stoklisteid);
            totalrecord = daoFunctions.recordAgg(BaseDao.STOK, "count", "*", " where bilesenid= " + Integer.valueOf(mamulid));
            sumagg = daoFunctions.recordAgg(BaseDao.V_STOKMIKTAR, "sum", "miktar"," where bilesenid= " + Integer.valueOf(mamulid));
            if (request.getParameter("page") != null)
                page = Integer.parseInt(request.getParameter("page"));
            noofpages = (int) Math.ceil(totalrecord * 1.0 / Genel.ROWPERPAGE);
            // PAGING
            stok = stokDao.stokListeGetirTum(BilesenTip.MAMUL, Integer.valueOf(mamulid), page, null);
            
            logger.info(String.valueOf(totalrecord));
            logger.info(String.valueOf(noofpages));
        }
        
        request.setAttribute("mamul", mamul);
        request.setAttribute("firma", firma);
        request.setAttribute("stok", stok);
        request.setAttribute("totalrecord", totalrecord);
        request.setAttribute("sumagg", sumagg);
        request.setAttribute("currentpage", page);
        request.setAttribute("noofpages", noofpages);
        request.setAttribute("mamulid", mamulid);
        request.setAttribute("excelsql", mamulid);
        
        request.getRequestDispatcher("mamulstok.jsp").forward(request, response);
    }
    
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        response.setContentType("text/html; charset=UTF-8");

        // Integer stoklisteid =
        // Integer.valueOf(request.getParameter("stoklisteid"));
        String mamulid      = request.getParameter("mamulid");
        String mamulekleid  = request.getParameter("mamulekleid");
        String message      = null;
        Boolean ajaxInAction= false;
        
        
        if(request.getParameter("islemid")!=null) {
            Integer islemid     = Integer.valueOf(request.getParameter("islemid"));
            String tarih        = request.getParameter("tarih");
            String gkrno        = request.getParameter("gkrno");
            String miktar       = request.getParameter("miktar");
            String iade         = request.getParameter("iade");
            String bilesentipid = request.getParameter("bilesentipid");
            /* with ajax */
            if (islemid == 6) {
                ajaxInAction    = true;
                String result   = stokDao.stokKontrol(tarih, gkrno, miktar, iade, mamulid, bilesentipid);
                PrintWriter out = response.getWriter();
                out.print(result);
            }            
            logger.info("tarih: " + tarih + ", gkrno: " + gkrno + ", miktar: " + miktar + ", iade: " + iade );
        }
        
        if (!ajaxInAction) {
            
            List<Stok> stok     = new ArrayList<Stok>();
            
            int totalrecord = 0;
            int page        = 1;
            int noofpages   = 0;
            int sumagg      = 0;
            
            if (mamulekleid.equals("1")) {
                String result = "";
                String iade = request.getParameter("iade");
                if (iade == null) {
                    result = stokDao.stokEkle(
                                    request.getParameter("mamulid"), 
                                    request.getParameter("miktar"),
                                    request.getParameter("irsaliyeno"), 
                                    request.getParameter("lot"), 
                                    Util.gkrnoNumberCheck(request.getParameter("lot")), /* gkrno */
                                    request.getParameter("tarih"),
                                    request.getParameter("not")
                                    );
                }
                else {
                    result = stokDao.stokDus(
                                    request.getParameter("mamulid"), 
                                    request.getParameter("miktar"),
                                    request.getParameter("irsaliyeno"), 
                                    request.getParameter("lot"),
                                    request.getParameter("lot"), /* gkrno */
                                    request.getParameter("tarih"), 
                                    request.getParameter("not")
                                    );
                }
                message = result.equals("0") ? "Hata oluştu!.." : "";
            }

            // if(stoklisteid!=null && stoklisteid==1) {
            
            totalrecord = daoFunctions.recordAgg(BaseDao.STOK, "count", "*", " where bilesenid= " + Integer.valueOf(mamulid));
            sumagg      = daoFunctions.recordAgg(BaseDao.V_STOKMIKTAR, "sum", "miktar", " where bilesenid= " + Integer.valueOf(mamulid));
            if (request.getParameter("page") != null)
                page = Integer.parseInt(request.getParameter("page"));
            noofpages   = (int) Math.ceil(totalrecord * 1.0 / Genel.ROWPERPAGE);
            stok        = stokDao.stokListeGetirTum(BilesenTip.MAMUL, Integer.valueOf(mamulid), page, null);
            // }
            
            List<Mamul> mamul = new ArrayList<Mamul>();
            mamul = mamulDao.mamulListeStokGetir(null, 0, 1);
            
            List<Firma> firma = new ArrayList<Firma>();
            firma = firmaDao.firmaListeGetirTum(0);
            
            request.setAttribute("mamul", mamul);
            request.setAttribute("firma", firma);
            request.setAttribute("stok", stok);
            request.setAttribute("message", message);
            request.setAttribute("totalrecord", totalrecord);
            request.setAttribute("sumagg", sumagg);
            request.setAttribute("currentpage", page);
            request.setAttribute("noofpages", noofpages);
            request.setAttribute("mamulid", mamulid);
            request.setAttribute("excelsql", mamulid);
            
            request.getRequestDispatcher("mamulstok.jsp").forward(request, response);
        }
    }
}
