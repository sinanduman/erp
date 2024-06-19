package erp.servlet;

import erp.dao.BaseDao;
import erp.dao.IzlemeNoDao;
import erp.dao.MamulDao;
import erp.entity.Genel;
import erp.entity.IzlemeNo;
import erp.entity.Mamul;
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
import java.io.PrintWriter;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/izlemeno")
public class IzlemeNoServlet extends HttpServlet implements Serializable {
    private static final Logger logger = LoggerFactory.getLogger(IzlemeNoServlet.class);

    @Inject
    private IzlemeNoDao izlemeNoDao;
    @Inject
    private MamulDao mamulDao;
    @Inject
    private DAOFunctions daoFunctions;

    private String IzlemeNoStr(List<IzlemeNo> temp) {

        String tmpStr = "";
        String delimeter = "";

        for (IzlemeNo i : temp) {
            tmpStr = tmpStr + delimeter + "{ "
                    + "\"gkrno\":\"" + i.getGkrno().toString() + "\""
                    + ",\"mamulid\":\"" + i.getMamulid().toString() + "\""
                    + ",\"mamulad\":\"" + i.getMamulad() + "\""
                    + ",\"mamulkod\":\"" + i.getMamulkod() + "\""
                    + ",\"kullanildi\":\"" + i.getKullanildi().toString() + "\""
                    + ",\"kullanildi_tarih\":\"" + Util.getTarihTR(i.getKullanildi_tarih()) + "\"" + "}";
            delimeter = ",";
        }
        tmpStr = "[" + tmpStr + "]";
        return tmpStr;

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        response.setContentType("text/html; charset=UTF-8");

        int totalrecord = 0;
        int page0 = 1;
        int noofpages = 0;
        int bilesenid = 0;

        String bas_tarih = request.getParameter(Fields.BAS_TARIH);
        String bit_tarih = request.getParameter(Fields.BIT_TARIH);
        String page = request.getParameter(Fields.PAGE);
        String excelsql = "";
        String filter0 = "";
        String filter1 = "";
        String filter2 = "";
        String andYes = " AND ";
        String tablename = BaseDao.V_IZLEMENO_TUM;
        boolean ajaxInAction = false;

        if (request.getParameter(Fields.ISLEMID) != null) {
            List<IzlemeNo> result = new ArrayList<>();
            int islemid = Integer.parseInt(request.getParameter(Fields.ISLEMID));
            String mamulid = request.getParameter(Fields.MAMULID);
            String kullanildi = request.getParameter(Fields.KULLANILDI);
            String gkrno = request.getParameter(Fields.GKRNO);


            /* with ajax generation */
            if (islemid == 1) {
                ajaxInAction = true;
                result = izlemeNoDao.izlemeNoUret(mamulid, kullanildi);
                PrintWriter out = response.getWriter();
                out.print(IzlemeNoStr(result));
            }
            /* with ajax delete izlemeno */
            if (islemid == 3) {
                ajaxInAction = true;
                result = izlemeNoDao.izlemeNoSil(mamulid, gkrno, kullanildi);
                PrintWriter out = response.getWriter();
                out.print(IzlemeNoStr(result));
            }
            /* with ajax kontrol */
            if (islemid == 6) {
                ajaxInAction = true;
                result = izlemeNoDao.izlemeNoKontrol(mamulid, kullanildi, null);
                PrintWriter out = response.getWriter();
                out.print(IzlemeNoStr(result));
            }
            logger.info("mamulid: " + mamulid + ", result: " + mamulid);
        }

        if (!ajaxInAction) {
            if (Util.isNotEmptyOrNull(request.getParameter(Fields.BILESENID))) {
                bilesenid = Integer.parseInt(request.getParameter(Fields.BILESENID));
            }

            filter1 = (bilesenid == 0) ? "" : " and id=" + bilesenid;

            if (bas_tarih != null && bit_tarih != null && !bas_tarih.equals("") && !bit_tarih.equals("")) {
                filter2 = andYes +
                        " ( kullanildi_tarih BETWEEN '" + Util.date_tr_to_eng(bas_tarih) +
                        "' AND '" + Util.date_tr_to_eng(bit_tarih) +
                        "')";
            }

            List<Mamul> mamul = mamulDao.mamulListeGetir(null, 0, null);

            filter0 = filter1 + filter2;
            excelsql = filter0;

            // PAGING        
            totalrecord = daoFunctions.recordAgg(BaseDao.V_IZLEMENO_TUM, Fields.COUNT, "*", filter0);
            if (request.getParameter(Fields.PAGE) != null)
                page0 = Integer.parseInt(page);
            noofpages = (int) Math.ceil(totalrecord * 1.0 / Genel.ROWPERLONGPAGE);
            // PAGING

            List<IzlemeNo> stok = izlemeNoDao.izlemeNoTum(tablename, filter0, page0);

            request.setAttribute(Fields.MAMUL, mamul);
            request.setAttribute(Fields.STOK, stok);
            request.setAttribute(Fields.BAS_TARIH, bas_tarih);
            request.setAttribute(Fields.BIT_TARIH, bit_tarih);
            request.setAttribute(Fields.TOTALRECORD, totalrecord);
            request.setAttribute(Fields.CURRENTPAGE, page0);
            request.setAttribute(Fields.NOOFPAGES, noofpages);
            request.setAttribute(Fields.BILESENID, bilesenid);
            request.setAttribute(Fields.EXCELSQL, excelsql);
            request.getRequestDispatcher("izlemeno.jsp").forward(request, response);
        }
    }
}
