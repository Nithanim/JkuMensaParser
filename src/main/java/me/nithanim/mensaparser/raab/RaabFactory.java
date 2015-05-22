package me.nithanim.mensaparser.raab;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ListIterator;
import java.util.Locale;
import me.nithanim.mensaparser.MensaParseException;
import me.nithanim.mensaapi.Meal;
import me.nithanim.mensaapi.Menu;
import me.nithanim.mensaapi.Type;
import me.nithanim.mensaparser.SourceFactory;
import me.nithanim.mensaparser.util.TimeUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;

public class RaabFactory {
    private static final SimpleDateFormat TIME_FORMAT = new SimpleDateFormat("d. MMMM yyyy", Locale.GERMAN);
    private final SourceFactory sourceFactory;

    public RaabFactory(SourceFactory sourceFactory) {
        this.sourceFactory = sourceFactory;
    }
    
    public List<Menu> newRaab() throws IOException {
        Document doc = sourceFactory.getAsHtml();
        Elements trs = doc.select("html body div#content table tbody tr td table#speiseplan tbody tr");
        
        ArrayList<Menu> menus = new ArrayList<Menu>();
        ListIterator<Element> it = trs.listIterator();
        while(it.hasNext()) {
            String date = parseDate(it.next().text());
            
            if(it.hasNext()) {
                Element tr = it.next();
                menus.addAll(parseMenus(tr.select("span").first(), date));
            }
        }
        return menus;
    }
    
    private static List<Menu> parseMenus(Element e, String date) {
        ArrayList<Menu> menus = new ArrayList<Menu>();
        
        ListIterator<TextNode> it = e.textNodes().listIterator();
        while(it.hasNext()) {
            menus.add(parseSingleMenu(it, date));
        }
        return menus;
    }
    
    private static Menu parseSingleMenu(ListIterator<TextNode> it, String date) {
        String title = tidyTitle(it.next().text().trim());
        
        if(!it.hasNext()) { // -> full day only has one element -> closed
            List<Meal> meals = new ArrayList<Meal>(1);
            meals.add(new Meal(title, -2));
            return new Menu(Type.RAAB, "", meals, -1, 0, date, false);
        }
        
        ArrayList<Meal> meals = new ArrayList<Meal>(4);
        while(true) {
            TextNode next = it.next();
            
            String content = next.text().trim();
            if(content.length() > 0) {
                meals.add(new Meal(content, -1));
            }
            
            if(!it.hasNext()) {
                break;
            } else if(it.next().text().trim().startsWith("MENÜ")) { //peek next element
                it.previous(); //step back in EVERY case
                break;
            }
            it.previous(); //step back
        }
        return new Menu(Type.RAAB, title, meals, -1, 0, date, false);
    }
    
    private static String tidyTitle(String title) {
        StringBuilder sb = new StringBuilder(title.toLowerCase());
        sb.setCharAt(0, Character.toUpperCase(sb.charAt(0)));
        sb.setLength(sb.length() - 1);
        return sb.toString();
    }
    
    private static String parseDate(String rawDate) {
        try {
            Date d = TIME_FORMAT.parse(rawDate.split(" ", 2)[1]);
            return TimeUtil.dateToIso(d);
        } catch(ParseException ex) {
            throw new MensaParseException(ex);
        }
    }
}
