package me.nithanim.mensaapi;

import java.io.IOException;
import java.util.List;
import me.nithanim.mensaapi.parser.MainFactory;
import me.nithanim.mensaapi.common.Menu;
import me.nithanim.mensaapi.parser.MensaType;

public class Test {
    public static void main(String[] args) throws IOException {
        List<Menu> menus = MainFactory.newMain(MensaType.JKU);
        menus.addAll(MainFactory.newMain(MensaType.KHG));
        for(Menu m : menus) {
            System.out.println(m.toString());
        }
    }
}
