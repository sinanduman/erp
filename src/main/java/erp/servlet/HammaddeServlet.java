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
import erp.dao.BilesenDao;
import erp.dao.FirmaDao;
import erp.dao.HammaddeDao;
import erp.entity.Bilesen;
import erp.entity.Firma;
import erp.entity.Genel;
import erp.util.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@WebServlet("/hammadde")
public class HammaddeServlet extends HttpServlet implements Serializable {
    private static long token = 1L;
    private static final Logger logger = LoggerFactory.getLogger(HammaddeServlet.class);

    @Inject
    FirmaDao firmaDao;
    @Inject
    BilesenDao bilesenDao;
    @Inject
    HammaddeDao hammaddeDao;
    @Inject
    DAOFunctions daoFunctions;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String message      = "";
        String bilesenid    = request.getParameter("bilesenidara");
        String stoklisteid  = request.getParameter("stoklisteid");
        List<Firma> firma   = new ArrayList<Firma>();
        firma = firmaDao.firmaListeGetirTum(0);

        int totalrecord = daoFunctions.recordCount(BaseDao.BILESEN," ");
        int page = 1;
        if(request.getParameter("page") != null)
            page = Integer.parseInt(request.getParameter("page"));

        int noofpages = (int) Math.ceil(totalrecord * 1.0 / Genel.ROWPERPAGE);

        List<Bilesen> hammaddetum   = new ArrayList<Bilesen>();
        List<Bilesen> hammadde      = new ArrayList<Bilesen>();
        hammaddetum = bilesenDao.bilesenListeGetirTum(null, 0, null, null);
        hammadde = bilesenDao.bilesenListeGetirTum(null, page, (Util.isNotEmptyOrNull(bilesenid))? Integer.parseInt(bilesenid): null, null);

        token  = Util.getToken();
        request.setAttribute("token", token);
        request.setAttribute("firma", firma);
        request.setAttribute("hammadde", hammadde);
        request.setAttribute("hammaddetum", hammaddetum);
        request.setAttribute("message", message);
        request.setAttribute("totalrecord", totalrecord);
        request.setAttribute("currentpage", page);
        request.setAttribute("noofpages", noofpages);        
        request.setAttribute("bilesenid", bilesenid);
        request.setAttribute("stoklisteid", stoklisteid);        
        request.getRequestDispatcher("hammadde.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        response.setContentType("text/html; charset=UTF-8");

        String islemid      = request.getParameter("islemid");
        String bilesenid    = null;
        String stoklisteid  = null;
        String message      = "-1";
        if(islemid != null && islemid != "" ) {
            Integer result = 0;
            switch (Integer.parseInt(islemid)) {
                case 1:
                    message = bilesenDao.bilesenGuncelle(
                                    new String(request.getParameter("bilesenid").getBytes("UTF-8")),
                                    new String(request.getParameter("birimid").getBytes("UTF-8")),
                                    new String(request.getParameter("firmaid").getBytes("UTF-8")),
                                    new String(request.getParameter("bilesenkod").getBytes("UTF-8")),
                                    new String(request.getParameter("bilesenad").getBytes("UTF-8")),
                                    new String(request.getParameter("durum").getBytes("UTF-8")),
                                    new String(request.getParameter("fiyatid").getBytes("UTF-8")),
                                    new String(request.getParameter("fiyat").getBytes("UTF-8")),
                                    new String(request.getParameter("kritikesik").getBytes("UTF-8"))
                                    );
                    break;
                case 3:
                    message = bilesenDao.bilesenSil(new String(request.getParameter("bilesenid").getBytes("UTF-8")));
                    break;
            }
            PrintWriter out = response.getWriter();
            out.print(result);
        }
        else {
            /* Prevent Resubmit */
            if(Util.isNotEmptyOrNull(request.getParameter("stoklisteid")) && 
                            Integer.parseInt(request.getParameter("stoklisteid"))==1 ) {
                bilesenid   = request.getParameter("bilesenidara");
                stoklisteid = request.getParameter("stoklisteid");
            }
            else if(Long.valueOf(request.getParameter("token"))==token) {
                message = hammaddeDao.hammaddeEkle(
                        new String(request.getParameter("hamkod").getBytes("UTF-8")),
                        new String(request.getParameter("hamad").getBytes("UTF-8")),
                        new String(request.getParameter("hambirim").getBytes("UTF-8")),
                        new String(request.getParameter("hamfirma").getBytes("UTF-8")),
                        new String(request.getParameter("hamtip").getBytes("UTF-8")),
                        new String(request.getParameter("hamfiyatid").getBytes("UTF-8")),
                        new String(request.getParameter("hamfiyat").getBytes("UTF-8")),
                        new String(request.getParameter("hamkritikesik").getBytes("UTF-8"))
                );
            }
            else {
                message="-1";
            }
            /* PAGING */
            String filter = ((bilesenid==null) ? "" : " where id=" + bilesenid);
            int totalrecord = daoFunctions.recordAgg(BaseDao.BILESEN, "COUNT", "*", filter);
            int page = 1;
            if(request.getParameter("page") != null)
                page = Integer.parseInt(request.getParameter("page"));            
            int noofpages = (int) Math.ceil(totalrecord * 1.0 / Genel.ROWPERPAGE);
            /* PAGING */

            List<Bilesen> hammaddetum   = new ArrayList<Bilesen>();
            List<Bilesen> hammadde      = new ArrayList<Bilesen>();
            hammaddetum = bilesenDao.bilesenListeGetirTum(null, 0, null, null);
            hammadde = bilesenDao.bilesenListeGetirTum(null, page, (Util.isNotEmptyOrNull(bilesenid))? Integer.parseInt(bilesenid): null, null);

            List<Firma> firma = new ArrayList<Firma>();
            firma = firmaDao.firmaListeGetirTum(0);

            token  = Util.getToken();
            request.setAttribute("token", token);
            request.setAttribute("firma", firma);
            request.setAttribute("hammadde", hammadde);
            request.setAttribute("hammaddetum", hammaddetum);
            request.setAttribute("totalrecord", totalrecord);
            request.setAttribute("currentpage", page);
            request.setAttribute("noofpages", noofpages);
            request.setAttribute("message", message);
            request.setAttribute("bilesenid", bilesenid);
            request.setAttribute("stoklisteid", stoklisteid);
            request.getRequestDispatcher("hammadde.jsp").forward(request, response);
        }
    }
}