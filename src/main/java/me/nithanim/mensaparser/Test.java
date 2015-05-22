package me.nithanim.mensaparser;

import java.io.IOException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import me.nithanim.mensaapi.Menu;
import me.nithanim.mensaapi.Type;

public class Test {
    public static void main(String[] args) throws IOException {
        List<Menu> menus = new LinkedList<Menu>();
        menus.addAll(call(Type.CLASSIC));
        menus.addAll(call(Type.CHOICE));
        menus.addAll(call(Type.KHG));
        menus.addAll(call(Type.RAAB));
        for(Menu m : menus) {
            System.out.println(m.toString());
        }
    }
    
    private static List<Menu> call(Type t) {
        try {
            return MainFactory.newMain(t);
        } catch(Exception ex) {
            ex.printStackTrace();
        }
        return Collections.EMPTY_LIST;
    }
}
