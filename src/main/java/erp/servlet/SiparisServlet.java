package erp.servlet;

import erp.dao.BilesenDao;
import erp.dao.FirmaDao;
import erp.dao.SiparisDao;
import erp.entity.Bilesen;
import erp.entity.Birim;
import erp.entity.Firma;
import erp.entity.Siparis;
import erp.model.BilesenTip;
import erp.model.ResultTip;
import erp.model.SiparisTip;
import erp.util.Fields;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class SiparisServlet extends HttpServlet implements Serializable {

    private static final Logger logger = LoggerFactory.getLogger(SiparisServlet.class);

    @Inject
    private BilesenDao bilesenDao;
    @Inject
    private SiparisDao siparisDao;
    @Inject
    private FirmaDao firmaDao;
    @Inject
    private DAOFunctions daoFunctions;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        List<Bilesen> bilesen = bilesenDao.bilesenListeGetirTum(BilesenTip.MAMUL, 0, null, null);

        List<Siparis> siparis = siparisDao.siparisListeGetir(SiparisTip.BEKLEYEN);

        request.setAttribute(Fields.BILESEN, bilesen);
        request.setAttribute(Fields.SIPARIS, siparis);
        request.setAttribute(Fields.RESULT, ResultTip.NORESULT);
        request.getRequestDispatcher("siparis.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        response.setContentType("text/html; charset=UTF-8");

        List<Bilesen> bilesen = bilesenDao.bilesenListeGetirTum(BilesenTip.MAMUL, 0, null, null);

        List<Siparis> siparis = siparisDao.siparisEkle(
                new String(request.getParameter(Fields.BILESEN).getBytes(StandardCharsets.UTF_8)),
                new String(request.getParameter(Fields.MIKTAR).getBytes(StandardCharsets.UTF_8)),
                new String(request.getParameter(Fields.NOT).getBytes(StandardCharsets.UTF_8))
        );

        List<Birim> birim = daoFunctions.birimListeGetirTum();
        List<Firma> firma = firmaDao.firmaListeGetirTum(0);

        request.setAttribute(Fields.FIRMA, firma);
        request.setAttribute(Fields.BIRIM, birim);
        request.setAttribute(Fields.BILESEN, bilesen);
        request.setAttribute(Fields.SIPARIS, siparis);
        request.setAttribute(Fields.RESULT, (!siparis.isEmpty() ? ResultTip.OK : ResultTip.ERROR));
        request.getRequestDispatcher("siparis.jsp").forward(request, response);
    }
}
