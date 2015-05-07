package me.nithanim.mensaparser.jku;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import me.nithanim.mensaparser.MensaParseException;
import me.nithanim.mensaapi.Meal;
import me.nithanim.mensaapi.Menu;
import me.nithanim.mensaapi.Type;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class JkuClassicMenuFactory {
    public static Menu newMenu(String name, Element a, int price, int oehBonus) {
        Elements es = a.select(">td");
        
        String date = es.get(0).text();
        try {
            date = Util.parseTimeClassic(date);
        } catch(ParseException ex) {
            throw new MensaParseException("Unable to parse date \"" + date + "\" for Classic!");
        }
        
        List<Meal> courses = new ArrayList<Meal>(3);
        for(Element e : es.get(1).select(">p")) {
            String meal = e.text().replaceAll(",$", "");
            courses.add(new Meal(meal, -1));
        }
        
        boolean isVegetarian = isVegetarian(es.get(2));
        
        
        if(courses.get(1).getDesc().contains("Geschlossen")) {
            return null;
        } else {
            return new Menu(Type.CLASSIC, name, courses, price, oehBonus, date, isVegetarian);
        }
    }
    
    private static boolean isVegetarian(Element e) {
        Element img = e.select("img").first();
        if(img != null) {
            return img.attr("alt").startsWith("Vegetar");
        } else {
            return false;
        }
    }
}
