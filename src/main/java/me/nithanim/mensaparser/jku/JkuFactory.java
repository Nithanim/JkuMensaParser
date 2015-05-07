package me.nithanim.mensaparser.jku;

import java.io.IOException;
import java.util.List;
import me.nithanim.mensaapi.Menu;
import me.nithanim.mensaapi.Type;
import me.nithanim.mensaparser.MensaParseException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class JkuFactory {

    public static List<Menu> newJku(Type type) throws IOException {
        if(!(type == Type.CLASSIC || type == Type.CHOICE)) {
            throw new IllegalArgumentException("Not responsible for " + type);
        }
        
        String beginMatch = type == Type.CLASSIC ? "Menü Classic" : "Choice";
        
        Document doc = Jsoup.connect("http://menu.mensen.at/index/print/locid/1").get();
        Elements offers = doc.select("html body div#wrapper div#menu");
        
        for(Element offer : offers) {
            if(offer.select(">h2").first().text().startsWith(beginMatch)) {
                return JkuOfferFactory.newMenus(offer);
            }
        }
        throw new MensaParseException("Unable to locate anchor for " + type);
    }
    
}
