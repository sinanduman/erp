package erp.servlet;

import erp.dao.BilesenDao;
import erp.dao.FirmaDao;
import erp.dao.SiparisDao;
import erp.entity.*;
import erp.model.BilesenTip;
import erp.model.ResultTip;
import erp.model.SiparisTip;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;

public class BekleyenSiparisServlet extends HttpServlet implements Serializable {
    private static final Logger logger = LoggerFactory.getLogger(BekleyenSiparisServlet.class);

    @Inject
    BilesenDao bilesenDao;
    @Inject
    SiparisDao siparisDao;
    @Inject
    FirmaDao firmaDao;
    @Inject
    DAOFunctions daoFunctions;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        List<Bilesen> bilesen = bilesenDao.bilesenListeGetirTum( BilesenTip.MAMUL,0, null, null);
        List<Siparis> siparis = siparisDao.siparisListeGetir(SiparisTip.BEKLEYEN);

        request.setAttribute("bilesen", bilesen);
        request.setAttribute("siparis", siparis);
        request.setAttribute("result", ResultTip.NORESULT);
        request.getRequestDispatcher("bekleyensiparis.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        response.setContentType("text/html; charset=UTF-8");

        List<Bilesen> bilesen = bilesenDao.bilesenListeGetirTum( BilesenTip.MAMUL,0, null, null);
        List<Siparis> siparis = siparisDao.siparisEkle (
                new String(request.getParameter("bilesen").getBytes("UTF-8")),
                new String(request.getParameter("miktar").getBytes("UTF-8")),
                new String(request.getParameter("not").getBytes("UTF-8"))
        );

        List<Birim> birim = daoFunctions.birimListeGetirTum();
        List<Firma> firma = firmaDao.firmaListeGetirTum(0);

        request.setAttribute("firma", firma);
        request.setAttribute("birim", birim);
        request.setAttribute("bilesen", bilesen);
        request.setAttribute("siparis", siparis);
        request.setAttribute("result", ( siparis.size()>0? ResultTip.OK : ResultTip.ERROR ));
        request.getRequestDispatcher("bekleyensiparis.jsp").forward(request, response);
    }
}
