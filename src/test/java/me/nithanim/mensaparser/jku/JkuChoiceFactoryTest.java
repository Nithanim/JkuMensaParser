package me.nithanim.mensaparser.jku;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import me.nithanim.mensaapi.Meal;
import me.nithanim.mensaapi.Menu;
import me.nithanim.mensaapi.Type;
import me.nithanim.mensaparser.HTMLResourceSourceFactory;
import me.nithanim.mensaparser.ParserTest;
import me.nithanim.mensaparser.ParserTestUtil;
import org.junit.Test;
import static org.junit.Assert.*;

public class JkuChoiceFactoryTest extends ParserTest {
    public JkuChoiceFactoryTest() {
        sourceFactory = new HTMLResourceSourceFactory("jku/choice");
        mensaFactory = new JkuChoiceFactory(sourceFactory);
    }

    @Test
    public void testParseOrdinaryChoice() throws Exception {
        getSourceFactory().setFile("ordinary.htm");
        List<Menu> actual = newMensa();

        List<Menu> expected = new ArrayList<Menu>();
        expected.add(new Menu(Type.CHOICE, "Grill",
            Arrays.asList(new Meal[]{
                new Meal("Mensa Burger", 460)
            }), -1, 0, "2015-05-29", false));
        expected.add(new Menu(Type.CHOICE, "Eintopf",
            Arrays.asList(new Meal[]{
                new Meal("Gebackene Tintenfischringe mit Karotffelsalat und Sauce Tartare", 510),}), -1, 0, "2015-05-29", false));
        expected.add(new Menu(Type.CHOICE, "Pizza/Pasta",
            Arrays.asList(new Meal[]{
                new Meal("Al Tonno", 270),
                new Meal("Spaghetti Bolognese/Broccolisauce", 280)
            }), -1, 0, "2015-05-29", false));
        expected.add(new Menu(Type.CHOICE, "Suppentopf",
            Arrays.asList(new Meal[]{
                new Meal("Klare Gemüsesuppe mit Profiteroles", 170),}), -1, 0, "2015-05-29", false));
        expected.add(new Menu(Type.CHOICE, "Brainfood",
            Arrays.asList(new Meal[]{
                new Meal("Penne Tricolore mit Spargelsauce", 490),}), -1, 0, "2015-05-29", false));
        expected.add(new Menu(Type.CHOICE, "Gemüsebuffet",
            Arrays.asList(new Meal[]{
                new Meal("Erdäpfel (vegan), Reis (vegan), Mischgemüse (vegan), Karfiol-Käselaibchen, Ratatouille Gemüse (vegan), Kohlsprossen (vegan)", -2),}), -1, 0, "2015-05-29", false));
        expected.add(new Menu(Type.CHOICE, "Süßhaus",
            Arrays.asList(new Meal[]{
                new Meal("Topfen-Marillenstrudel mit Vanillesauce und Fruchtcocktail 2,40/4,80 Euro", -2),}), -1, 0, "2015-05-29", false));

        System.out.println(Arrays.toString(expected.toArray()));
        System.out.println(Arrays.toString(actual.toArray()));
        ParserTestUtil.assertMenuListEquals(expected, actual);
    }

    @Test
    public void testWithoutSnack() throws Exception {
        getSourceFactory().setFile("without-snack.htm");
        List<Menu> menus = newMensa();

        for (Menu menu : menus) {
            if (menu.getSubtype().equals("Snack")) {
                fail("Empty section Snack was found but should have been discarded.");
            }
        }
    }

    @Test
    public void testSuesshausTwoPrices() throws Exception {
        //list as price not readable and one meal for now
        getSourceFactory().setFile("suesshaus-doubleprice.htm");
        List<Menu> menus = newMensa();

        Menu suesshaus = getSpecificMenu("Süßhaus", menus);
        assertEquals(1, suesshaus.getMeals().size());
        assertEquals(-1, suesshaus.getPrice());
        assertEquals(-2, suesshaus.getMeals().get(0).getPrice());
    }

    @Test(expected = NoSuchElementException.class)
    public void testSuesshausVeganEmpty() throws Exception {
        getSourceFactory().setFile("suesshaus-vegan-empty.htm");
        List<Menu> menus = newMensa();
        getSpecificMenu("Süßhaus", menus);
    }
    
    @Test
    public void testGrillElementInNewP() throws Exception {
        getSourceFactory().setFile("grill-element-in-new-p.htm");
        newMensa();
        assertEquals(1, exceptions.size());
    }

    private Menu getSpecificMenu(String subtype, List<Menu> menus) {
        int counter = 0;
        Menu needle = null;
        for (Menu menu : menus) {
            if (menu.getSubtype().equals("Süßhaus")) {
                counter++;
                needle = menu;
            }
        }
        if (needle == null) {
            throw new NoSuchElementException("No " + subtype + " found");
        } else if (counter != 1) {
            throw new IllegalStateException("More than one " + subtype + " found");
        }
        return needle;
    }
}
