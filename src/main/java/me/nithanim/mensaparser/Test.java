package me.nithanim.mensaparser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import me.nithanim.mensaapi.Menu;
import me.nithanim.mensaapi.Type;

public class Test {
    public static void main(String[] args) throws IOException {
        Collection<MensaParseException> exceptions = new ArrayList<MensaParseException>();
        List<Menu> menus = new LinkedList<Menu>();
        menus.addAll(call(Type.CLASSIC, exceptions));
        menus.addAll(call(Type.CHOICE, exceptions));
        menus.addAll(call(Type.KHG, exceptions));
        menus.addAll(call(Type.RAAB,exceptions));
        for (Menu m : menus) {
            System.out.println(m.toString());
        }
        for(MensaParseException ex : exceptions) {
            ex.printStackTrace();
        }
    }

    private static List<Menu> call(Type t, Collection<MensaParseException> exceptions) {
        try {
            return MainFactory.newMain(t, exceptions);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return Collections.EMPTY_LIST;
    }
}
