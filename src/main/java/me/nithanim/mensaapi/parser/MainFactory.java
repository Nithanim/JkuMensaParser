package me.nithanim.mensaapi.parser;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import me.nithanim.mensaapi.common.Menu;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class MainFactory {
    public static List<Menu> newMain(MensaType mensaType) throws IOException {
        Document doc;
        Elements offers;
        switch(mensaType) {
            case JKU:
                doc = Jsoup.connect("http://menu.mensen.at/index/print/locid/1").get();
                offers = doc.select("html body div#wrapper div#menu");

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
            case KHG:
                doc = Jsoup.connect("http://www.khg-linz.at/?page_id=379").get();
                offers = doc.select("html body div#total-container div#container div#middle-wrapper div#content div.post_content");
                
            default:
                throw new IllegalArgumentException("Unknown MensaType!");
        }
    }
}
