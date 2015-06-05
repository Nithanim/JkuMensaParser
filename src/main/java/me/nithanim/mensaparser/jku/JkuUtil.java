package me.nithanim.mensaparser.jku;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import me.nithanim.mensaparser.MensaParseException;

public final class JkuUtil {
    private static final SimpleDateFormat TIME_FORMAT_CLASSIC = new SimpleDateFormat("dd.MM.", Locale.GERMAN);
    private static final SimpleDateFormat TIME_FORMAT_CHOICE = new SimpleDateFormat("dd. MMMM yyyy", Locale.GERMAN);
    private static final SimpleDateFormat TIME_FORMAT_ISO = new SimpleDateFormat("yyyy-MM-dd");
    
    public static String parseTimeClassic(String time) throws MensaParseException {
        try {
            Date t = TIME_FORMAT_CLASSIC.parse(time.split(" ", 2)[1]);
            t.setYear(getCurrentYear());
            return convertToIsoDate(t);
        } catch(Exception ex) {
            throw new MensaParseException("Unable to parse \"" + time + "\"");
        }
    }
    
    public static String parseTimeChoice(String time) throws MensaParseException {
        try {
            Date t = TIME_FORMAT_CHOICE.parse(time.split(", ", 2)[1]);
            return convertToIsoDate(t);
        } catch(Exception ex) {
            throw new MensaParseException("Unable to parse \"" + time + "\"");
        }
    }
    
    private static String convertToIsoDate(Date d) {
        return TIME_FORMAT_ISO.format(d);
    }
    
    private static int getCurrentYear() {
        return new Date().getYear();
    }
}
