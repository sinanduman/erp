package erp.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;
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
import erp.dao.FirmaDao;
import erp.dao.MamulDao;
import erp.model.BilesenTip;
import erp.entity.Bilesen;
import erp.entity.Firma;
import erp.entity.Genel;
import erp.entity.Mamul;
import erp.entity.MamulBilesen;
import erp.util.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@WebServlet("/mamul")
public class MamulServlet extends HttpServlet implements Serializable {
    private static long token = 1L;
    private static final Logger logger = LoggerFactory.getLogger(MamulServlet.class);
    @Inject
    FirmaDao firmaDao;
    @Inject
    BilesenDao bilesenDao;
    @Inject
    MamulDao mamulDao;
    @Inject
    DAOFunctions daoFunctions;
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doPost(request, response);
    }
    
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        response.setContentType("text/html; charset=UTF-8");

        String islemid  = request.getParameter("islemid");
        String message  = "";
        Integer mamulid = null;
        
        if(Util.isNotEmptyOrNull(islemid)) {
            switch (Integer.parseInt(islemid)) {
                case 1:
                    message = mamulDao.mamulGuncelle(
                                    new String(request.getParameter("bilesenid").getBytes(StandardCharsets.UTF_8)),
                                    new String(request.getParameter("firmaid").getBytes(StandardCharsets.UTF_8)),
                                    new String(request.getParameter("bilesenkod").getBytes(StandardCharsets.UTF_8)),
                                    new String(request.getParameter("bilesenad").getBytes(StandardCharsets.UTF_8)),
                                    new String(request.getParameter("figur").getBytes(StandardCharsets.UTF_8)),
                                    new String(request.getParameter("cevrimsuresi").getBytes(StandardCharsets.UTF_8)),
                                    new String(request.getParameter("esmamulid").getBytes(StandardCharsets.UTF_8)),
                                    new String(request.getParameter("durum").getBytes(StandardCharsets.UTF_8)),
                                    new String(request.getParameter("kritikesik").getBytes(StandardCharsets.UTF_8)),
                                    new String(request.getParameter("bakimtarihi").getBytes(StandardCharsets.UTF_8)),
                                    String.valueOf(request.getSession().getAttribute("userid"))
                    );
                    break;
                case 2:
                    message = mamulDao.mamulBilesenGuncelle(
                                    new String(request.getParameter("mamulid").getBytes(StandardCharsets.UTF_8)),
                                    new String(request.getParameter("bilesenid").getBytes(StandardCharsets.UTF_8)),
                                    new String(request.getParameter("miktar").getBytes(StandardCharsets.UTF_8))
                                    );
                    break;
                case 3:
                    message = mamulDao.mamulSil(new String(request.getParameter("bilesenid").getBytes(StandardCharsets.UTF_8)));
                    break;
            }
            PrintWriter out = response.getWriter();
            out.print(message);
        }
        else {
            if(Util.isNotEmptyOrNull(request.getParameter("stoklisteid")) && 
                            Integer.parseInt(request.getParameter("stoklisteid"))==1 ) {
                mamulid = Integer.parseInt(request.getParameter("mamulidara"));
            }
            /* Prevent ReSubmit */
            else if(request.getParameter("token") != null && Long.valueOf(request.getParameter("token"))==token) {
                String newMamulId = mamulDao.mamulEkle(
                        new String(request.getParameter("mamulad").getBytes("UTF-8")),
                        new String(request.getParameter("mamulkod").getBytes("UTF-8")),
                        new String(request.getParameter("mamulcevrim").getBytes("UTF-8")),
                        new String(request.getParameter("mamulfirma").getBytes("UTF-8")),
                        new String(request.getParameter("mamulfigur").getBytes("UTF-8")),
                        new String(request.getParameter("mamulbakimsuresi").getBytes("UTF-8"))
                        );
        
                /* if Error occured */
                if(!Util.isNumeric(newMamulId)) {
                    message = newMamulId;
                }
                else {
                    message = "0"; //Basarili
                    int bilesenSayisi = Integer.parseInt(request.getParameter("bilesen_length"));
                    for(int i=0; i< bilesenSayisi; i++) {
                        String bilesenid= request.getParameter("bilesenid_" + i);
                        String birimid  = request.getParameter("birimid_" + i);
                        String miktar   = request.getParameter("miktar_" + i);
                        logger.info("newMamulId: "+ newMamulId + ", bilesenid: "+ bilesenid);
                        mamulDao.mamulBilesenEkle(
                                Integer.parseInt(newMamulId),
                                Integer.parseInt(bilesenid),
                                Integer.parseInt(birimid),
                                Float.parseFloat(miktar)
                        );
                    }
                }
            }
            else {
                message="-1";
            }
                    
            List<Firma> firma = new ArrayList<Firma>();
            firma = firmaDao.firmaListeGetirTum(0);
            
            List<Bilesen> hammadde = new ArrayList<Bilesen>();
            hammadde = bilesenDao.bilesenListeGetirTum(BilesenTip.HAMMADDE, 0, null, 1); /// Aktif
            
            List<Bilesen> yarimamul = new ArrayList<Bilesen>();
            yarimamul = bilesenDao.bilesenListeGetirTum(BilesenTip.YARIMAMUL, 0, null, 1); /// Aktif
            
            // PAGING
            int totalrecord = daoFunctions.recordAgg(BaseDao.V_MAMUL_TUM, "COUNT", "*", ((mamulid==null)?"":" where id=" + mamulid));
            int page = 1;
            if(request.getParameter("page") != null)
                page = Integer.parseInt(request.getParameter("page"));        
            int noofpages = (int) Math.ceil(totalrecord * 1.0 / Genel.ROWPERPAGE);
            // PAGING
            
            List<Mamul> mamul   = new ArrayList<Mamul>();
            List<Mamul> mamultum= new ArrayList<Mamul>();
            mamul   = mamulDao.mamulListeGetir(mamulid, page, null);
            mamultum= mamulDao.mamulListeGetir(null, 0, null);
            
            List<MamulBilesen> mamulbilesen = new ArrayList<MamulBilesen>();
            mamulbilesen = mamulDao.mamulBilesenListeGetirTum(mamulid);
            
            token  = Util.getToken();
            request.setAttribute("token", token);
            request.setAttribute("firma", firma);
            request.setAttribute("hammadde", hammadde);
            request.setAttribute("yarimamul", yarimamul);
            request.setAttribute("mamul", mamul);
            request.setAttribute("mamultum", mamultum);
            request.setAttribute("message", message);
            request.setAttribute("totalrecord", totalrecord);
            request.setAttribute("currentpage", page);
            request.setAttribute("noofpages", noofpages);
            request.setAttribute("mamulbilesen", mamulbilesen);
            
            request.getRequestDispatcher("mamul.jsp").forward(request, response);
        }
    }
}
