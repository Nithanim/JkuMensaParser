package me.nithanim.mensaparser.jku;

import java.io.IOException;
import java.util.List;
import me.nithanim.mensaapi.Menu;
import me.nithanim.mensaapi.Type;
import me.nithanim.mensaparser.MensaParseException;
import me.nithanim.mensaparser.SourceFactory;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class JkuFactory {
    private final SourceFactory sourceFactory;
    
    public JkuFactory(SourceFactory sourceFactory) {
        this.sourceFactory = sourceFactory;
    }

    public List<Menu> newJku(Type type) throws IOException {
        if(!(type == Type.CLASSIC || type == Type.CHOICE)) {
            throw new IllegalArgumentException("Not responsible for " + type);
        }
        
        String beginMatch = type == Type.CLASSIC ? "Menü Classic" : "Choice";
        
        Document doc = sourceFactory.getAsHtml();
        Elements offers = doc.select("html body div#wrapper div#menu");
        
        for(Element offer : offers) {
            if(offer.select(">h2").first().text().startsWith(beginMatch)) {
                return JkuOfferFactory.newMenus(offer);
            }
        }
        throw new MensaParseException("Unable to locate anchor for " + type);
    }
    
}
