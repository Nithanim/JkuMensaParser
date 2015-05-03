package me.nithanim.mensaapi.parser;

import me.nithanim.mensaapi.parser.jku.JkuPlanFactory;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import me.nithanim.mensaapi.common.Menu;
import me.nithanim.mensaapi.parser.jku.JkuFactory;
import me.nithanim.mensaapi.parser.khg.KhgFactory;
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
                return JkuFactory.newJku();
            case KHG:
                return KhgFactory.newKhg();
            default:
                throw new IllegalArgumentException("Unknown MensaType!");
        }
    }
}
