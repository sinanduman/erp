package erp.servlet;

import erp.dao.FirmaDao;
import erp.dao.IrsaliyeDao;
import erp.dao.StokDao;
import erp.entity.*;
import erp.model.IrsaliyeEnum;
import erp.model.IrsaliyeTip;
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
    private static final int MEMORY_THRESHOLD   = 1024 * 1024 * 3;  // 3MB
    private static final int MAX_FILE_SIZE      = 1024 * 1024 * 40; // 40MB
    private static final int MAX_REQUEST_SIZE   = 1024 * 1024 * 50; // 50MB

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html; charset=UTF-8");

        String message        = "";
        Boolean ajaxInAction  = false;

        if(request.getParameter("islemid")!=null) {
            Integer islemid = Integer.valueOf(request.getParameter("islemid"));
            /* with ajax */
            if (islemid == IrsaliyeEnum.IRSALIYE_SIL.getValue()) {
                ajaxInAction  = true;
                String result = irsaliyeDao.irsaliyeSil(request.getParameter("irsaliyeid"));
                PrintWriter out = response.getWriter();
                out.print(result);
            }
            /* with ajax */
            else if (islemid == IrsaliyeEnum.IRSALIYE_ONAYLA.getValue()) {
                ajaxInAction  = true;
                String result = irsaliyeDao.irsaliyeOnayla(request.getParameter("irsaliyeid"));
                PrintWriter out = response.getWriter();
                out.print(result);
            }
            else if (islemid == IrsaliyeEnum.IRSALIYE_BILESEN_SIL.getValue()) {
                ajaxInAction  = true;
                Integer result  = irsaliyeDao.irsaliyeBilesenSil(request.getParameter("irsaliyebilesenid"));
                PrintWriter out = response.getWriter();
                out.print(result);
            }
            else if (islemid == IrsaliyeEnum.IRSALIYE_NO_KONTROL.getValue()) {
                ajaxInAction  = true;
                String result   = irsaliyeDao.irsaliyeNoKontrol(request.getParameter("irsaliyeno"));
                PrintWriter out = response.getWriter();
                out.print(result);
            }
            else {
                logger.info("islemid :" + islemid);
                if (islemid == IrsaliyeEnum.IRSALIYE_BILESEN_GUNCELLE.getValue()) {
                    int irsaliyeid         = Integer.valueOf(request.getParameter("irsaliyeid"));
                    int irsaliyebilesenid  = Integer.valueOf(request.getParameter("irsaliyebilesenid"));
                    int mamulid            = Integer.valueOf(request.getParameter("mamulid"));
                    int gkrno              = Integer.valueOf(request.getParameter("gkrno"));
                    int miktar             = Integer.valueOf(request.getParameter("miktar"));
                    String not             = request.getParameter("not");

                    irsaliyeDao.irsaliyeBilesenGuncelle(irsaliyeid, irsaliyebilesenid, mamulid, gkrno, miktar, not );
                }
                else if (islemid == IrsaliyeEnum.IRSALIYE_KAPAT.getValue()) {
                    irsaliyeDao.irsaliyeKapat(request.getParameter("irsaliyeid"));
                }
                else {
                    int irsaliyeid = Integer.valueOf(request.getParameter("irsaliyeid"));
                    logger.info("irsaliyeid :" + irsaliyeid);
                    /* 0:Insert */
                    if (IrsaliyeEnum.IRSALIYE_EKLE.getValue() == irsaliyeid) {
                        String irsaliyeno = request.getParameter("irsaliyeno");
                        String tarih      = request.getParameter("tarih");
                        String firmaid    = request.getParameter("firmaid");
                        int newIrsaliyeId = irsaliyeDao.irsaliyeEkle(irsaliyeno, tarih, firmaid);

                        /* if Error occured */
                        if (newIrsaliyeId == -1) {
                            logger.info("Hata Olustu!..");
                        }
                        else {
                            irsaliyeid = newIrsaliyeId;
                        }
                    }
                    String mamulid     = request.getParameter("mamulid");
                    String miktar      = request.getParameter("miktar");
                    String not         = request.getParameter("not");

                    message = irsaliyeDao.irsaliyeBilesenEkle(
                            irsaliyeid,
                            Integer.valueOf(mamulid),
                            Integer.valueOf(miktar),
                            not);
                }
            }
        }
        if(!ajaxInAction) {
            List<Birim> birim = daoFunctions.birimListeGetirTum();
            List<Firma> firma = firmaDao.firmaListeGetirTum(0);
            List<Stok> stok = stokDao.stokMamulListeGetirTum(null, 1);
            List<Irsaliye> irsaliye = irsaliyeDao.irsaliyeListeGetirTum(IrsaliyeTip.OPEN, 0, null, null, null);

            /* Son eklenen irsaliyeyi getir */
            List<Irsaliye> irsaliyekapali = irsaliyeDao.irsaliyeListeGetirKapaliTum(IrsaliyeTip.COMPLETED, 1);
            List<IrsaliyeBilesen> irsaliyebilesenopen = irsaliyeDao.irsaliyeBilesenListeGetirTum(IrsaliyeTip.OPEN, 0, null, null, null,0);
            List<IrsaliyeBilesen> irsaliyebilesencompleted = irsaliyeDao.irsaliyeBilesenListeGetirTum(IrsaliyeTip.COMPLETED, 0, null, null, null,0);

            if (ServletFileUpload.isMultipartContent(request)) {
                processUploadRequest(request, response);
            }

            request.setAttribute("birim", birim);
            request.setAttribute("firma", firma);
            request.setAttribute("stok", stok);
            request.setAttribute("message", message);
            request.setAttribute("irsaliye", irsaliye);
            request.setAttribute("irsaliyekapali", irsaliyekapali);
            request.setAttribute("irsaliyebilesenopen", irsaliyebilesenopen);
            request.setAttribute("irsaliyebilesencompleted", irsaliyebilesencompleted);

            request.getRequestDispatcher("irsaliye.jsp").forward(request, response);
        }
    }

    protected void processUploadRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
            // parses the request's content to extract file data
            @SuppressWarnings("unchecked")
            List<FileItem> formItems = upload.parseRequest(request);

            if (formItems != null && formItems.size() > 0) {
                // iterates over form's fields
                for (FileItem item : formItems) {
                    // processes only fields that are not form fields
                    if (!item.isFormField()) {
                        String fileName = new File(item.getName()).getName();
                        String filePath = uploadPath + File.separator + fileName;
                        File storeFile = new File(filePath);
                        // saves the file on disk
                        item.write(storeFile);

                        MimetypesFileTypeMap fileTypeMap = new MimetypesFileTypeMap();
                        String mimeType = fileTypeMap.getContentType(storeFile);

                        logger.info("MimeType: " + mimeType + " ? " + (mimeType.equals(EXCEL_MIME_TYPE)));
                        if(mimeType.equals(EXCEL_MIME_TYPE)){
                            processExcelFile(storeFile);
                        }
                        request.getSession().setAttribute("message","Dosya başarıyla yüklendi.");
                    }
                }
            }
        } catch (Exception ex) {
            request.getSession().setAttribute("message","There was an error: " + ex.getMessage());
        }
        // redirects client to message page
        // request.getRequestDispatcher("irsaliye.jsp").forward(request, response);
    }

    public void processExcelFile(File storeFile){
        int count = 0;
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
            while(rowIter.hasNext()){
                XSSFRow myRow = (XSSFRow) rowIter.next();
                if (count++ == 0)
                    continue;

                IrsaliyeUploadData uploadData = new IrsaliyeUploadData();

                Iterator<Cell> cellIter = myRow.cellIterator();
                while(cellIter.hasNext()){

                    XSSFCell myCell = (XSSFCell) cellIter.next();

                    String cellValue = myCell.getCellType() == XSSFCell.CELL_TYPE_NUMERIC ? String.valueOf(myCell.getRawValue()) : myCell.getStringCellValue();
                    switch (myCell.getColumnIndex()){
                        case 0:
                            uploadData.setIrsaliyeId(cellValue);
                            break;
                        case 1:
                            uploadData.setMamulId(cellValue);
                            break;
                        case 2:
                            uploadData.setMiktar(cellValue);
                            break;
                        case 3:
                            uploadData.setNot(cellValue);
                            System.out.println(uploadData);
                    }
                    /*
                    //get cell index
                    System.out.println("Cell column index: " + myCell.getColumnIndex());
                    //get cell type
                    System.out.println("Cell Type: " + myCell.getCellType());

                    //get cell value
                    switch (myCell.getCellType()) {
                        case XSSFCell.CELL_TYPE_NUMERIC :
                            System.out.println("Cell Value: " + myCell.getNumericCellValue());
                            break;
                        case XSSFCell.CELL_TYPE_STRING:
                            System.out.println("Cell Value: " + myCell.getStringCellValue());
                            break;
                        default:
                            System.out.println("Cell Value: " + myCell.getRawValue());
                            break;
                    }
                    System.out.println("---");
                     */
                }
            }
        }
        catch ( Exception e){
            e.printStackTrace();
        }
    }
}