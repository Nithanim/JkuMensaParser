package me.nithanim.mensaapi.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class TimeUtil {
    private static final DateFormat ISO_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
    
    public static String calendarToIso(Calendar c) {
        return ISO_DATE_FORMAT.format(c.getTime());
    }
    
    private TimeUtil() {
    }
}
