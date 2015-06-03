package me.nithanim.mensaparser;

import java.io.IOException;
import java.util.List;
import me.nithanim.mensaapi.Menu;

public interface MensaFactory {
    List<Menu> newMensa() throws IOException;
}
