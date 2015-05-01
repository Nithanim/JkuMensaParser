package me.nithanim.mensaapi.classic;

import java.util.LinkedList;
import java.util.List;
import me.nithanim.mensaapi.common.Menu;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class MainFactory {
    public static List<Menu> newMain(Document doc) {
        Elements offers = doc.select("html body div#wrapper div#menu");
        
        List<Menu> menus = new LinkedList<Menu>();
        for(Element offer : offers) {  //Classic 1, Classic 2, Choice, M-Cafe, ...
            try {
                menus.addAll(PlanFactory.newMenus(offer));
            } catch(IllegalArgumentException ex) {
                //Ignore all but Classic and Choice for now
                //System.out.println(ex.getMessage());
            }
        }
        
        return menus;
    }
}
