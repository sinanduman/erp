package erp.servlet;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import erp.dao.IrsaliyeDao;
import erp.util.Util;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import erp.entity.IrsaliyeBilesen;
import erp.entity.IrsaliyeTip;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@WebServlet("/excelsevk")
public class ExcelSevkServlet extends HttpServlet implements Serializable {
	private static final Logger logger = LoggerFactory.getLogger(ExcelSevkServlet.class);

	@Inject
	IrsaliyeDao irsaliyeDao;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	    doPost(request, response);
	}
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	    response.setCharacterEncoding("UTF-8");
        
        String excelegonder     = request.getParameter("excelegonder");
        String excelirsaliyeid  = request.getParameter("excelirsaliyeid");
        String excelfirmaid     = request.getParameter("excelfirmaid");
        String excelmamulid     = request.getParameter("excelmamulid");
        String excelbastarih    = request.getParameter("excelbastarih");
        String excelbittarih    = request.getParameter("excelbittarih");
        String excelsql         = request.getParameter("excelsql");
        
        excelirsaliyeid = (Util.isNotEmptyOrNull(excelirsaliyeid))?excelirsaliyeid:null;
        excelfirmaid    = (Util.isNotEmptyOrNull(excelfirmaid))?excelfirmaid:null;
        excelbastarih   = (Util.isNotEmptyOrNull(excelbastarih))?excelbastarih:null;
        excelbittarih   = (Util.isNotEmptyOrNull(excelbittarih))?excelbittarih:null;
        excelmamulid    = (Util.isNotEmptyOrNull(excelmamulid))?excelmamulid:null;

        logger.info("excelsql: " + excelsql);
        
        if(excelegonder!=null && excelegonder.equals("1")) {
            
            List<IrsaliyeBilesen> irsaliyebilesen = irsaliyeDao.irsaliyeBilesenListeTum(IrsaliyeTip.ONAYLANDI, 1, excelirsaliyeid, excelfirmaid, excelbastarih, excelbittarih, 0, excelmamulid);
            
            Workbook workbook = new XSSFWorkbook();
            //Sheet sheeti    = workbook.createSheet("Irsaliye");
            Sheet sheetib   = workbook.createSheet("IrsaliyeBilesen");
            //int cnti        = 0;
            int cntib       = 0;
            Map<Integer, Object[]> dataib   = new TreeMap<Integer, Object[]>();
            
            dataib.put(++cntib, new Object[] {
                            "Gönderim Tarihi",
                            "İrsaliye No",
                            "Sıra No",
                            "Mamül Adı",
                            "Mamül Kodu",
                            "Firma",
                            "GKR.No",
                            "Miktarı"
                            }
            );
            
            int sayac = 0;
            int irsaliyeid= 0;
            for(IrsaliyeBilesen sr: irsaliyebilesen) {
                
                if(irsaliyeid != sr.getIrsaliyeid()) {
                    sayac = 0;
                    irsaliyeid = sr.getIrsaliyeid();
                }
                dataib.put(++cntib, new Object[] {
                                Util.getTarih(sr.getGonderimtarihi()),
                                sr.getIrsaliyeno(),
                                ++sayac,
                                sr.getMamulad(),
                                sr.getMamulkod(),
                                sr.getFirmaad(),
                                sr.getGkrno(),
                                sr.getMiktar()
                                });                
            }

            Set<Integer> keysetib = dataib.keySet();
            int rownumib = 0;
            for (Integer key : keysetib) {
                Row row = sheetib.createRow(rownumib++);
                Object [] objArr = dataib.get(key);
                int cellnum = 0;
                for (Object obj : objArr) {
                    Cell cell = row.createCell(cellnum++);                    
                    if(obj instanceof Date) {
                        cell.setCellValue((Date)obj);
                    }
                    else if(obj instanceof Boolean) {
                        cell.setCellValue((Boolean)obj);
                    }
                    else if(obj instanceof String) {
                        cell.setCellValue((String)obj);
                    }                        
                    else if(obj instanceof Double) {
                        cell.setCellValue((Double)obj);
                    }                        
                    else if(obj instanceof Integer) {
                        cell.setCellValue((Integer)obj);
                    }
                    if(key == 1) {
                        CellStyle style = workbook.createCellStyle();
                        XSSFFont font   = (XSSFFont) workbook.createFont();
                        font.setColor(HSSFColor.WHITE.index);
                        style.setFont(font);
                        style.setFillBackgroundColor( HSSFColor.RED.index );
                        style.setFillPattern(CellStyle.SOLID_FOREGROUND);
                        cell.setCellStyle(style);
                    }
                }
            }
            
            logger.info("Excel written successfully..");
            
            // write it as an excel attachment
            ByteArrayOutputStream outByteStream = new ByteArrayOutputStream();
            workbook.write(outByteStream);
            byte [] outArray = outByteStream.toByteArray();
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setHeader("Expires:", "0"); // eliminates browser caching
            response.setContentLength(outArray.length);
            response.setHeader("Content-Disposition", "attachment; filename="+ "excel_sevk_" + excelbastarih +"_"+  excelbittarih + ".xls");
            OutputStream outStream = response.getOutputStream();
            outStream.write(outArray);
            outStream.flush();
            outStream.close();
            workbook.close();
        }
        else {
            request.getRequestDispatcher("index.jsp").forward(request, response);
        }
	}
}
