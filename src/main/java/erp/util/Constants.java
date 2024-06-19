package erp.util;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public final class Constants {
    private Constants() {
    }

    public static final String COMMA_SEPERATED = "###,###.##";
    public static final String LOG_FORMAT = "{} - {}ms.";
    public static final Locale customLocale = new Locale("tr", "TR");
    public static final DecimalFormat decimalFormat = new DecimalFormat(COMMA_SEPERATED, DecimalFormatSymbols.getInstance(customLocale));
}
