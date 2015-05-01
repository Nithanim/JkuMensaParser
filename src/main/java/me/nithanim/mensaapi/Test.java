package me.nithanim.mensaapi;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.IOException;
import java.util.List;
import me.nithanim.mensaapi.parser.MainFactory;
import me.nithanim.mensaapi.common.Menu;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class Test {
    public static void main(String[] args) throws IOException {
        Document doc = Jsoup.connect("http://menu.mensen.at/index/print/locid/1").get();
        List<Menu> menus = MainFactory.newMain(doc);
        printJson(menus);
    }
    
    private static void printToString(List<Menu> menus) {
        for(Menu m : menus) {
            System.out.println(m.toString());
        }
    }
    
    private static void printJson(List<Menu> menus) {
        Gson gson = new GsonBuilder().create();
        System.out.println(gson.toJson(menus));
    }
    
}
