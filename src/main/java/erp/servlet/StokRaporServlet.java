package erp.servlet;

import erp.dao.BaseDao;
import erp.dao.BilesenDao;
import erp.dao.MamulDao;
import erp.dao.StokDao;
import erp.entity.Bilesen;
import erp.entity.Genel;
import erp.entity.Mamul;
import erp.entity.StokRapor;
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
import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/stokrapor")
public class StokRaporServlet extends HttpServlet implements Serializable {
    private static final Logger logger = LoggerFactory.getLogger(StokRaporServlet.class);

    @Inject
    MamulDao mamulDao;
    @Inject
    BilesenDao bilesenDao;
    @Inject
    StokDao stokDao;
    @Inject
    DAOFunctions daoFunctions;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html; charset=UTF-8");

        String raporgetirid = request.getParameter("raporgetirid");
        String bilesenid = request.getParameter("bilesenid");
        String bilesentip = request.getParameter("bilesentipid");
        String tarih = request.getParameter("tarih");
        String page = request.getParameter("page");
        String excelsql = "";
        String tablename = BaseDao.V_STOKDETAY_MAMUL;

        int totalrecord = 0;
        int noofpages = 0;
        int page0 = 1;

        List<Mamul> mamul = mamulDao.mamulListeGetir(null, 0, null);
        List<Bilesen> bilesen = bilesenDao.bilesenListeGetirTum(BilesenTip.HAMMADDEYARIMAMUL, 0, null, null);

        List<StokRapor> stokrapor = new ArrayList<StokRapor>();
        List<StokRapor> stokdetay = new ArrayList<StokRapor>();
        if (raporgetirid != null || page != null) {
            String filter;
            String filter0 = " AND bilesentipid IN (3) ";
            String filter1 = "";
            LocalDate datetime = LocalDate.parse(tarih, DateTimeFormatter.ofPattern("dd-MM-yyyy")).plusDays(1);
            if (bilesentip != null) {
                if (Integer.parseInt(bilesentip) == BilesenTip.HAMMADDE.value()) {
                    tablename = BaseDao.V_STOKDETAY_BILESEN;
                    filter0 = " AND bilesentipid IN (1) ";
                } else if (Integer.parseInt(bilesentip) == BilesenTip.YARIMAMUL.value()) {
                    tablename = BaseDao.V_STOKDETAY_BILESEN;
                    filter0 = " AND bilesentipid IN (2) ";
                } else {
                    tablename = BaseDao.V_STOKDETAY_MAMUL;
                    filter0 = " AND bilesentipid IN (3) ";
                }
                filter1 += " AND tarih <= '" + datetime.toString() + "' ";
            }
            filter = " WHERE 1=1 " + filter0 + filter1;

            // PAGING
            if (request.getParameter("page") != null)
                page0 = Integer.parseInt(request.getParameter("page"));
            // PAGING

            /* Eger bir urunde detay isteniyorsa*/
            if (Util.isNotEmptyOrNull(bilesenid)) {
                if(Integer.parseInt(bilesentip) == BilesenTip.MAMUL.value())
                    tablename = BaseDao.V_STOKDETAY_MAMUL_ID;
                else
                    tablename = BaseDao.V_STOKDETAY_BILESEN_ID;
                totalrecord = daoFunctions.recordAgg(tablename, "COUNT", "*", " WHERE bilesenid = " + bilesenid);
                noofpages = (int) Math.ceil(totalrecord * 1.0 / Genel.ROWPERPAGE);
                stokdetay = stokDao.stokBilesenDetayRapor(tablename, filter + "AND bilesenid = " + bilesenid, page0);
                excelsql = filter + " AND bilesenid = " + bilesenid;
            }
            else{
                totalrecord = daoFunctions.recordAgg(tablename, "COUNT", "*", filter);
                noofpages = (int) Math.ceil(totalrecord * 1.0 / Genel.ROWPERPAGE);
                stokrapor = stokDao.stokBilesenRapor(tablename, filter, page0);
                excelsql = filter;
            }
        }
        request.setAttribute("mamul", mamul);
        request.setAttribute("bilesen", bilesen);
        request.setAttribute("stokrapor", stokrapor);
        request.setAttribute("stokdetay", stokdetay);
        request.setAttribute("bilesentip", bilesentip);
        request.setAttribute("bilesenid", bilesenid);
        request.setAttribute("tarih", tarih);
        request.setAttribute("totalrecord", totalrecord);
        request.setAttribute("currentpage", page0);
        request.setAttribute("noofpages", noofpages);
        request.setAttribute("excelsql", excelsql);
        request.setAttribute("tablename", tablename);
        request.getRequestDispatcher("stokrapor.jsp").forward(request, response);
    }
}
