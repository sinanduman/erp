package erp.servlet;

import java.io.IOException;
import java.io.PrintWriter;
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
import erp.dao.IzlemeNoDao;
import erp.dao.MamulDao;
import erp.entity.Genel;
import erp.entity.IzlemeNo;
import erp.entity.Mamul;
import erp.util.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@WebServlet("/izlemeno")
public class IzlemeNoServlet extends HttpServlet implements Serializable {
    private static final Logger logger = LoggerFactory.getLogger(IzlemeNoServlet.class);

    @Inject
    IzlemeNoDao izlemeNoDao;
    @Inject
    MamulDao mamulDao;
    @Inject
    DAOFunctions daoFunctions;
    
    private String IzlemeNoStr(List<IzlemeNo> temp) {
        
        String tmpStr   = "";
        String delimeter= "";        
        
        for(IzlemeNo i : temp ) {
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
        doPost(request,response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        response.setContentType("text/html; charset=UTF-8");

        int totalrecord     = 0;
        int page0           = 1;
        int noofpages       = 0;
        int bilesenid       = 0;
        
        String bas_tarih    = request.getParameter("bas_tarih");
        String bit_tarih    = request.getParameter("bit_tarih");
        String page         = request.getParameter("page");
        String excelsql     = "";
        String filter0      = "";
        String filter1      = "";
        String filter2      = "";
        String andYes       = " AND ";
        String tablename    = BaseDao.V_IZLEMENO_TUM;
        Boolean ajaxInAction= false;
        
        if(request.getParameter("islemid")!=null) {
            List<IzlemeNo> result = new ArrayList<IzlemeNo>(); 
            Integer islemid     = Integer.parseInt(request.getParameter("islemid"));
            String mamulid      = request.getParameter("mamulid");
            String kullanildi   = request.getParameter("kullanildi");
            String gkrno        = request.getParameter("gkrno");
            
            
            /* with ajax generation */
            if (islemid == 1) {
                ajaxInAction= true;
                result      = izlemeNoDao.izlemeNoUret(mamulid, kullanildi);
                PrintWriter out = response.getWriter();
                out.print( IzlemeNoStr(result) );
            }
            /* with ajax delete izlemeno */
            if (islemid == 3) {
                ajaxInAction= true;
                result      = izlemeNoDao.izlemeNoSil(mamulid, gkrno, kullanildi);
                PrintWriter out = response.getWriter();
                out.print( IzlemeNoStr(result) );
            }
            /* with ajax kontrol */
            if (islemid == 6) {
                ajaxInAction= true;
                result      = izlemeNoDao.izlemeNoKontrol(mamulid, kullanildi, null);
                PrintWriter out = response.getWriter();
                out.print( IzlemeNoStr(result) );
            }
            logger.info("mamulid: " + mamulid + ", result: " + mamulid );
        }
        
        if (!ajaxInAction) {
            if(Util.isNotEmptyOrNull(request.getParameter("bilesenid"))) {
                bilesenid   = Integer.parseInt(request.getParameter("bilesenid"));
            }
                    
            filter1 = (bilesenid==0) ? "" : " and id=" + bilesenid;
            
            if(bas_tarih!=null && bit_tarih!=null && !bas_tarih.equals("") && !bit_tarih.equals("") ) {
                filter2 = andYes + 
                                " ( kullanildi_tarih BETWEEN '" + Util.date_tr_to_eng(bas_tarih) + 
                                "' AND '" + Util.date_tr_to_eng(bit_tarih) + 
                                "')";
            }
            
            List<Mamul> mamul = new ArrayList<Mamul>();
            mamul   = mamulDao.mamulListeGetir(null, 0, null);
            
            filter0 = filter1 + filter2;
            excelsql= filter0;
            
            // PAGING        
            totalrecord = daoFunctions.recordAgg(BaseDao.V_IZLEMENO_TUM,"count","*"," where 1=1" + filter0);
            if(request.getParameter("page") != null)
                page0   = Integer.parseInt(page);        
            noofpages   = (int) Math.ceil(totalrecord * 1.0 / Genel.ROWPERLONGPAGE);
            // PAGING
            
            List<IzlemeNo> stok = new ArrayList<IzlemeNo>();
            stok    = izlemeNoDao.izlemeNoTum(tablename, filter0, page0);
                    
            request.setAttribute("mamul", mamul);
            request.setAttribute("stok", stok);
            request.setAttribute("bas_tarih", bas_tarih);
            request.setAttribute("bit_tarih", bit_tarih);
            request.setAttribute("totalrecord", totalrecord);
            request.setAttribute("currentpage", page0);
            request.setAttribute("noofpages", noofpages);
            request.setAttribute("bilesenid", bilesenid);
            request.setAttribute("excelsql", excelsql);
            request.getRequestDispatcher("izlemeno.jsp").forward(request, response);
        }
    }
}
