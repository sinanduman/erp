package erp.servlet;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.ArrayList;
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

import erp.dao.BaseDao;
import erp.dao.StokDao;
import erp.model.BilesenTip;
import erp.util.Util;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import erp.entity.Stok;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@WebServlet("/excelmamul")
public class ExcelMamulServlet extends HttpServlet implements Serializable {
	private static final Logger logger = LoggerFactory.getLogger(ExcelMamulServlet.class);

	@Inject
	StokDao stokDao;
	@Inject
	DAOFunctions daoFunctions;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	    doPost(request, response);
	}
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String excelegonder = request.getParameter("excelegonder");
        String exceltarih   = request.getParameter("exceltarih");
        String excelsql     = request.getParameter("excelsql");
        
        logger.info("excelsql: " + excelsql);
        
        if(excelegonder!=null && excelegonder.equals("1")) {

            int sumagg      = daoFunctions.recordAgg(BaseDao.V_STOKMIKTAR, "sum", "miktar", " where bilesenid= " +  Integer.parseInt(excelsql));
            List<Stok> stok = stokDao.stokListeGetirTum(BilesenTip.MAMUL, Integer.parseInt(excelsql), 0, null);
            
            Workbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet();
            
            int cnt         = 0;
            Long giren   = null;
            Long cikan   = null;
            String not      = "";
            String urunkodu = "";
            String urunadi  = "";
            String uruntarih= exceltarih;
            String prefix   = "";
            
            Map<Integer, Object[]> data = new TreeMap<Integer, Object[]>();

            if(stok.size()>0) {
                urunkodu= stok.get(0).getBilesenkod();
                urunadi = stok.get(0).getBilesenad();
                prefix  = urunkodu;
            }
            
            data.put(++cnt, new Object[] {
                            "Ürün Kodu",
                            urunkodu,
                            "Envanter Tarihi",
                            uruntarih
                            }
            );
            data.put(++cnt, new Object[] {
                            "Ürün Tanımı",
                            urunadi,
                            "Envanter Miktarı",
                            sumagg
                            }
            );
            data.put(++cnt, new Object[] {
                            "Tarih",
                            "Giren",
                            "Çıkan",
                            "Kalan",
                            "Açıklama"
                            }
            );
            for(Stok s: stok) {
                not = (s.getNot()==null)?"":s.getNot();
                if(s.getIslemyonu()==1){
                    giren = null;
                    cikan = Long.valueOf(s.getMiktar());
                }
                else{
                    giren = Long.valueOf(s.getMiktar());
                    cikan = null;
                }
                data.put(++cnt, new Object[] {
                                (Util.getTarihTR((s.getIslemyonu()==1)?s.getCikistarihi():s.getGiristarihi())),
                                giren,
                                cikan,
                                s.getKalan(),
                                not
                                });
            }

            Set<Integer> keyset = data.keySet();
            int rownum = 0;
            for (Integer key : keyset) {
                Row row = sheet.createRow(rownum++);
                Object [] objArr = data.get(key);
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
                    if(key < 4) {
                        CellStyle style = workbook.createCellStyle();
                        XSSFFont font = (XSSFFont) workbook.createFont();
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
            response.setHeader("Content-Disposition", "attachment; filename="+ "excel_" + prefix +"_"+ exceltarih + ".xls");
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
