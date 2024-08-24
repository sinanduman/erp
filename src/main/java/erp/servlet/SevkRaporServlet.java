package erp.servlet;

import erp.dao.BaseDao;
import erp.dao.FirmaDao;
import erp.dao.IrsaliyeDao;
import erp.dao.MamulDao;
import erp.entity.*;
import erp.model.IrsaliyeTip;
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
import java.util.ArrayList;
import java.util.List;

@WebServlet("/sevkrapor")
public class SevkRaporServlet extends HttpServlet implements Serializable {
    private static final Logger logger = LoggerFactory.getLogger(SevkRaporServlet.class);

    @Inject
    private FirmaDao firmaDao;
    @Inject
    private MamulDao mamulDao;
    @Inject
    private IrsaliyeDao irsaliyeDao;
    @Inject
    private DAOFunctions daoFunctions;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html; charset=UTF-8");

        String raporgetirid = request.getParameter(Fields.RAPORGETIRID);
        String irsaliyeid = request.getParameter(Fields.IRSALIYEID);
        String irsaliyeno = request.getParameter(Fields.IRSALIYENO);
        String firmaid = request.getParameter(Fields.FIRMAID);
        String tarih = request.getParameter(Fields.TARIH);
        String mamulid = request.getParameter(Fields.MAMULID);
        String page = request.getParameter(Fields.PAGE);
        String excelsql = "";

        List<Firma> firma = firmaDao.firmaListeGetirTum(0);

        int totalrecord = 0;
        int noofpages = 0;
        int page0 = 1;

        List<Mamul> mamul = mamulDao.mamulListeGetir(null, 0, null);
        List<Irsaliye> irsaliyejson = irsaliyeDao.irsaliyeListeGetirTum(IrsaliyeTip.ONAYLANDI, 0, null, null, null);
        List<IrsaliyeBilesen> irsaliyebilesenonaylandi = new ArrayList<>();

        if (raporgetirid != null || page != null) {
            String filter0 = "";
            String filter1 = "";
            String filter2 = "";
            String filter3 = "";
            String filter4 = "";
            String andYes = " AND ";
            if (irsaliyeid != null && Util.isNumeric(irsaliyeid)) {
                filter1 = andYes + " id = " + Integer.parseInt(irsaliyeid);
            } else {
                irsaliyeid = null;
            }
            if (firmaid != null && Util.isNumeric(firmaid)) {
                filter2 = andYes + " firmaid = " + Integer.parseInt(firmaid);
            } else {
                firmaid = null;
            }
            if (tarih != null && !tarih.trim().isEmpty()) {
                filter3 = andYes + " ( date(gonderimtarihi) = '" + Util.date_tr_to_eng(tarih) + "')";
            } else {
                tarih = null;
            }
            if (mamulid != null && Util.isNumeric(mamulid)) {
                filter4 = andYes + " exists (select 1 from " + BaseDao.IRSALIYEBILESEN + " ib where irsaliye.id = ib.irsaliyeid and ib.mamulid = " + Integer.parseInt(mamulid) + ")";
            } else {
                mamulid = null;
            }
            filter0 = filter1 + filter2 + filter3 + filter4;

            // PAGING
            totalrecord = daoFunctions.recordAgg(BaseDao.IRSALIYE, Fields.COUNT, "*", filter0);
            if (request.getParameter(Fields.PAGE) != null)
                page0 = Integer.parseInt(request.getParameter(Fields.PAGE));
            noofpages = (int) Math.ceil(totalrecord * 1.0 / Genel.ROWPERPAGE);
            // PAGING

            logger.info("noofpages: {}, totalrecord: {}, {} WHERE 1=1 {}", noofpages, totalrecord, BaseDao.IRSALIYE, filter0);

            irsaliyebilesenonaylandi = irsaliyeDao.irsaliyeBilesenListeTum(IrsaliyeTip.ONAYLANDI, 1, irsaliyeid, firmaid, tarih, tarih, page0, mamulid);

            excelsql = filter0;
        }

        request.setAttribute(Fields.MAMUL, mamul);
        request.setAttribute(Fields.FIRMA, firma);
        request.setAttribute(Fields.IRSALIYEID, irsaliyeid);
        request.setAttribute(Fields.IRSALIYENO, irsaliyeno);
        request.setAttribute("irsaliyejson", irsaliyejson);
        request.setAttribute("irsaliyebilesenonaylandi", irsaliyebilesenonaylandi);
        request.setAttribute(Fields.FIRMAID, firmaid);
        request.setAttribute(Fields.TARIH, tarih);
        request.setAttribute(Fields.MAMULID, mamulid);
        request.setAttribute(Fields.TOTALRECORD, totalrecord);
        request.setAttribute(Fields.CURRENTPAGE, page0);
        request.setAttribute(Fields.NOOFPAGES, noofpages);
        request.setAttribute(Fields.EXCELSQL, excelsql);
        request.getRequestDispatcher("sevkrapor.jsp").forward(request, response);
    }
}
