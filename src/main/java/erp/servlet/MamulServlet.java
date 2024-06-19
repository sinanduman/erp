package erp.servlet;

import erp.dao.*;
import erp.entity.*;
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
import java.io.PrintWriter;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.util.List;

@WebServlet("/mamul")
public class MamulServlet extends HttpServlet implements Serializable {
    private static long token = 1L;
    private static final Logger logger = LoggerFactory.getLogger(MamulServlet.class);
    @Inject
    private CalisanDao calisanDao;
    @Inject
    private FirmaDao firmaDao;
    @Inject
    private BilesenDao bilesenDao;
    @Inject
    private MamulDao mamulDao;
    @Inject
    private DAOFunctions daoFunctions;

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doPost(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        response.setContentType("text/html; charset=UTF-8");

        String islemid = request.getParameter(Fields.ISLEMID);
        String message = "";
        Integer mamulid = null;

        if (Util.isNotEmptyOrNull(islemid)) {
            switch (Integer.parseInt(islemid)) {
                case 1:
                    message = mamulDao.mamulGuncelle(
                            new String(request.getParameter(Fields.BILESENID).getBytes(StandardCharsets.UTF_8)),
                            new String(request.getParameter(Fields.FIRMAID).getBytes(StandardCharsets.UTF_8)),
                            new String(request.getParameter(Fields.BILESENKOD).getBytes(StandardCharsets.UTF_8)),
                            new String(request.getParameter(Fields.BILESENAD).getBytes(StandardCharsets.UTF_8)),
                            new String(request.getParameter(Fields.FIGUR).getBytes(StandardCharsets.UTF_8)),
                            new String(request.getParameter(Fields.CEVRIMSURESI).getBytes(StandardCharsets.UTF_8)),
                            new String(request.getParameter(Fields.ESMAMULID).getBytes(StandardCharsets.UTF_8)),
                            new String(request.getParameter(Fields.DURUM).getBytes(StandardCharsets.UTF_8)),
                            new String(request.getParameter(Fields.KRITIKESIK).getBytes(StandardCharsets.UTF_8)),
                            new String(request.getParameter("bakimtarihi").getBytes(StandardCharsets.UTF_8)),
                            new String(request.getParameter("baskisayisi").getBytes(StandardCharsets.UTF_8)),
                            new String(request.getParameter(Fields.CALISANID).getBytes(StandardCharsets.UTF_8))
                    );
                    break;
                case 2:
                    message = mamulDao.mamulBilesenGuncelle(
                            new String(request.getParameter(Fields.MAMULID).getBytes(StandardCharsets.UTF_8)),
                            new String(request.getParameter(Fields.BILESENID).getBytes(StandardCharsets.UTF_8)),
                            new String(request.getParameter(Fields.MIKTAR).getBytes(StandardCharsets.UTF_8))
                    );
                    break;
                case 3:
                    message = mamulDao.mamulSil(new String(request.getParameter(Fields.BILESENID).getBytes(StandardCharsets.UTF_8)));
                    break;
            }
            PrintWriter out = response.getWriter();
            out.print(message);
        } else {
            if (Util.isNotEmptyOrNull(request.getParameter(Fields.STOKLISTEID)) &&
                    Integer.parseInt(request.getParameter(Fields.STOKLISTEID)) == 1) {
                mamulid = Integer.parseInt(request.getParameter("mamulidara"));
            }
            /* Prevent ReSubmit */
            else if (request.getParameter(Fields.TOKEN) != null && Long.parseLong(request.getParameter(Fields.TOKEN)) == token) {
                String newMamulId = mamulDao.mamulEkle(
                        new String(request.getParameter(Fields.MAMULAD).getBytes(StandardCharsets.UTF_8)),
                        new String(request.getParameter(Fields.MAMULKOD).getBytes(StandardCharsets.UTF_8)),
                        new String(request.getParameter("mamulcevrim").getBytes(StandardCharsets.UTF_8)),
                        new String(request.getParameter("mamulfirma").getBytes(StandardCharsets.UTF_8)),
                        new String(request.getParameter("mamulfigur").getBytes(StandardCharsets.UTF_8)),
                        new String(request.getParameter("mamulbaskisayisi").getBytes(StandardCharsets.UTF_8)),
                        new String(request.getParameter("mamulsonbakim").getBytes(StandardCharsets.UTF_8)),
                        new String(request.getParameter("mamulsonbakimyapan").getBytes(StandardCharsets.UTF_8))
                );
                /* if Error occured */
                if (!Util.isNumeric(newMamulId)) {
                    message = newMamulId;
                } else {
                    message = "0"; //Basarili
                    int bilesenSayisi = Integer.parseInt(request.getParameter("bilesen_length"));
                    for (int i = 0; i < bilesenSayisi; i++) {
                        String bilesenid = request.getParameter("bilesenid_" + i);
                        String birimid = request.getParameter("birimid_" + i);
                        String miktar = request.getParameter("miktar_" + i);
                        logger.info("newMamulId: " + newMamulId + ", bilesenid: " + bilesenid);
                        mamulDao.mamulBilesenEkle(
                                Integer.parseInt(newMamulId),
                                Integer.parseInt(bilesenid),
                                Integer.parseInt(birimid),
                                Float.parseFloat(miktar)
                        );
                    }
                }
            } else {
                message = "-1";
            }

            List<Calisan> calisan = calisanDao.calisanListeGetirTum(0, null);
            List<Firma> firma = firmaDao.firmaListeGetirTum(0);
            List<Bilesen> hammadde = bilesenDao.bilesenListeGetirTum(BilesenTip.HAMMADDE, 0, null, 1); /// Aktif
            List<Bilesen> yarimamul = bilesenDao.bilesenListeGetirTum(BilesenTip.YARIMAMUL, 0, null, 1); /// Aktif

            // PAGING
            int totalrecord = daoFunctions.recordAgg(BaseDao.V_MAMUL_TUM, Fields.COUNT, "*", mamulid == null ? "" : "AND id=" + mamulid);
            int page = 1;
            if (request.getParameter(Fields.PAGE) != null)
                page = Integer.parseInt(request.getParameter(Fields.PAGE));
            int noofpages = (int) Math.ceil(totalrecord * 1.0 / Genel.ROWPERPAGE);
            // PAGING

            List<Mamul> mamul = mamulDao.mamulListeGetir(mamulid, page, null);
            List<Mamul> mamultum = mamulDao.mamulListeGetir(null, 0, null);
            List<MamulBilesen> mamulbilesen = mamulDao.mamulBilesenListeGetirTum(mamulid);

            token = Util.getToken();
            request.setAttribute(Fields.TOKEN, token);
            request.setAttribute(Fields.CALISAN, calisan);
            request.setAttribute(Fields.FIRMA, firma);
            request.setAttribute(Fields.HAMMADDE, hammadde);
            request.setAttribute(Fields.YARIMAMUL, yarimamul);
            request.setAttribute(Fields.MAMUL, mamul);
            request.setAttribute(Fields.MAMULTUM, mamultum);
            request.setAttribute(Fields.MESSAGE, message);
            request.setAttribute(Fields.TOTALRECORD, totalrecord);
            request.setAttribute(Fields.CURRENTPAGE, page);
            request.setAttribute(Fields.NOOFPAGES, noofpages);
            request.setAttribute(Fields.MAMULBILESEN, mamulbilesen);

            request.getRequestDispatcher("mamul.jsp").forward(request, response);
        }
    }
}
