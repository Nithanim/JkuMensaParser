package me.nithanim.mensaapi.parser.khg;

import java.io.IOException;
import java.util.List;
import me.nithanim.mensaapi.common.Menu;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class KhgFactory {

    public static List<Menu> newKhg() throws IOException {
        Document doc = Jsoup.connect("http://www.khg-linz.at/?page_id=379").get();
        Elements offers = doc.select("html body div#total-container div#container div#middle-wrapper div#content div.post_content");
        
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
