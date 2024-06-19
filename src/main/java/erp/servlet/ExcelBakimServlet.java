package erp.servlet;

import erp.dao.BakimDao;
import erp.dao.BaseDao;
import erp.dao.CalisanDao;
import erp.dao.MamulDao;
import erp.entity.Bakim;
import erp.entity.Calisan;
import erp.entity.Mamul;
import erp.util.Util;
import org.apache.commons.lang3.StringUtils;
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

@WebServlet("/excelbakim")
public class ExcelBakimServlet extends HttpServlet implements Serializable {
    private static final Logger logger = LoggerFactory.getLogger(ExcelBakimServlet.class);

    @Inject
    private MamulDao mamulDao;
    @Inject
    private CalisanDao calisanDao;
    @Inject
    private BakimDao bakimDao;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String excelegonder = request.getParameter("excelegonder");
        String exceltarih = request.getParameter("exceltarih");
        String excelsql = request.getParameter("excelsql");

        logger.info("excelsql: {}", excelsql);

        if (StringUtils.equals(excelegonder, "1")) {

            String mamulid = request.getParameter("excelmamulid");
            String calisanid = request.getParameter("excelcalisanid");
            String bas_tarih = request.getParameter("excelbastarih");
            String bit_tarih = request.getParameter("excelbittarih");

            List<Calisan> calisan = calisanDao.calisanListeGetirTum(0, null);
            Map<Integer, Calisan> calisanmap = Util.calisanListToMap(calisan);

            List<Mamul> mamul = mamulDao.mamulListeGetir(null, 0, null);
            Map<Integer, Mamul> mamulmap = Util.mamulListToMap(mamul);

            String filter0 = "";
            String filter1 = "";
            String filter2 = "";
            String filter3 = "";
            String andYes = " AND ";
            String tableName = BaseDao.V_BAKIM_MAMUL;

            if (!StringUtils.isEmpty(mamulid)) {
                filter1 = andYes + " mamulid = " + Integer.parseInt(mamulid);
            }
            if (!StringUtils.isEmpty(calisanid)) {
                filter2 = andYes + " calisanid = " + Integer.parseInt(calisanid);
            }
            if (!StringUtils.isEmpty(bas_tarih) && !StringUtils.isEmpty(bit_tarih)) {
                filter3 = andYes + " ( bakimtarihi BETWEEN '" + Util.date_tr_to_eng(bas_tarih) + "' AND '" + Util.date_tr_to_eng(bit_tarih) + "')";
            }
            filter0 = filter1 + filter2 + filter3;

            List<Bakim> bakimrapor = bakimDao.bakimRapor(tableName, 0, filter0, mamulid);

            try (Workbook workbook = new XSSFWorkbook()) {
                Sheet sheet = workbook.createSheet();

                int cnt = 0;
                Map<Integer, Object[]> data = new TreeMap<>();

                data.put(++cnt, new Object[]{
                                "Sonraki Bakım Zamanı",
                                "Mamül Kod",
                                "Mamül Ad",
                                "Son Bakım Zamanı",
                                "Üretim Miktarı",
                                "Baskı Sayısı",
                                "Bakıma Kalan Baskı"
                        }
                );
                for (Bakim r : bakimrapor) {
                    data.put(++cnt, new Object[]{
                            mamulmap.get(r.getMamulid()) == null ? "" : mamulmap.get(r.getMamulid()).getKod(),
                            mamulmap.get(r.getMamulid()) == null ? "" : mamulmap.get(r.getMamulid()).getAd(),
                            calisanmap.get(r.getCalisanid()) == null ? "" : calisanmap.get(r.getCalisanid()).getFullName(),
                            Util.getTarihTR(r.getBakimtarihi()),
                            r.getUretimMiktar(),
                            r.getBaskiSayisi(),
                            r.getKalanBaski()
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
                            font.setColor(HSSFColor.HSSFColorPredefined.WHITE.getIndex());
                            style.setFont(font);
                            style.setFillBackgroundColor(HSSFColor.HSSFColorPredefined.RED.getIndex());
                            style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
                            cell.setCellStyle(style);
                        }
                    }
                }

                logger.info("Excel written successfully..");

                // write it as an excel attachment
                try (ByteArrayOutputStream outByteStream = new ByteArrayOutputStream()) {
                    workbook.write(outByteStream);
                    byte[] outArray = outByteStream.toByteArray();
                    response.setContentType("application/vnd.ms-excel");
                    response.setHeader("Cache-Control", "private, no-store, no-cache, must-revalidate");
                    response.setContentLength(outArray.length);
                    response.setHeader("Content-Disposition", "attachment; filename=" + "excelbakim_" + (StringUtils.isEmpty(mamulid) ? "" : mamulid + "_") + bit_tarih + ".xlsx");
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
        } else {
            request.getRequestDispatcher("index.jsp").forward(request, response);
        }
    }
}
