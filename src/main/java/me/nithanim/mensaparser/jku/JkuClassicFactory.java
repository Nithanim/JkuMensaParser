package me.nithanim.mensaparser.jku;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import me.nithanim.mensaapi.Meal;
import me.nithanim.mensaapi.Menu;
import me.nithanim.mensaapi.Type;
import me.nithanim.mensaparser.MensaFactory;
import me.nithanim.mensaparser.SourceFactory;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class JkuClassicFactory implements MensaFactory {
    private final SourceFactory sourceFactory;

    public JkuClassicFactory(SourceFactory sourceFactory) {
        this.sourceFactory = sourceFactory;
    }

    @Override
    public List<Menu> newMensa() throws IOException {
        Document doc = sourceFactory.getAsHtml();

        List<Menu> menus = new ArrayList<Menu>(2 * 5);
        Elements offers = doc.select("item");
        for (Element offer : offers) {
            String title = offer.select("> title").text();
            if (title.startsWith("Menü Classic")) {
                int price = title.endsWith("1") ? 365 : 500;
                int oehBonus = title.endsWith("1") ? 115 : 80;
                String menuesAsXmlString = offer.select("> description").first().text();
                menus.addAll(parseMenus(Jsoup.parse(menuesAsXmlString), title, price, oehBonus));
            }
        }
        return menus;
    }

    private ArrayList<Menu> parseMenus(Document domMenus, String title, int price, int oehBonus) {
        ArrayList<Menu> menus = new ArrayList<Menu>(5);

        for (Element rawMenu : domMenus.getElementsByTag("menu")) {
            String rawDate = rawMenu.getElementsByTag("day").first().text();
            String date;
            date = JkuUtil.parseTimeClassic(rawDate);

            List<Meal> meals = new ArrayList<Meal>(3);
            for (Element rawMeal : rawMenu.getElementsByTag("p")) {
                String desc = rawMeal.text().replaceAll("[\\s\\,]+$", "");
                meals.add(new Meal(desc, -1));
            }
            menus.add(new Menu(Type.CLASSIC, title, meals, price, oehBonus, date, false));
        }
        return menus;
    }
}
