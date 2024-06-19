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
import erp.util.Fields;
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
import java.util.Map;

@WebServlet("/stokrapor")
public class StokRaporServlet extends HttpServlet implements Serializable {
    private static final Logger logger = LoggerFactory.getLogger(StokRaporServlet.class);

    @Inject
    private MamulDao mamulDao;
    @Inject
    private BilesenDao bilesenDao;
    @Inject
    private StokDao stokDao;
    @Inject
    private DAOFunctions daoFunctions;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html; charset=UTF-8");

        String raporgetirid = request.getParameter("raporgetirid");
        String bilesenid = request.getParameter(Fields.BILESENID);
        String bilesentip = request.getParameter(Fields.BILESENTIPID);
        String tarih = (request.getParameter(Fields.TARIH) == null) ? LocalDate.now().plusDays(1).format(DateTimeFormatter.ofPattern("dd-MM-yyyy")) : request.getParameter("tarih");
        String page = request.getParameter(Fields.PAGE);
        String azalan = request.getParameter("azalan");

        String excelsql = "";
        String tablename = BaseDao.V_STOKDETAY_MAMUL;

        int totalrecord = 0;
        int noofpages = 0;
        int page0 = 1;

        List<Mamul> mamul = mamulDao.mamulListeGetir(null, 0, null);
        List<Bilesen> bilesen = bilesenDao.bilesenListeGetirTum(BilesenTip.HAMMADDEYARIMAMUL, 0, null, null);
        Map<Integer, Bilesen> bilesenmap = Util.bilesenListToMap(bilesen);

        List<StokRapor> stokrapor = new ArrayList<>();
        List<StokRapor> stokdetay = new ArrayList<>();
        if (raporgetirid != null || page != null) {
            String filter;
            String filter0 = " AND bilesentipid IN (3) ";
            String filter1 = "";
            LocalDate datetime = (tarih == null) ? LocalDate.now().plusDays(1) : LocalDate.parse(tarih, DateTimeFormatter.ofPattern("dd-MM-yyyy")).plusDays(1);

            if (bilesentip != null) {
                tablename = getTableName(bilesentip, azalan, bilesenid);
                filter0 = getFilter0(bilesentip);
                filter1 += " AND tarih <= '" + datetime.toString() + "' ";
            }
            filter = " WHERE 1=1 " + filter0 + filter1;

            // PAGING
            if (request.getParameter(Fields.PAGE) != null)
                page0 = Integer.parseInt(request.getParameter(Fields.PAGE));
            // PAGING

            /* Eger bir urunde detay isteniyorsa*/
            if (Util.isNotEmptyOrNull(bilesenid)) {
                totalrecord = daoFunctions.recordAgg(tablename, Fields.COUNT, "*", "AND bilesenid=" + bilesenid);
                noofpages = (int) Math.ceil(totalrecord * 1.0 / Genel.ROWPERPAGE);
                stokdetay = stokDao.stokBilesenDetayRapor(tablename, filter + "AND bilesenid = " + bilesenid, page0);
                excelsql = filter + " AND bilesenid = " + bilesenid;
            } else {
                totalrecord = daoFunctions.recordAgg(tablename, Fields.COUNT, "*", filter0 + filter1);
                noofpages = (int) Math.ceil(totalrecord * 1.0 / Genel.ROWPERPAGE);
                stokrapor = stokDao.stokBilesenRapor(tablename, filter, page0);
                excelsql = filter;
            }
        }
        request.setAttribute(Fields.MAMUL, mamul);
        request.setAttribute(Fields.BILESEN, bilesen);
        request.setAttribute("stokrapor", stokrapor);
        request.setAttribute("stokdetay", stokdetay);
        request.setAttribute(Fields.BILESENTIP, bilesentip);
        request.setAttribute(Fields.BILESENID, bilesenid);
        request.setAttribute(Fields.TARIH, tarih);
        request.setAttribute(Fields.TOTALRECORD, totalrecord);
        request.setAttribute(Fields.CURRENTPAGE, page0);
        request.setAttribute(Fields.NOOFPAGES, noofpages);
        request.setAttribute(Fields.EXCELSQL, excelsql);
        request.setAttribute("tablename", tablename);
        request.setAttribute("azalan", azalan);
        request.setAttribute("bilesenmap", bilesenmap);
        request.getRequestDispatcher("stokrapor.jsp").forward(request, response);
    }

    public String getTableName(String bilesenTip, String azalan, String bilesenid) {
        String tablename;
        if (Integer.parseInt(bilesenTip) == BilesenTip.HAMMADDE.value()) {
            tablename = (azalan != null && azalan.equals("1")) ? BaseDao.V_STOKESIK_HAMMADDE : BaseDao.V_STOKDETAY_BILESEN;
            if (Util.isNotEmptyOrNull(bilesenid)) {
                tablename = BaseDao.V_STOKDETAY_BILESEN_ID;
            }
        } else if (Integer.parseInt(bilesenTip) == BilesenTip.YARIMAMUL.value()) {
            tablename = (azalan != null && azalan.equals("1")) ? BaseDao.V_STOKESIK_YARIMAMUL : BaseDao.V_STOKDETAY_BILESEN;
            if (Util.isNotEmptyOrNull(bilesenid)) {
                tablename = BaseDao.V_STOKDETAY_BILESEN_ID;
            }
        } else {
            tablename = (azalan != null && azalan.equals("1")) ? BaseDao.V_STOKESIK_MAMUL : BaseDao.V_STOKDETAY_MAMUL;
            if (Util.isNotEmptyOrNull(bilesenid)) {
                tablename = BaseDao.V_STOKDETAY_MAMUL_ID;
            }
        }
        return tablename;
    }

    public String getFilter0(String bilesentip) {
        String filter0;
        if (Integer.parseInt(bilesentip) == BilesenTip.HAMMADDE.value()) {
            filter0 = " AND bilesentipid IN (1) ";
        } else if (Integer.parseInt(bilesentip) == BilesenTip.YARIMAMUL.value()) {
            filter0 = " AND bilesentipid IN (2) ";
        } else {
            filter0 = " AND bilesentipid IN (3) ";
        }
        return filter0;
    }
}
