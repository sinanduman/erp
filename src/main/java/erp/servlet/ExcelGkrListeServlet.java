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

import erp.dao.BaseDao;
import erp.dao.StokDao;
import erp.model.BilesenTip;
import erp.util.Util;
import org.apache.commons.lang3.exception.ExceptionUtils;
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

@WebServlet("/excelgkrliste")
public class ExcelGkrListeServlet extends HttpServlet implements Serializable {
	private static final Logger logger = LoggerFactory.getLogger(ExcelGkrListeServlet.class);

	@Inject
	StokDao stokDao;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	    doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String excelegonder = request.getParameter("excelegonder");
        String excelbastarih= request.getParameter("excelbastarih");
        String excelbittarih= request.getParameter("excelbittarih");
        String excelsql     = request.getParameter("excelsql");
        String tablename    = BaseDao.V_STOKBILESEN;
                
        logger.info("excelsql: " + excelsql);
        
        if(excelegonder!=null && excelegonder.equals("1")) {
            
            List<Stok> stokrapor = stokDao.stokListeRapor(tablename, 0, excelsql);

            try (Workbook workbook = new XSSFWorkbook()) {
                Sheet sheet = workbook.createSheet();

                int cnt = 0;
                Map<Integer, Object[]> data = new TreeMap<Integer, Object[]>();

                data.put(++cnt, new Object[]{
                                "Tarih",
                                "Firma",
                                "Bileşen Kod",
                                "Bileşen Ad",
                                "Miktar",
                                "GKR.No",
                                "İrsaliye No",
                                "Lot/Batch No",
                                "Açıklama"
                        }
                );

                for (Stok sr : stokrapor) {
                    String smiktar = "";
                    Double dmkitar = 0.0;
                    Long imiktar = 0L;
                    if (sr.getBilesentipid() == BilesenTip.HAMMADDE.value()) {
                        if ((Long.valueOf(sr.getMiktar()) / 1000.0) > (Long.valueOf(sr.getMiktar()) / 1000)) {
                            dmkitar = (Long.valueOf(sr.getMiktar())) / 1000.0;
                            smiktar = dmkitar.toString() + " Kg.";
                        } else {
                            imiktar = (Long.valueOf(sr.getMiktar())) / 1000;
                            smiktar = imiktar.toString() + " Kg.";
                        }
                    } else {
                        smiktar = sr.getMiktar().toString() + " Ad.";
                    }
                    data.put(++cnt, new Object[]{
                            Util.getTarihTR(sr.getGiristarihi()),
                            sr.getFirmaad(),
                            sr.getBilesenkod(),
                            sr.getBilesenad(),
                            smiktar,
                            sr.getGkrno(),
                            sr.getIrsaliyeno(),
                            sr.getLot(),
                            sr.getNot()
                    });
                }

                Set<Integer> keyset = data.keySet();
                int rownum = 0;
                for (Integer key : keyset) {
                    Row row = sheet.createRow(rownum++);
                    Object[] objArr = data.get(key);
                    int cellnum = 0;
                    for (Object obj : objArr) {
                        Cell cell = row.createCell(cellnum++);
                        if (obj instanceof Date) {
                            cell.setCellValue((Date) obj);
                        } else if (obj instanceof Boolean) {
                            cell.setCellValue((Boolean) obj);
                        } else if (obj instanceof String) {
                            cell.setCellValue((String) obj);
                        } else if (obj instanceof Double) {
                            cell.setCellValue((Double) obj);
                        } else if (obj instanceof Integer) {
                            cell.setCellValue((Integer) obj);
                        }
                        if (key == 1) {
                            CellStyle style = workbook.createCellStyle();
                            XSSFFont font = (XSSFFont) workbook.createFont();
                            font.setColor(HSSFColor.WHITE.index);
                            style.setFont(font);
                            style.setFillBackgroundColor(HSSFColor.RED.index);
                            style.setFillPattern(CellStyle.SOLID_FOREGROUND);
                            cell.setCellStyle(style);
                        }
                    }
                }

                logger.info("Excel written successfully..");

                // write it as an excel attachment
                try (ByteArrayOutputStream outByteStream = new ByteArrayOutputStream()) {
                    workbook.write(outByteStream);
                    byte[] outArray = outByteStream.toByteArray();
                    response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
                    response.setHeader("Cache-Control", "private, no-store, no-cache, must-revalidate");
                    response.setContentLength(outArray.length);
                    response.setHeader("Content-Disposition", "attachment; filename=" + "excel_gkrliste_" + excelbastarih + "_" + excelbittarih + ".xlsx");
                    OutputStream outStream = response.getOutputStream();
                    outStream.write(outArray);
                    outStream.flush();
                    outStream.close();
                } catch (Exception e) {
                    logger.error(ExceptionUtils.getStackTrace(e));
                }
            } catch (Exception e) {
                logger.error(ExceptionUtils.getStackTrace(e));
            }
        }
        else {
            request.getRequestDispatcher("index.jsp").forward(request, response);
        }
	}
}
