package erp.servlet;

import erp.dao.BakimDao;
import erp.dao.BaseDao;
import erp.dao.CalisanDao;
import erp.dao.MamulDao;
import erp.entity.Bakim;
import erp.entity.Calisan;
import erp.entity.Genel;
import erp.entity.Mamul;
import erp.util.Fields;
import erp.util.Util;
import org.apache.commons.lang3.StringUtils;
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
    private CalisanDao calisanDao;
    @Inject
    private MamulDao mamulDao;
    @Inject
    private BakimDao bakimDao;
    @Inject
    private DAOFunctions daoFunctions;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html; charset=UTF-8");

        String mamulid = request.getParameter(Fields.MAMULID);
        String calisanid = request.getParameter(Fields.CALISANID);
        String bas_tarih = request.getParameter(Fields.BAS_TARIH);
        String bit_tarih = request.getParameter(Fields.BIT_TARIH);
        String page = request.getParameter(Fields.PAGE);
        String excelsql = "";

        int totalrecord = 0;
        int noofpages = 0;
        int page0 = 1;

        List<Calisan> calisan = calisanDao.calisanListeGetirTum(0, null);
        Map<Integer, Calisan> calisanmap = Util.calisanListToMap(calisan);

        List<Mamul> mamul = mamulDao.mamulListeGetir(null, 0, null);
        Map<Integer, Mamul> mamulmap = Util.mamulListToMap(mamul);

        String filter0 = "";
        String filter1 = "";
        String filter2 = "";
        String filter3 = "";
        String andYes = " AND ";
        String tableName = BaseDao.V_BAKIM_MAMUL;

        if (!StringUtils.isEmpty(mamulid)) {
            filter1 = andYes + " mamulid = " + Integer.parseInt(mamulid);
        }
        if (!StringUtils.isEmpty(calisanid)) {
            filter2 = andYes + " calisanid = " + Integer.parseInt(calisanid);
        }
        if (!StringUtils.isEmpty(bas_tarih) && !StringUtils.isEmpty(bit_tarih)) {
            filter3 = andYes + " ( bakimtarihi BETWEEN '" + Util.date_tr_to_eng(bas_tarih) + "' AND '" + Util.date_tr_to_eng(bit_tarih) + "')";
        }
        filter0 = filter1 + filter2 + filter3;

        // PAGING
        totalrecord = daoFunctions.recordAgg(tableName, Fields.COUNT, "*", filter0);
        if (request.getParameter(Fields.PAGE) != null)
            page0 = Integer.parseInt(page);
        noofpages = (int) Math.ceil(totalrecord * 1.0 / Genel.ROWPERPAGE);
        // PAGING

        List<Bakim> bakimrapor = bakimDao.bakimRapor(tableName, page0, filter0, mamulid);

        logger.info("noofpages: {}, totalrecord: {}, tablename: {}, WHERE 1=1 {}",noofpages , totalrecord, tableName, filter0);
        excelsql = filter0;

        request.setAttribute(Fields.CALISAN, calisan);
        request.setAttribute("calisanmap", calisanmap);
        request.setAttribute(Fields.MAMUL, mamul);
        request.setAttribute("mamulmap", mamulmap);
        request.setAttribute("bakimrapor", bakimrapor);
        request.setAttribute(Fields.CALISANID, calisanid);
        request.setAttribute(Fields.MAMULID, mamulid);
        request.setAttribute(Fields.BAS_TARIH, bas_tarih);
        request.setAttribute(Fields.BIT_TARIH, bit_tarih);
        request.setAttribute(Fields.TOTALRECORD, totalrecord);
        request.setAttribute(Fields.CURRENTPAGE, page0);
        request.setAttribute(Fields.NOOFPAGES, noofpages);
        request.setAttribute(Fields.EXCELSQL, excelsql);
        request.getRequestDispatcher("bakimrapor.jsp").forward(request, response);
    }
}
