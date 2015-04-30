package me.nithanim.mensaapi.classic;

import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import me.nithanim.mensaapi.MensaParseException;
import me.nithanim.mensaapi.common.Meal;
import me.nithanim.mensaapi.common.Menu;
import me.nithanim.mensaapi.common.Type;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;

public class ChoiceMenuFactory {
    private static final Pattern PATTERN = Pattern.compile("^([\\D]+)(\\d{1,2},\\d\\d) [A-Za-z]+$");
    
    public static List<Menu> newMenu(Element e) {
        Elements subtags = e.select("p");
        
        String date = subtags.remove(0).text();
        
        List<Menu> menus = new LinkedList<Menu>();
        for(Element menuElement : subtags) {
            if(!isEmpty(menuElement)) {
                try {
                    String subtype = getSubtype(menuElement);
                    
                    replaceAllImages(menuElement);
                    
                    List<Meal> meals = new LinkedList<Meal>();
                    for(TextNode tn : menuElement.textNodes()) {
                        String raw = tn.text().replace("\u00a0" /*&nbsp*/, " ").trim();
                        Matcher matcher = PATTERN.matcher(raw);
                        
                        String desc;
                        int price;
                        if(matcher.matches()) {
                            desc = matcher.group(1).trim();
                            String priceRaw = matcher.group(2).trim();
                            price = Integer.parseInt(priceRaw.replace(",", ""));
                        } else {
                            if(raw.matches("-+")) { //subtype closed
                                continue;
                            } else {
                                //throw new MensaParseException("Unable to parse " + menuElement.toString());
                                desc = raw;
                                price = -2;
                            }
                        }
                        meals.add(new Meal(desc, price));
                    }
                    
                    menus.add(new Menu(Type.CHOICE, subtype, meals, -1, date, false)); //TODO Veggie
                } catch(NumberFormatException ex) {
                    ex.printStackTrace();
                }
            }
        }
        return menus;
    }

    private static boolean isEmpty(Element menuElement) {
        return menuElement.textNodes().isEmpty()
                || (menuElement.textNodes().size() == 1 && menuElement.textNodes().get(0).text().equals("\u00a0"));
    }
    
    private static String getSubtype(Element elem) {
        StringBuilder sb = new StringBuilder();
        Element currElem = elem.select("> strong").first();
        while(currElem != null && currElem.tagName().equals("strong")) {
            sb.append(currElem.text());
            currElem = currElem.nextElementSibling();
        }
        if(sb.length() == 0) {
            throw new MensaParseException();
        }
        return sb.toString().replace("\u00a0" /*&nbsp*/, "").trim();
    }

    private static void replaceAllImages(Element elem) {
        Elements imgs = elem.select("img");
        for(Element img : imgs) {
            StringBuilder sb = new StringBuilder();
            if(img.previousSibling() instanceof TextNode) {
                TextNode s = (TextNode)img.previousSibling();
                sb.append(s.text());
                sb.append(' ');
                s.remove();
            }
            //String imgalt = img.attr("alt");
            //sb.append(imgalt.startsWith("Veg") ? "(vegetarisch)" : "(Fisch)"); ignore for now
            if(img.nextSibling() instanceof TextNode) {
                TextNode s = (TextNode)img.nextSibling();
                sb.append(' ');
                sb.append(s.text());
                s.remove();
            }
            
            img.replaceWith(new TextNode(sb.toString(), ""));
        }
    }
}
