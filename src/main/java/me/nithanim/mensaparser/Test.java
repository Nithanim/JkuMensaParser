package me.nithanim.mensaparser;

import java.io.IOException;
import java.util.List;
import me.nithanim.mensaapi.Menu;
import me.nithanim.mensaapi.Type;

public class Test {
    public static void main(String[] args) throws IOException {
        List<Menu> menus = MainFactory.newMain(Type.CLASSIC);
        menus.addAll(MainFactory.newMain(Type.CHOICE));
        menus.addAll(MainFactory.newMain(Type.KHG));
        menus.addAll(MainFactory.newMain(Type.RAAB));
        for(Menu m : menus) {
            System.out.println(m.toString());
        }
    }
}
