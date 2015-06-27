package me.nithanim.mensaparser;

import java.io.IOException;
import java.util.List;
import me.nithanim.mensaapi.Menu;
import me.nithanim.mensaapi.Type;
import me.nithanim.mensaparser.jku.JkuChoiceSourceFactory;
import me.nithanim.mensaparser.jku.JkuChoiceFactory;
import me.nithanim.mensaparser.jku.JkuClassicFactory;
import me.nithanim.mensaparser.jku.JkuClassicSourceFactory;
import me.nithanim.mensaparser.khg.KhgFactory;
import me.nithanim.mensaparser.khg.KhgSourceFactory;
import me.nithanim.mensaparser.raab.RaabFactory;
import me.nithanim.mensaparser.raab.RaabSourceFactory;

public class MainFactory {
    public static List<Menu> newMain(Type type) throws IOException {
        return getFactoryForType(type).newMensa();
    }

    private static MensaFactory getFactoryForType(Type type) {
        switch (type) {
            case CLASSIC:
                return new JkuClassicFactory(new JkuClassicSourceFactory());
            case CHOICE:
                return new JkuChoiceFactory(new JkuChoiceSourceFactory());
            case KHG:
                return new KhgFactory(new KhgSourceFactory());
            case RAAB:
                return new RaabFactory(new RaabSourceFactory());
            default:
                throw new IllegalArgumentException("Unknown MensaType!");
        }
    }
}
