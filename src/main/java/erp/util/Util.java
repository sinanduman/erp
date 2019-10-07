package erp.util;

import erp.model.Aylar;
import erp.entity.Genel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

public class Util {

    public static long nanoTime = System.nanoTime();

    private static final Logger logger = LoggerFactory.getLogger(Util.class);

    public static String mamulInfoDecompose(String bileseninfo, int type) {
        if (bileseninfo == null)
            return null;
        else {
            String[] returnValue = bileseninfo.split("\\|");
            // 1: mamul ad, 2: mamul kod, 3: mamul agirlik 4: GKR No
            return returnValue[type - 1].toString();
        }
    }

    public static String splitLine(String line, String delimeter, String newLineChar) {
        String[] tempArray = line.split(delimeter);

        String retVal = "";
        String newLine = "";
        for (String word : tempArray) {
            retVal = retVal + newLine + word;
            newLine = newLineChar;
        }
        if (retVal.equals("-1")) {
            retVal = "-";
        }
        return retVal;
    }

    public static String date_tr_to_month(String dateinfo) {
        String returnValue = dateinfo;
        Integer tmp = 1;
        if (returnValue.matches("[0-9]{2}-[0-9]{2}-[0-9]{4}")) {
            tmp = Integer.valueOf(dateinfo.substring(3, 5));
            logger.info("tmp :" + tmp);
            for (Aylar a : Aylar.values()) {
                if (a.value() == tmp) {
                    returnValue = a.tr();
                }
            }
        }
        return returnValue;
    }

    public static String date_tr_to_eng(String dateinfo) {
        logger.info("Before: " + dateinfo);
        String returnValue = dateinfo;
        if (returnValue.matches("[0-9]{2}-[0-9]{2}-[0-9]{4}")) {
            returnValue = dateinfo.substring(6) + "-" + dateinfo.substring(3, 5) + "-" + dateinfo.substring(0, 2);
        }
        logger.info("After: " + dateinfo);
        return returnValue;
    }

    public static String date_eng_to_tr(String dateinfo) {
        String returnValue = dateinfo;
        if (returnValue.matches("[0-9]{4}-[0-9]{2}-[0-9]{2}")) {
            returnValue = dateinfo.substring(8) + "-" + dateinfo.substring(5, 7) + "-" + dateinfo.substring(0, 4);
        }
        return returnValue;
    }

    public static boolean isNumeric(String str) {
        boolean returnValue = false;
        if (str.matches("[0-9]+")) {
            returnValue = true;
        }
        return returnValue;
    }

    public static boolean isPositive(String str) {
        boolean returnValue = false;
        if (str.matches("[1-9][0-9]*")) {
            returnValue = true;
        }
        return returnValue;
    }

    public static boolean isDate(String str) {
        boolean returnValue = false;
        if (str.matches("[0-9]{4}-[0-9]{2}-[0-9]{2}") || str.matches("[0-9]{2}-[0-9]{2}-[0-9]{4}")) {
            returnValue = true;
        }
        return returnValue;
    }

    public static long getToken() {
        return System.nanoTime();
    }

    public static Timestamp date_tr_to_timestamp(String dateinfo) {

        Timestamp returnValue = null;
        DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        Date date = new Date();
        try {
            date = df.parse(dateinfo);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        returnValue = new Timestamp(date.getTime());
        return returnValue;
    }

    public static String getTarih(Date tarih) {
        return (tarih != null) ? (new SimpleDateFormat("yyyy-MM-dd").format(tarih)) : "";
    }

    public static String getTarihTR(Date tarih) {
        return (tarih != null) ? (new SimpleDateFormat("dd-MM-yyyy").format(tarih)) : "";
    }

    public static String getTarihTRShort(Date tarih) {
        return (tarih != null) ? (new SimpleDateFormat("dd.MM.yy").format(tarih)) : "";
    }

    public static double Round(int pay, int payda) {
        double temp = (pay) * 1.0 / payda;
        temp = ((double) ((int) (temp * 100))) / 100;
        return temp;
    }

    public static double Round(double deger, double ondalik) {
        return Math.round(deger * Math.pow(10, ondalik)) / Math.pow(10, ondalik);
    }

    public static boolean isNotEmptyOrNull(Object o) {
        return o != null && !o.equals("");
    }

    public static String gkrnoNumberCheck(String gkrno) {
        if (isPositive(gkrno)) {
            return gkrno;
        } else {
            return null;
        }
    }

    public static String getPageFilter(int offset, Map<String, String> param) {
        int rowperpage = (param == null || param.get("rowperpage") == null) ? Genel.ROWPERPAGE : Integer.valueOf(param.get("rowperpage"));
        return (offset == 0) ? "" : " offset " + (offset - 1) * rowperpage + " limit " + rowperpage;
    }

    public static String getPageFilter(int offset){
        return getPageFilter(offset, null);
    }

    public static String md5Dao(String temp) {
        String digest = temp;
        if (temp != null) {
            MessageDigest md;
            try {
                md = MessageDigest.getInstance("MD5");
                md.update(temp.getBytes());
                BigInteger hash = new BigInteger(1, md.digest());
                digest = hash.toString(16);
                while (digest.length() < 32) { // 40 for SHA-1
                    digest = "0" + digest;
                }
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
        }
        return digest;
    }

    public static String nullToEmpty(String val){
        if (val == null)
            return "";
        else
            return val;
    }
}