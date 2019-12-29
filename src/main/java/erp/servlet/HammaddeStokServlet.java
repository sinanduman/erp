package erp.servlet;

import erp.dao.BaseDao;
import erp.dao.BilesenDao;
import erp.dao.FirmaDao;
import erp.dao.StokDao;
import erp.entity.*;
import erp.model.BilesenTip;
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
import java.io.PrintWriter;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/hammaddestok")
public class HammaddeStokServlet extends HttpServlet implements Serializable {
    private static final Logger logger = LoggerFactory.getLogger(HammaddeStokServlet.class);

    @Inject
    BilesenDao bilesenDao;
    @Inject
    FirmaDao firmaDao;
    @Inject
    StokDao stokDao;
    @Inject
    DAOFunctions daoFunctions;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        List<Bilesen> hammadde = bilesenDao.bilesenListeGetirTum(null, 0, null, null);
        List<Firma> firma = firmaDao.firmaListeGetirTum(0);
        List<Stok> stok = new ArrayList<Stok>();

        String stoklisteid  = request.getParameter("stoklisteid");
        String bilesenid    = request.getParameter("bilesenid");
        
        int totalrecord = 0;
        int page        = 1;
        int noofpages   = 0;
        int sumagg      = 0;
        
        if(stoklisteid != null && Integer.parseInt(stoklisteid) == 1) {
            // PAGING
            totalrecord = daoFunctions.recordCount(BaseDao.V_STOKBILESEN," where bilesenid= " + Integer.parseInt(bilesenid) );
            sumagg      = daoFunctions.recordAgg(BaseDao.V_STOKMIKTAR, "sum", "miktar", " where bilesenid= " + Integer.parseInt(bilesenid));
            if(request.getParameter("page") != null)
                page    = Integer.parseInt(request.getParameter("page"));        
            noofpages   = (int) Math.ceil(totalrecord * 1.0 / Genel.ROWPERPAGE);
            // PAGING
            stok        = stokDao.stokListeGetirTum(BilesenTip.HAMMADDEYARIMAMUL, Integer.parseInt(bilesenid), page, null);
        }
        
        request.setAttribute("hammadde", hammadde);
        request.setAttribute("firma", firma);
        request.setAttribute("stok", stok);
        request.setAttribute("bilesenid", bilesenid);
        request.setAttribute("totalrecord", totalrecord);
        request.setAttribute("currentpage", page);
        request.setAttribute("noofpages", noofpages);
        request.setAttribute("sumagg", sumagg);
        request.getRequestDispatcher("hammaddestok.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html; charset=UTF-8");

        String bilesenid    = request.getParameter("bilesenid");
        String bilesenekleid= request.getParameter("bilesenekleid");        
        String message      = null;
        Boolean ajaxInAction= false;
        
        if(request.getParameter("islemid")!=null) {
            Integer islemid     = Integer.parseInt(request.getParameter("islemid"));
            String tarih        = request.getParameter("tarih");
            String gkrno        = request.getParameter("gkrno");
            String miktar       = request.getParameter("miktar");
            String iade         = request.getParameter("iade");
            String bilesentipid = request.getParameter("bilesentipid");
            /* with ajax */
            if (islemid == 6) {
                ajaxInAction    = true;
                String result   = stokDao.stokKontrol(tarih, gkrno, miktar, iade, bilesenid, bilesentipid);
                PrintWriter out = response.getWriter();
                out.print(result);
            }            
            logger.info("tarih: " + tarih + ", gkrno: " + gkrno + ", miktar: " + miktar + ", iade: " + iade);
        }
        
        if (!ajaxInAction) {
            List<Stok> stok;

            int totalrecord;
            int page = 1;
            int noofpages;
            int sumagg;
            
            if(bilesenekleid.equals("1")) {
                String result;
                String iade= request.getParameter("iade");
                if(iade==null) {
                    result = stokDao.stokEkle(
                                    request.getParameter("bilesenid"),
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
                                    request.getParameter("bilesenid"),
                                    request.getParameter("miktar"),
                                    request.getParameter("irsaliyeno"),
                                    request.getParameter("lot"),
                                    request.getParameter("lot"), /* gkrno*/
                                    request.getParameter("tarih"),
                                    request.getParameter("not")
                                    );
                }
                message = result.equals("0")?"Hata oluştu!..":"";
            }
            
            // PAGING
            totalrecord = daoFunctions.recordCount(BaseDao.V_STOKBILESEN," where bilesenid= " +  Integer.parseInt(bilesenid));
            sumagg      = daoFunctions.recordAgg(BaseDao.V_STOKMIKTAR, "sum", "miktar", " where bilesenid= " +  Integer.parseInt(bilesenid));
            if(request.getParameter("page") != null)
                page = Integer.parseInt(request.getParameter("page"));
            noofpages = (int) Math.ceil(totalrecord * 1.0 / Genel.ROWPERPAGE);
            // PAGING
            stok = stokDao.stokListeGetirTum(BilesenTip.HAMMADDEYARIMAMUL, Integer.parseInt(bilesenid), page, null);
            
            List<Bilesen> hammadde = bilesenDao.bilesenListeGetirTum(null, 0, null, null);
            List<Firma> firma = firmaDao.firmaListeGetirTum(0);
            
            request.setAttribute("hammadde", hammadde);
            request.setAttribute("firma", firma);
            request.setAttribute("stok", stok);
            request.setAttribute("bilesenid", bilesenid);
            request.setAttribute("totalrecord", totalrecord);
            request.setAttribute("currentpage", page);
            request.setAttribute("noofpages", noofpages);
            request.setAttribute("sumagg", sumagg);
            request.setAttribute("message", message);
            request.getRequestDispatcher("hammaddestok.jsp").forward(request, response);
        }
    }
}
