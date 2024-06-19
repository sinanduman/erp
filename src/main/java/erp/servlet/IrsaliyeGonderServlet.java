package erp.servlet;

import erp.dao.BaseDao;
import erp.dao.FirmaDao;
import erp.dao.IrsaliyeDao;
import erp.entity.*;
import erp.model.IrsaliyeTip;
import erp.util.Fields;
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

@WebServlet("/irsaliyegonder")
public class IrsaliyeGonderServlet extends HttpServlet implements Serializable {
    private static final Logger logger = LoggerFactory.getLogger(IrsaliyeGonderServlet.class);

    @Inject
    private FirmaDao firmaDao;
    @Inject
    private IrsaliyeDao irsaliyeDao;
    @Inject
    private DAOFunctions daoFunctions;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        List<Birim> birim = daoFunctions.birimListeGetirTum();
        List<Firma> firma = firmaDao.firmaListeGetirTum(0);

        // PAGING
        int totalrecord = daoFunctions.recordAgg(BaseDao.IRSALIYE, Fields.COUNT, "*", "AND onaylandi=1");
        int page = 1;
        if (request.getParameter(Fields.PAGE) != null)
            page = Integer.parseInt(request.getParameter(Fields.PAGE));
        int noofpages = (int) Math.ceil(totalrecord * 1.0 / Genel.ROWPERPAGE);
        // PAGING

        List<Irsaliye> irsaliye = irsaliyeDao.irsaliyeListeGetirTum(IrsaliyeTip.ONAYLANDI, page, null, null, null);
        List<IrsaliyeBilesen> irsaliyebilesenonaylandi = irsaliyeDao.irsaliyeBilesenListeGetirTum(IrsaliyeTip.ONAYLANDI, 1, null, null, null, page);

        request.setAttribute(Fields.BIRIM, birim);
        request.setAttribute(Fields.FIRMA, firma);
        request.setAttribute(Fields.IRSALIYE, irsaliye);
        request.setAttribute(Fields.TOTALRECORD, totalrecord);
        request.setAttribute(Fields.CURRENTPAGE, page);
        request.setAttribute(Fields.NOOFPAGES, noofpages);
        request.setAttribute("irsaliyebilesenonaylandi", irsaliyebilesenonaylandi);

        request.getRequestDispatcher("irsaliyegonder.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        response.setContentType("text/html; charset=UTF-8");

        List<Birim> birim = daoFunctions.birimListeGetirTum();
        List<Firma> firma = firmaDao.firmaListeGetirTum(0);

        // PAGING
        int totalrecord = daoFunctions.recordAgg(BaseDao.IRSALIYE, Fields.COUNT, "*", "AND onaylandi=1");
        int page = 1;
        if (request.getParameter(Fields.PAGE) != null)
            page = Integer.parseInt(request.getParameter(Fields.PAGE));
        int noofpages = (int) Math.ceil(totalrecord * 1.0 / Genel.ROWPERPAGE);
        // PAGING

        List<Irsaliye> irsaliye = irsaliyeDao.irsaliyeListeGetirTum(IrsaliyeTip.ONAYLANDI, page, null, null, null);
        List<IrsaliyeBilesen> irsaliyebilesenonaylandi = irsaliyeDao.irsaliyeBilesenListeGetirTum(IrsaliyeTip.ONAYLANDI, 1, null, null, null, page);

        request.setAttribute(Fields.BIRIM, birim);
        request.setAttribute(Fields.FIRMA, firma);
        request.setAttribute(Fields.IRSALIYE, irsaliye);
        request.setAttribute(Fields.TOTALRECORD, totalrecord);
        request.setAttribute(Fields.CURRENTPAGE, page);
        request.setAttribute(Fields.NOOFPAGES, noofpages);
        request.setAttribute("irsaliyebilesenonaylandi", irsaliyebilesenonaylandi);

        request.getRequestDispatcher("irsaliye.jsp").forward(request, response);
    }
}