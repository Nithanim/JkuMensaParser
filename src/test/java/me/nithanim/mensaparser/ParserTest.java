package me.nithanim.mensaparser;

import java.io.IOException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import me.nithanim.mensaapi.Menu;
import org.junit.Before;

public class ParserTest {
    protected ResourceSourceFactory sourceFactory;
    protected MensaFactory mensaFactory;
    protected Collection<MensaParseException> exceptions;

    @Before
    public void setUp() {
        sourceFactory.setFile(null);
        exceptions = new LinkedList<MensaParseException>();
    }

    public ResourceSourceFactory getSourceFactory() {
        return sourceFactory;
    }

    public List<Menu> newMensa() throws IOException {
        return mensaFactory.newMensa(exceptions);
    }
}
