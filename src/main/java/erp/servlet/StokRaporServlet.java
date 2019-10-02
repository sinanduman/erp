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

import erp.dao.BaseDao;
import erp.dao.BilesenDao;
import erp.dao.MamulDao;
import erp.dao.StokDao;
import erp.entity.BilesenTip;
import erp.util.Util;
import org.joda.time.DateTime;

import erp.entity.Bilesen;
import erp.entity.Genel;
import erp.entity.Mamul;
import erp.entity.StokRapor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
        String tablename = BaseDao.MAMUL;

        int totalrecord = 0;
        int noofpages = 0;
        int page0 = 1;

        List<Mamul> mamul = mamulDao.mamulListeGetir(null, 0);
        List<Bilesen> bilesen = bilesenDao.bilesenListeGetirTum(BilesenTip.HAMMADDEYARIMAMUL, 0, null);

        List<StokRapor> stokrapor = new ArrayList<StokRapor>();
        List<StokRapor> stokdetay = new ArrayList<StokRapor>();
        BilesenTip bilesentipenum = BilesenTip.MAMUL;
        if (raporgetirid != null || page != null) {
            String filter = "";
            String filter0 = " AND bilesentipid IN (3) ";
            String filter1 = "";
            String filter2 = "";
            String filterIn = "";
            DateTime datetime = new DateTime(Util.date_tr_to_eng(tarih)).plusDays(1);
            if (bilesentip != null) {
                if (Integer.parseInt(bilesentip) == 1) {
                    tablename = BaseDao.BILESEN;
                    filter0 = " AND bilesentipid IN (1) ";
                    bilesentipenum = BilesenTip.HAMMADDE;
                } else if (Integer.parseInt(bilesentip) == 2) {
                    tablename = BaseDao.BILESEN;
                    filter0 = " AND bilesentipid IN (2) ";
                    bilesentipenum = BilesenTip.YARIMAMUL;
                } else {
                    tablename = BaseDao.MAMUL;
                    filter0 = " AND bilesentipid IN (3) ";
                    bilesentipenum = BilesenTip.MAMUL;
                }
                filter2 += " AND (case when islemyonu=0 then giristarihi else cikistarihi end)<'" + datetime.toString() + "' ";
            }
            if (Util.isNotEmptyOrNull(bilesenid)) {
                filter1 = " AND " + tablename + ".id = " + Integer.valueOf(bilesenid);
            }
            filter = filter0 + filter1 + filter2;
            filterIn = " AND id in (select bilesenid from " + BaseDao.STOK + " where (case when islemyonu=0 then giristarihi else cikistarihi end) <'" + datetime.toString() + "') ";
            // PAGING
            System.out.println("islem: " +tablename + "count" + "*" +filter0 + filter1 + filterIn);
            totalrecord = daoFunctions.recordAgg(tablename, "COUNT", "*", " WHERE 1=1 " + filter0 + filter1 + filterIn);
            if (request.getParameter("page") != null)
                page0 = Integer.parseInt(request.getParameter("page"));
            noofpages = (int) Math.ceil(totalrecord * 1.0 / Genel.ROWPERPAGE);
            // PAGING
            stokrapor = stokDao.stokBilesenRapor(tablename, filter, page0);

            /* Eger bir urunde detay isteniyorsa*/
            if (Util.isNotEmptyOrNull(bilesenid)) {
                stokdetay = stokDao.stokBilesenDetayRapor(tablename, filter, page0, Integer.valueOf(bilesenid));
                totalrecord = daoFunctions.recordAgg(BaseDao.STOK, "COUNT", "*", " WHERE 1=1 " + "AND bilesenid = " + bilesenid);
                noofpages = (int) Math.ceil(totalrecord * 1.0 / Genel.ROWPERPAGE);
            }

            excelsql = filter;

            logger.info("noofpages: " + noofpages + ", totalrecord: " + totalrecord + ", " + tablename + " WHERE 1=1 " + filter);
            logger.info("bilesentip: " + bilesentipenum.id());
            logger.info("excelsql: " + excelsql);

        }
        request.setAttribute("mamul", mamul);
        request.setAttribute("bilesen", bilesen);
        request.setAttribute("stokrapor", stokrapor);
        request.setAttribute("stokdetay", stokdetay);
        request.setAttribute("bilesentip", bilesentipenum.id());
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
