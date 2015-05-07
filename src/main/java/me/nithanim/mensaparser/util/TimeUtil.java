package me.nithanim.mensaparser.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class TimeUtil {
    private static final DateFormat ISO_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd", Locale.GERMAN);
    
    public static String calendarToIso(Calendar c) {
        return ISO_DATE_FORMAT.format(c.getTime());
    }
    
    public static String dateToIso(Date d) {
        return ISO_DATE_FORMAT.format(d);
    }
    
    private TimeUtil() {
    }
}
