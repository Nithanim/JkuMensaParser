package me.nithanim.mensaparser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import me.nithanim.mensaapi.Menu;
import org.junit.Assert;

public class ParserTestUtil {
    public static void assertMenuListEquals(List<Menu> expected, List<Menu> actual) {
        List<Menu> missingExpected = new ArrayList<Menu>(expected);
        missingExpected.removeAll(actual);
        Assert.assertEquals(Collections.EMPTY_LIST, missingExpected);

        List<Menu> tooMuchActual = new ArrayList<Menu>(actual);
        tooMuchActual.removeAll(expected);
        Assert.assertEquals(Collections.EMPTY_LIST, tooMuchActual);
    }

    private ParserTestUtil() {
    }
}
