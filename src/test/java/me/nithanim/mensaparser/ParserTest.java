package me.nithanim.mensaparser;

import java.io.IOException;
import java.util.List;
import me.nithanim.mensaapi.Menu;
import me.nithanim.mensaparser.jku.JkuChoiceFactory;
import org.junit.Before;

public class ParserTest {
    private final ResourceSourceFactory sourceFactory;
    private final JkuChoiceFactory factory;

    @Before
    public void setUp() {
        sourceFactory.setFile(null);
    }

    public ParserTest(String folder) {
        sourceFactory = new ResourceSourceFactory(folder);
        factory = new JkuChoiceFactory(sourceFactory);
    }

    public ResourceSourceFactory getSourceFactory() {
        return sourceFactory;
    }

    public List<Menu> newMensa() throws IOException {
        return factory.newMensa();
    }
}
