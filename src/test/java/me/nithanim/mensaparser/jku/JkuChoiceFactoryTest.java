package me.nithanim.mensaparser.jku;

import java.util.List;
import me.nithanim.mensaapi.Menu;
import me.nithanim.mensaparser.ParserTest;
import org.junit.Test;
import static org.junit.Assert.*;

public class JkuChoiceFactoryTest extends ParserTest {
    public JkuChoiceFactoryTest() {
        super("jku/choice");
    }
    
    @Test
    public void testWithoutSnack() throws Exception {
        getSourceFactory().setFile("without-snack.htm");
        List<Menu> menus = getFactory().newJkuChoice();
        
        for(Menu menu : menus) {
            if(menu.getSubtype().equals("Snack")) {
                fail("Empty section Snack was founf but shuld have been discarded.");
            }
        }
    }
    
    @Test
    public void testSuesshausTwoPrices() throws Exception {
        //list as not readable and one meal for now
        getSourceFactory().setFile("suesshaus-doubleprice.htm");
        List<Menu> menus = getFactory().newJkuChoice();
        
        int counter = 0;
        Menu suesshaus = null;
        for(Menu menu : menus) {
            if(menu.getSubtype().equals("Süßhaus")) {
                counter++;
                suesshaus = menu;
            }
        }
        assertNotNull("No Süßhaus found", suesshaus);
        assertEquals(1, counter);
        assertEquals(1, suesshaus.getMeals().size());
        assertEquals(-1, suesshaus.getPrice());
        assertEquals(-2, suesshaus.getMeals().get(0).getPrice());
    }
}
