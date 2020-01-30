package erp.util;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public interface Constants {
    String COMMA_SEPERATED = "###,###.##";
    Locale customLocale = new Locale("tr", "TR");
    DecimalFormat decimalFormat = new DecimalFormat(COMMA_SEPERATED, DecimalFormatSymbols.getInstance(customLocale));
}
