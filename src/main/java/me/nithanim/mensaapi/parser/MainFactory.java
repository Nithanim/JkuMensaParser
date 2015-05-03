package me.nithanim.mensaapi.parser;

import java.io.IOException;
import java.util.List;
import me.nithanim.mensaapi.common.Menu;
import me.nithanim.mensaapi.parser.jku.JkuFactory;
import me.nithanim.mensaapi.parser.khg.KhgFactory;

public class MainFactory {
    public static List<Menu> newMain(MensaType mensaType) throws IOException {
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
