package me.nithanim.mensaparser;

import java.io.IOException;
import java.util.List;
import me.nithanim.mensaapi.Menu;
import me.nithanim.mensaapi.Type;
import me.nithanim.mensaparser.jku.JkuChoiceHtmlSourceFactory;
import me.nithanim.mensaparser.jku.JkuChoiceFactory;
import me.nithanim.mensaparser.jku.JkuFactory;
import me.nithanim.mensaparser.jku.JkuClassicHtmlSourceFactory;
import me.nithanim.mensaparser.khg.KhgFactory;
import me.nithanim.mensaparser.khg.KhgHtmlSourceFactory;
import me.nithanim.mensaparser.raab.RaabFactory;
import me.nithanim.mensaparser.raab.RaabHtmlSourceFactory;

public class MainFactory {
    public static List<Menu> newMain(Type type) throws IOException {
        switch(type) {
            case CLASSIC:
                return new JkuFactory(new JkuClassicHtmlSourceFactory()).newJku(type);
            case CHOICE:
                return new JkuChoiceFactory(new JkuChoiceHtmlSourceFactory()).newMenu();
            case KHG:
                return new KhgFactory(new KhgHtmlSourceFactory()).newKhg();
            case RAAB:
                return new RaabFactory(new RaabHtmlSourceFactory()).newRaab();
            default:
                throw new IllegalArgumentException("Unknown MensaType!");
        }
    }
}
