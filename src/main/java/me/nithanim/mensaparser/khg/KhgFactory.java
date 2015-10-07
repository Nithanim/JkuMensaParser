package me.nithanim.mensaparser.khg;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import me.nithanim.mensaapi.Meal;
import me.nithanim.mensaapi.Menu;
import me.nithanim.mensaapi.Type;
import me.nithanim.mensaparser.MensaFactory;
import me.nithanim.mensaparser.MensaParseException;
import me.nithanim.mensaparser.SourceFactory;
import me.nithanim.mensaparser.util.TimeUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class KhgFactory implements MensaFactory {
    private static final String CSS_PATH = "html body div#heightCalcBox.print div#mainBox.print div.container.print div#contentBox.print div.article.list.articleList div.contentSection.firstSection div.listContent div.modTeaser div.swslang";
    private static final Pattern DATE_PATTERN = Pattern.compile("^[^\\d]+(\\d{1,2})\\.(\\d{1,2})\\.[\\-– ]+\\d{1,2}\\.\\d{1,2}\\.(\\d{4}) *$");
    private static final String[] MENU_NAMES = {"MENÜ 1", "MENÜ 2"};

    private final SourceFactory sourceFactory;

    public KhgFactory(SourceFactory sourceFactory) {
        this.sourceFactory = sourceFactory;
    }

    @Override
    public List<Menu> newMensa(Collection<MensaParseException> exceptions) throws IOException { //TODO handle exceptions
        Document doc = sourceFactory.getAsHtml();
        Element content = doc.select(CSS_PATH).first();

        Elements metas = content.select(">p");

        List<Menu> menus = new ArrayList<Menu>();

        Calendar baseDate = parseDate(metas.get(3).textNodes().get(1).text());
        Calendar currDate = (Calendar) baseDate.clone();
        int counter = 0;
        for (Element e : content.select(">table > tbody > tr")) {
            Elements childs = e.select(">td");

            if (childs.size() == 4) {
                currDate.set(Calendar.DAY_OF_MONTH, getDayOfWeekOffset(childs.remove(0).text()));
            }

            menus.add(parseMenu(MENU_NAMES[counter], childs, TimeUtil.calendarToIso(currDate)));
            counter = (counter + 1) % 2;
        }

        return menus;
    }

    private static Menu parseMenu(String name, Elements es, String date) {
        List<Meal> meals = parseMeals(es.get(0).text());
        int priceFull;
        try {
            String priceRaw = es.get(2).text().trim();
            priceFull = Integer.parseInt(priceRaw.replace(",", ""));
        } catch (NumberFormatException ex) {
            priceFull = -2;
        }
        int priceCheap;
        try {
            String priceRaw = es.get(1).text().trim();
            priceCheap = Integer.parseInt(priceRaw.replace(",", ""));
        } catch (NumberFormatException ex) {
            priceCheap = -2;
        }

        return new Menu(Type.KHG, name, meals, priceFull, priceFull - priceCheap, date, false);
    }

    private static List<Meal> parseMeals(String text) {
        String[] raw = text.split(", ", 2);
        List<Meal> result = new ArrayList<Meal>(raw.length);
        for (String meal : raw) {
            result.add(new Meal(meal, -1));
        }
        return result;
    }

    private static Calendar parseDate(String s) {
        Matcher matcher = DATE_PATTERN.matcher(s);
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+1:00"));
        if (matcher.matches()) {
            int y = Integer.parseInt(matcher.group(3));
            int m = Integer.parseInt(matcher.group(2));
            int d = Integer.parseInt(matcher.group(1));
            calendar.set(y, m - 1, d);
            return calendar;
        } else {
            return null;
        }
    }

    private static int getDayOfWeekOffset(String dow) {
        if ("MO".equals(dow)) {
            return 0;
        } else if ("DI".equals(dow)) {
            return 1;
        } else if ("MI".equals(dow)) {
            return 2;
        } else if ("DO".equals(dow)) {
            return 3;
        } else if ("FR".equals(dow)) {
            return 4;
        } else if ("SA".equals(dow)) {
            return 5;
        }
        throw new IllegalArgumentException("Unable to parse date!");
    }
}
