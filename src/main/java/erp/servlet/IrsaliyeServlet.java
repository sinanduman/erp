package erp.servlet;

import erp.dao.FirmaDao;
import erp.dao.IrsaliyeDao;
import erp.dao.StokDao;
import erp.entity.*;
import erp.model.IrsaliyeEnum;
import erp.model.IrsaliyeTip;
import erp.util.Util;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.activation.MimetypesFileTypeMap;
import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@WebServlet("/irsaliye")
public class IrsaliyeServlet extends HttpServlet implements Serializable {
    @Inject
    IrsaliyeDao irsaliyeDao;
    @Inject
    FirmaDao firmaDao;
    @Inject
    StokDao stokDao;
    @Inject
    DAOFunctions daoFunctions;
    private static final Logger logger = LoggerFactory.getLogger(IrsaliyeServlet.class);
    // location to store file uploaded
    private static final String CATALINA_HOME = System.getProperty("catalina.home");
    private static final String UPLOAD_DIRECTORY = "/upload/excel";
    private static final String EXCEL_MIME_TYPE = "application/octet-stream";

    // upload settings
    private static final int MEMORY_THRESHOLD = 1024 * 1024 * 3;  // 3MB
    private static final int MAX_FILE_SIZE = 1024 * 1024 * 40; // 40MB
    private static final int MAX_REQUEST_SIZE = 1024 * 1024 * 50; // 50MB

    private String r_islemid;
    private String r_irsaliyeid;
    private String r_irsaliyebilesenid;
    private String r_irsaliyeno;
    private String r_mamulid;
    private String r_gkrno;
    private String r_miktar;
    private String r_not;
    private String r_tarih;
    private String r_firmaid;
    private StringBuffer pageFlash = new StringBuffer("");

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html; charset=UTF-8");
        String message = "";
        Boolean ajaxInAction = false;
        Boolean excelRowsProcessed = false;
        List<Stok> stok = null;

        if (ServletFileUpload.isMultipartContent(request)) {
            if(stok == null)
                stok = stokDao.stokMamulListeGetirTum(null, 1);
            excelRowsProcessed = processUploadRequest(request, response, stok);
        }
        else {
            getFormFieldsWithoutFile(request);
        }

        if (r_islemid != null) {
            Integer islemid = Integer.valueOf(r_islemid);
            /* with ajax */
            if (islemid == IrsaliyeEnum.IRSALIYE_SIL.getValue()) {
                ajaxInAction = true;
                String result = irsaliyeDao.irsaliyeSil(r_irsaliyeid);
                PrintWriter out = response.getWriter();
                out.print(result);
            } else if (islemid == IrsaliyeEnum.IRSALIYE_ONAYLA.getValue()) {
                ajaxInAction = true;
                String result = irsaliyeDao.irsaliyeOnayla(r_irsaliyeid);
                PrintWriter out = response.getWriter();
                out.print(result);
            } else if (islemid == IrsaliyeEnum.IRSALIYE_BILESEN_SIL.getValue()) {
                ajaxInAction = true;
                Integer result = irsaliyeDao.irsaliyeBilesenSil(r_irsaliyebilesenid);
                PrintWriter out = response.getWriter();
                out.print(result);
            } else if (islemid == IrsaliyeEnum.IRSALIYE_NO_KONTROL.getValue()) {
                ajaxInAction = true;
                String result = irsaliyeDao.irsaliyeNoKontrol(r_irsaliyeno);
                PrintWriter out = response.getWriter();
                out.print(result);
            } else {
                /* no ajax */
                logger.info("islemid :" + islemid);
                if (islemid == IrsaliyeEnum.IRSALIYE_BILESEN_GUNCELLE.getValue()) {
                    int irsaliyeid = Integer.valueOf(r_irsaliyeid);
                    int irsaliyebilesenid = Integer.valueOf(r_irsaliyebilesenid);
                    int mamulid = Integer.valueOf(r_mamulid);
                    int gkrno = Integer.valueOf(r_gkrno);
                    int miktar = Integer.valueOf(r_miktar);
                    String not = r_not;

                    irsaliyeDao.irsaliyeBilesenGuncelle(irsaliyeid, irsaliyebilesenid, mamulid, gkrno, miktar, not);
                } else if (islemid == IrsaliyeEnum.IRSALIYE_KAPAT.getValue()) {
                    irsaliyeDao.irsaliyeKapat(r_irsaliyeid);
                } else {
                    int irsaliyeid = Integer.valueOf(r_irsaliyeid);
                    logger.info("irsaliyeid :" + irsaliyeid);
                    /* 0:Insert */
                    if (IrsaliyeEnum.IRSALIYE_EKLE.getValue() == irsaliyeid) {
                        irsaliyeid = irsaliyeEkle();
                    }
                    if(!excelRowsProcessed)
                        message = irsaliyeBilesenEkle(r_mamulid, r_miktar, r_not, irsaliyeid);
                }
            }
        }

        if (!ajaxInAction) {
            if(!excelRowsProcessed){
                pageFlash = new StringBuffer("");
            }
            if(stok == null)
                stok = stokDao.stokMamulListeGetirTum(null, 1);
            List<Firma> firma = firmaDao.firmaListeGetirTum(0);
            List<Irsaliye> irsaliye = irsaliyeDao.irsaliyeListeGetirTum(IrsaliyeTip.OPEN, 0, null, null, null);

            /* Son eklenen irsaliyeyi getir */
            List<Irsaliye> irsaliyekapali = irsaliyeDao.irsaliyeListeGetirKapaliTum(IrsaliyeTip.COMPLETED, 1);
            List<IrsaliyeBilesen> irsaliyebilesenopen = irsaliyeDao.irsaliyeBilesenListeGetirTum(IrsaliyeTip.OPEN, 0, null, null, null, 0);
            List<IrsaliyeBilesen> irsaliyebilesencompleted = irsaliyeDao.irsaliyeBilesenListeGetirTum(IrsaliyeTip.COMPLETED, 0, null, null, null, 0);

            request.setAttribute("firma", firma);
            request.setAttribute("stok", stok);
            request.setAttribute("message", message);
            request.setAttribute("pageflash", pageFlash);
            request.setAttribute("irsaliye", irsaliye);
            request.setAttribute("irsaliyekapali", irsaliyekapali);
            request.setAttribute("irsaliyebilesenopen", irsaliyebilesenopen);
            request.setAttribute("irsaliyebilesencompleted", irsaliyebilesencompleted);

            request.getRequestDispatcher("irsaliye.jsp").forward(request, response);
        }
    }

    private boolean processUploadRequest(HttpServletRequest request, HttpServletResponse response, List<Stok> stok) throws ServletException, IOException {
        boolean retVal = false;
        // configures upload settings
        DiskFileItemFactory factory = new DiskFileItemFactory();
        // sets memory threshold - beyond which files are stored in disk
        factory.setSizeThreshold(MEMORY_THRESHOLD);
        // sets temporary location to store files
        factory.setRepository(new File(System.getProperty("java.io.tmpdir")));

        ServletFileUpload upload = new ServletFileUpload(factory);

        // sets maximum size of upload file
        upload.setFileSizeMax(MAX_FILE_SIZE);

        // sets maximum size of request (include file + form data)
        upload.setSizeMax(MAX_REQUEST_SIZE);

        // constructs the directory path to store upload file
        // this path is relative to application's directory
        String uploadPath = CATALINA_HOME + UPLOAD_DIRECTORY;

        // creates the directory if it does not exist
        File uploadDir = new File(uploadPath);
        if (!uploadDir.exists()) {
            uploadDir.mkdir();
        }

        try {
            List<FileItem> formItems = upload.parseRequest(request);
            /*First form fields, later file part*/
            if (formItems != null && formItems.size() > 0) {
                getFormFieldsWithFile(formItems);
                /*Binary excel file*/
                for (FileItem item : formItems) {
                    if (!item.isFormField()) {
                        String fileName = new File(item.getName()).getName();
                        String filePath = uploadPath + File.separator + fileName;
                        File storeFile = new File(filePath);
                        // saves the file on disk
                        item.write(storeFile);

                        MimetypesFileTypeMap fileTypeMap = new MimetypesFileTypeMap();
                        String mimeType = fileTypeMap.getContentType(storeFile);

                        logger.info("MimeType: " + mimeType + " ? " + (mimeType.equals(EXCEL_MIME_TYPE)));
                        if (mimeType.equals(EXCEL_MIME_TYPE)) {
                            retVal = processExcelFile(storeFile, r_irsaliyeid, stok);
                        }
                        request.getSession().setAttribute("message", "Dosya başarıyla yüklendi.");
                    }
                }
            }
        } catch (Exception ex) {
            request.getSession().setAttribute("message", "There was an error: " + ex.getMessage());
        }
        // request.getRequestDispatcher("irsaliye.jsp").forward(request, response);
        return retVal;
    }

    private boolean processExcelFile(File storeFile, String irsaliyeId, List<Stok> stok) {
        boolean retVal = false;
        String message = "";
        pageFlash = new StringBuffer("");
        try {
            // Creating Input Stream
            FileInputStream myInput = new FileInputStream(storeFile);

            // Create a workbook using the File System
            XSSFWorkbook myWorkBook = new XSSFWorkbook(myInput);

            // Get the first sheet from workbook
            XSSFSheet mySheet = myWorkBook.getSheetAt(0);

            List<IrsaliyeUploadData> uploadDataList = new ArrayList<>();

            /** We now need something to iterate through the cells.**/
            Iterator<Row> rowIter = mySheet.rowIterator();
            int rCount = 0;
            boolean emptyLine = false;
            String pipe = "";
            while (rowIter.hasNext() && !emptyLine) {
                XSSFRow myRow = (XSSFRow) rowIter.next();
                /*Ilk satiri almamak icin*/
                if (rCount++ < 3) /* satir baslangici */
                    continue;

                IrsaliyeUploadData uploadData = new IrsaliyeUploadData();
                Iterator<Cell> cellIter = myRow.cellIterator();
                while (cellIter.hasNext() && !emptyLine) {
                    XSSFCell myCell = (XSSFCell) cellIter.next();

                    String cellValue = myCell.getCellType() == XSSFCell.CELL_TYPE_NUMERIC ? String.valueOf(myCell.getRawValue()) : myCell.getStringCellValue();
                    switch (myCell.getColumnIndex()) {
                        case 1:
                            if(cellValue.trim().isEmpty()) {
                                emptyLine = true;
                            }
                            else {
                                uploadData.setMamulKodu(cellValue);
                                uploadData.setMamulId(getMamuldIdFromMamulKod(cellValue, stok));
                            }
                            break;
                        case 2:
                            uploadData.setNot(cellValue);
                        case 3:
                            uploadData.setMiktar(cellValue);
                            break;
                    }
                }
                if (emptyLine || uploadData.getMamulKodu() == null)
                    continue;

                int newIrsaliyeId = 0;
                if (Integer.parseInt(r_irsaliyeid) == 0) {
                    newIrsaliyeId = irsaliyeEkle();
                    r_irsaliyeid = String.valueOf(newIrsaliyeId);
                }
                else {
                    newIrsaliyeId = Integer.valueOf(r_irsaliyeid);
                }
                if(uploadData.getMamulId().equals("0")) {
                    message = "<strong>" + uploadData.getMamulKodu() + "</strong>, " + uploadData.getNot() + " Stok'ta bulunamadı!";
                    pageFlash.append(pipe + message);
                    pipe = "<br/>";
                }
                else {
                    message = irsaliyeBilesenEkle(uploadData.getMamulId(), uploadData.getMiktar(), uploadData.getNot(), newIrsaliyeId);
                    if (!message.equals("0")){
                        if(Util.isNumeric(message)){
                            message = "<strong>" + uploadData.getMamulKodu() + "</strong>, " + uploadData.getNot() + " için <strong style='color:green;'>"+ message +"</strong> adet daha ekleyiniz!";
                            pageFlash.append(pipe + message);
                            pipe = "<br/>";
                        }
                    }
                }
                System.out.println(message);
                retVal = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return retVal;
    }

    private int irsaliyeEkle() {
        String irsaliyeno = r_irsaliyeno;
        String tarih = r_tarih;
        String firmaid = r_firmaid;
        int newIrsaliyeId = irsaliyeDao.irsaliyeEkle(irsaliyeno, tarih, firmaid);

        /* if Error occured */
        if (newIrsaliyeId == -1) {
            logger.info("Hata Olustu!..");
        }
        return newIrsaliyeId;
    }

    private String irsaliyeBilesenEkle(String mamulId, String miktar, String not, int newIrsaliyeId) {
        return irsaliyeDao.irsaliyeBilesenEkle(
                            newIrsaliyeId,
                            Integer.valueOf(mamulId),
                            Integer.valueOf(miktar),
                not);
    }

    private String getMamuldIdFromMamulKod(String mamulKodu, List<Stok> stok) {
        for (Stok s : stok) {
            String str = s.getBilesenkod();
            str = str.replaceFirst("^0+", "");
            if (str.equals(String.valueOf(Long.valueOf(mamulKodu))))
                return String.valueOf(s.getBilesenid());
        }
        return "0";
    }

    private void getFormFieldsWithoutFile(HttpServletRequest request){
        r_islemid = request.getParameter("islemid");
        r_irsaliyeid = request.getParameter("irsaliyeid");
        r_irsaliyebilesenid = request.getParameter("irsaliyebilesenid");
        r_irsaliyeno = request.getParameter("irsaliyeno");
        r_mamulid = request.getParameter("mamulid");
        r_gkrno = request.getParameter("gkrno");
        r_miktar = request.getParameter("miktar");
        r_not = request.getParameter("not");
        r_tarih = request.getParameter("tarih");
        r_firmaid = request.getParameter("firmaid");
    }

    private void getFormFieldsWithFile(List<FileItem> formItems){
        for (FileItem item : formItems) {
            if (item.isFormField()) {
                if (item.getFieldName().equals("islemid")) {
                    r_islemid = item.getString();
                } else if (item.getFieldName().equals("irsaliyeid")) {
                    r_irsaliyeid = item.getString();
                } else if (item.getFieldName().equals("irsaliyebilesenid")) {
                    r_irsaliyebilesenid = item.getString();
                } else if (item.getFieldName().equals("irsaliyeno")) {
                    r_irsaliyeno = item.getString();
                } else if (item.getFieldName().equals("mamulid")) {
                    r_mamulid = item.getString();
                } else if (item.getFieldName().equals("gkrno")) {
                    r_gkrno = item.getString();
                } else if (item.getFieldName().equals("miktar")) {
                    r_miktar = item.getString();
                } else if (item.getFieldName().equals("not")) {
                    r_not = item.getString();
                } else if (item.getFieldName().equals("tarih")) {
                    r_tarih = item.getString();
                } else if (item.getFieldName().equals("firmaid")) {
                    r_firmaid = item.getString();
                }
            }
        }
    }
}