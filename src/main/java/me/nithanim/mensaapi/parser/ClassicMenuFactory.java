package me.nithanim.mensaapi.parser;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import me.nithanim.mensaapi.common.Meal;
import me.nithanim.mensaapi.common.Menu;
import me.nithanim.mensaapi.common.Type;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class ClassicMenuFactory {
    public static Menu newMenu(Element a, int price) {
        Elements es = a.select(">td");
        
        String date = es.get(0).text();
        try {
            date = Util.parseTimeClassic(date);
        } catch(ParseException ex) {
            System.err.println("Unable to parse " + date + " for Classic!"); //print to err for now
        }
        
        List<Meal> courses = new ArrayList<Meal>(3);
        for(Element e : es.get(1).select(">p")) {
            String name = e.text().replaceAll(",$", "");
            courses.add(new Meal(name, -1));
        }
        
        boolean isVegetarian = isVegetarian(es.get(2));
        
        
        if(courses.get(1).getDesc().contains("Geschlossen")) {
            return null;
        } else {
            return new Menu(Type.CLASSIC, null, courses, price, date, isVegetarian);
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
