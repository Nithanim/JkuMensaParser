package me.nithanim.mensaapi.parser;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public final class Util {
    private static final SimpleDateFormat TIME_FORMAT_CLASSIC = new SimpleDateFormat("dd.MM.");
    private static final SimpleDateFormat TIME_FORMAT_CHOICE = new SimpleDateFormat("dd. MMMM yyyy");
    private static final SimpleDateFormat TIME_FORMAT_ISO = new SimpleDateFormat("yyyy-MM-dd");
    
    public static String parseTimeClassic(String time) throws ParseException {
        try {
            Date t = TIME_FORMAT_CLASSIC.parse(time.split(" ", 2)[1]);
            t.setYear(getCurrentYear());
            return convertToIsoDate(t);
        } catch(ParseException ex) {
            throw ex;
        } catch(Exception ex) {
            throw new ParseException("Complete miss in parsing", -1);
        }
    }
    
    public static String parseTimeChoice(String time) throws ParseException {
        try {
            Date t = TIME_FORMAT_CHOICE.parse(time.split(", ", 2)[1]);
            return convertToIsoDate(t);
        } catch(ParseException ex) {
            throw ex;
        } catch(Exception ex) {
            throw new ParseException("Complete miss in parsing", -1);
        }
    }
    
    private static String convertToIsoDate(Date d) {
        return TIME_FORMAT_ISO.format(d);
    }
    
    private static int getCurrentYear() {
        return new Date().getYear();
    }
}
