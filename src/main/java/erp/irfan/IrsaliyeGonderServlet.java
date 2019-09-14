package erp.irfan;

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
import erp.dao.FirmaDao;
import erp.dao.IrsaliyeDao;
import erp.irfan.entity.Birim;
import erp.irfan.entity.Firma;
import erp.irfan.entity.Genel;
import erp.irfan.entity.Irsaliye;
import erp.irfan.entity.IrsaliyeBilesen;
import erp.irfan.entity.IrsaliyeTip;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@WebServlet("/irsaliyegonder")
public class IrsaliyeGonderServlet extends HttpServlet implements Serializable {
    private static final Logger logger = LoggerFactory.getLogger(IrsaliyeGonderServlet.class);

    @Inject
    FirmaDao firmaDao;
    @Inject
    IrsaliyeDao irsaliyeDao;
    @Inject
    DAOFunctions daoFunctions;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        List<Birim> birim = daoFunctions.birimListeGetirTum();
        List<Firma> firma = firmaDao.firmaListeGetirTum(0);
        
        // PAGING
        int totalrecord = daoFunctions.recordAgg(BaseDao.IRSALIYE, "COUNT", "*", " WHERE onaylandi=1" );
        int page = 1;
        if(request.getParameter("page") != null)
            page = Integer.parseInt(request.getParameter("page"));        
        int noofpages = (int) Math.ceil(totalrecord * 1.0 / Genel.ROWPERPAGE);
        // PAGING
        
        List<Irsaliye> irsaliye = irsaliyeDao.irsaliyeListeGetirTum(IrsaliyeTip.ONAYLANDI, page, null, null, null);
        List<IrsaliyeBilesen> irsaliyebilesenonaylandi = irsaliyeDao.irsaliyeBilesenListeGetirTum(IrsaliyeTip.ONAYLANDI, 1, null, null, null, page);
        
        request.setAttribute("birim", birim);
        request.setAttribute("firma", firma);
        request.setAttribute("irsaliye", irsaliye);
        request.setAttribute("totalrecord", totalrecord);
        request.setAttribute("currentpage", page);
        request.setAttribute("noofpages", noofpages);
        request.setAttribute("irsaliyebilesenonaylandi", irsaliyebilesenonaylandi);
        
        request.getRequestDispatcher("irsaliyegonder.jsp").forward(request, response);
    }
    
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        response.setContentType("text/html; charset=UTF-8");

        List<Birim> birim = daoFunctions.birimListeGetirTum();
        List<Firma> firma = firmaDao.firmaListeGetirTum(0);
        
        // PAGING
        int totalrecord = daoFunctions.recordCount(BaseDao.IRSALIYE," where onaylandi=1 ");
        int page = 1;
        if(request.getParameter("page") != null)
            page = Integer.parseInt(request.getParameter("page"));        
        int noofpages = (int) Math.ceil(totalrecord * 1.0 / Genel.ROWPERPAGE);
        // PAGING
        
        List<Irsaliye> irsaliye = irsaliyeDao.irsaliyeListeGetirTum(IrsaliyeTip.ONAYLANDI, page, null, null, null);
        List<IrsaliyeBilesen> irsaliyebilesenonaylandi = irsaliyeDao.irsaliyeBilesenListeGetirTum(IrsaliyeTip.ONAYLANDI, 1, null, null, null, page);
        
        request.setAttribute("birim", birim);
        request.setAttribute("firma", firma);
        request.setAttribute("irsaliye", irsaliye);
        request.setAttribute("totalrecord", totalrecord);
        request.setAttribute("currentpage", page);
        request.setAttribute("noofpages", noofpages);
        request.setAttribute("irsaliyebilesenonaylandi", irsaliyebilesenonaylandi);

        request.getRequestDispatcher("irsaliye.jsp").forward(request, response);
    }
}