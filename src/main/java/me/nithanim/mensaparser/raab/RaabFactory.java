package me.nithanim.mensaparser.raab;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import me.nithanim.mensaparser.MensaParseException;
import me.nithanim.mensaapi.Meal;
import me.nithanim.mensaapi.Menu;
import me.nithanim.mensaapi.Type;
import me.nithanim.mensaparser.MensaFactory;
import me.nithanim.mensaparser.SourceFactory;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class RaabFactory implements MensaFactory {
    private static final SimpleDateFormat TIME_FORMAT = new SimpleDateFormat("EEEE, d. MMMM yyyy", Locale.GERMAN);
    private static final SimpleDateFormat TIME_FORMAT_ISO = new SimpleDateFormat("yyyy-MM-dd");
    private final SourceFactory sourceFactory;

    public RaabFactory(SourceFactory sourceFactory) {
        this.sourceFactory = sourceFactory;
    }

    @Override
    public List<Menu> newMensa(Collection<MensaParseException> exceptions) throws IOException { //TODO handle exceptions
        Document doc = sourceFactory.getAsHtml();
        Elements ps = doc.select("#speiseplan_text").first().select(">p");
        filterEmpty(ps);

        Date date = null;
        String title = null;
        List<Meal> meals = null;
        List<Menu> menus = new ArrayList<Menu>();
        for (Element p : ps) {
            String text = p.text();
            if (!p.select(">strong").isEmpty()) {
                try {
                    date = TIME_FORMAT.parse(text);
                } catch (ParseException ex) {
                    throw new MensaParseException("Unable to parse date", ex, p);
                }
            } else if (text.startsWith("MENÜ")) {
                if (meals != null) { // a menu was parsed before
                    menus.add(new Menu(Type.RAAB, title, meals, -1, -1, TIME_FORMAT_ISO.format(date), false));
                }
                title = text;
                meals = new ArrayList<Meal>();
            } else {
                if (meals != null) {
                    meals.add(new Meal(text, -1));
                } else {
                    throw new MensaParseException("Tried to parse a meal before a menu was found!");
                }
            }
        }
        return menus;
    }

    private void filterEmpty(Elements es) {
        Iterator<Element> it = es.iterator();
        while (it.hasNext()) {
            Element e = it.next();
            if (e.text().replace("\u00a0" /*&nbsp*/, " ").trim().equals("")) {
                it.remove();
            }
        }
    }
}
