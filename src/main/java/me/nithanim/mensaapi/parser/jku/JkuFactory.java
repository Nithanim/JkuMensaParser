package me.nithanim.mensaapi.parser.jku;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import me.nithanim.mensaapi.common.Menu;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class JkuFactory {

    public static List<Menu> newJku() throws IOException {
        Document doc = Jsoup.connect("http://menu.mensen.at/index/print/locid/1").get();
        Elements offers = doc.select("html body div#wrapper div#menu");

        List<Menu> menus = new LinkedList<Menu>();
        for(Element offer : offers) {  //Classic 1, Classic 2, Choice, M-Cafe, ...
            try {
                menus.addAll(JkuOfferFactory.newMenus(offer));
            } catch(IllegalArgumentException ex) {
                //Ignore all but Classic and Choice for now
                //System.out.println(ex.getMessage());
            }
        }
        return menus;
    }
    
}
