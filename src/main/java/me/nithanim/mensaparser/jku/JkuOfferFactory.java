package me.nithanim.mensaparser.jku;

import java.util.ArrayList;
import java.util.List;
import me.nithanim.mensaapi.Menu;
import org.jsoup.nodes.Element;

public class JkuOfferFactory {
    public static List<Menu> newMenus(Element e) {
        String name = e.select(">h2").text();
        
        if(name.startsWith("Menü Classic")) {
            int price = name.endsWith("1") ? 365 : 500;
            int oehBonus = name.endsWith("1") ? 115 : 80;
            
            List<Menu> menus = new ArrayList<Menu>(5);
            for(Element menuItem : e.select(">.menu-item > table > tbody > tr")) {
                Menu menu = JkuClassicMenuFactory.newMenu(name, menuItem, price, oehBonus);
                if(menu != null) {
                    menus.add(menu);
                }
            }
            return menus;
        }
        throw new IllegalArgumentException("Unknown Menu");
    }
}
