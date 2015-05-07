package me.nithanim.mensaparser;

import java.io.IOException;
import java.util.List;
import me.nithanim.mensaapi.Menu;
import me.nithanim.mensaapi.Type;
import me.nithanim.mensaparser.jku.JkuFactory;
import me.nithanim.mensaparser.khg.KhgFactory;
import me.nithanim.mensaparser.raab.RaabFactory;

public class MainFactory {
    public static List<Menu> newMain(Type type) throws IOException {
        switch(type) {
            case CLASSIC:
            case CHOICE:
                return JkuFactory.newJku(type);
            case KHG:
                return KhgFactory.newKhg();
            case RAAB:
                return RaabFactory.newRaab();
            default:
                throw new IllegalArgumentException("Unknown MensaType!");
        }
    }
}
