package erp.servlet;

import erp.dao.UretimDao;
import erp.model.UretimDurum;
import erp.entity.UretimPlan;

import erp.util.Util;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.*;

@WebServlet("/excel")
public class ExcelServlet extends HttpServlet implements Serializable {
	private static final Logger logger = LoggerFactory.getLogger(ExcelServlet.class);

	@Inject
	UretimDao uretimDao;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	    doPost(request, response);
	}
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String excelegonder = request.getParameter("excelegonder");
        String exceltarih   = request.getParameter("exceltarih");
        String excelsql     = request.getParameter("excelsql");
        
        logger.info("xls: " +excelsql);
        
        if(excelegonder!=null && excelegonder.equals("1")) {
            
            List<UretimPlan> uretimplan = uretimDao.uretimPlanListeGetirExcel(UretimDurum.TAMAMLANMIS,exceltarih, excelsql);

            try (Workbook workbook = new XSSFWorkbook()) {
                Sheet sheet = workbook.createSheet();

                int cnt = 0;
                Map<Integer, Object[]> data = new TreeMap<Integer, Object[]>();
                data.put(++cnt, new Object[]{
                                "Ay",
                                "Tarih",
                                "Baş.Saati",
                                "Bit.Saati",
                                "Makine",
                                "Operatör",
                                "Müşteri",
                                "Hammadde Adı",
                                "Ham.GKR.No",
                                "Ürün Kodu",
                                "Ürün Adı",
                                "Planlanan Adet",
                                "Üretilen Adet",
                                "Hata Sebebi",
                                "Hatalı Miktar",
                                "Duruş Sebebi",
                                "Sapma %",
                        }
                );

                for (UretimPlan p : uretimplan) {
                    data.put(++cnt, new Object[]{
                            Util.date_tr_to_month(p.getTarihTR()),
                            p.getTarihTR(),
                            p.getBasZaman(),
                            p.getBitZaman(),
                            p.getMakinaad(),
                            p.getCalisanShortName(),
                            p.getFirmaad(),
                            p.getHammaddead(),
                            p.getHammaddeizlno(),
                            p.getMamulkod(),
                            p.getMamulad(),
                            p.getPlanlananmiktar(),
                            p.getUretilenmiktar(),
                            p.getHatakodu(),
                            p.getHatalimiktar(),
                            p.getDuruskodu(),
                            p.getSapma()
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
                    response.setHeader("Content-Disposition", "attachment; filename=" + "excel_" + exceltarih + ".xlsx");
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
