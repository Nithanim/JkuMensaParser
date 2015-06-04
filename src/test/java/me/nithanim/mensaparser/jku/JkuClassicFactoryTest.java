package me.nithanim.mensaparser.jku;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import me.nithanim.mensaapi.Meal;
import me.nithanim.mensaapi.Menu;
import me.nithanim.mensaapi.Type;
import me.nithanim.mensaparser.ParserTest;
import me.nithanim.mensaparser.ParserTestUtil;
import me.nithanim.mensaparser.XMLResourceSourceFactory;
import org.hamcrest.CoreMatchers;
import org.junit.Test;
import static org.junit.Assert.*;

public class JkuClassicFactoryTest extends ParserTest {
    public JkuClassicFactoryTest() {
        sourceFactory = new XMLResourceSourceFactory("jku/classic");
        mensaFactory = new JkuClassicFactory(sourceFactory);
    }
    
    @Test
    public void testParseOrdinaryChoice() throws Exception {
        getSourceFactory().setFile("ordinary.rss");
        List<Menu> actual = newMensa();
        
        List<Menu> expected = new ArrayList<Menu>();
        expected.add(new Menu(Type.CLASSIC, "Menü Classic 1",
                Arrays.asList(new Meal[] {
                    new Meal("Erdäpfelcremesuppe", -1),
                    new Meal("Spaghetti \"Pomodoro\" mit gebratenen Fleischbällchen, dazu Parmesan", -1),
                    new Meal("und Salat", -1),
                }), 365, 115, "2015-06-01", false));
        expected.add(new Menu(Type.CLASSIC, "Menü Classic 1",
                Arrays.asList(new Meal[] {
                    new Meal("Klare Gemüsesuppe mit Backerbsen (vegan)", -1),
                    new Meal("Wildreispfanne Asia mit Shrimps und Lachs", -1),
                    new Meal("dazu Salat", -1),
                }), 365, 115, "2015-06-02", false));
        expected.add(new Menu(Type.CLASSIC, "Menü Classic 1",
                Arrays.asList(new Meal[] {
                    new Meal("Gemüsecremesuppe", -1),
                    new Meal("Auflauf mit Kartoffeln, Schwarzwurzeln und Spinat, dazu Schnittlauchdip", -1),
                    new Meal("und Salat", -1),
                }), 365, 115, "2015-06-03", false));
        expected.add(new Menu(Type.CLASSIC, "Menü Classic 1",
                Arrays.asList(new Meal[] {
                    new Meal("Frohleichnam - Geschlossen", -1),
                }), 365, 115, "2015-06-04", false));
        expected.add(new Menu(Type.CLASSIC, "Menü Classic 1",
                Arrays.asList(new Meal[] {
                    new Meal("Classic #1 - Geschlossen", -1),
                }), 365, 115, "2015-06-05", false));
        
        expected.add(new Menu(Type.CLASSIC, "Menü Classic 2",
                Arrays.asList(new Meal[] {
                    new Meal("Erdäpfelcremesuppe", -1),
                    new Meal("Saftiges Hirschragout mit Semmelknödel", -1),
                    new Meal("dazu Apfel-Rotkraut", -1),
                }), 500, 80, "2015-06-01", false));
        expected.add(new Menu(Type.CLASSIC, "Menü Classic 2",
                Arrays.asList(new Meal[] {
                    new Meal("Klare Gemüsesuppe mit Backerbsen (vegan)", -1),
                    new Meal("Käsekrainer vom Grill mit Curryketchup, dazu Pommes frites", -1),
                    new Meal("und Salat", -1),
                }), 500, 80, "2015-06-02", false));
        expected.add(new Menu(Type.CLASSIC, "Menü Classic 2",
                Arrays.asList(new Meal[] {
                    new Meal("Gemüsecremesuppe", -1),
                    new Meal("Gebratenes Hühnerfilet mit Spargelsauce, dazu Gemüsebulgur", -1),
                    new Meal("und Salat", -1),
                }), 500, 80, "2015-06-03", false));
        expected.add(new Menu(Type.CLASSIC, "Menü Classic 2",
                Arrays.asList(new Meal[] {
                    new Meal("Frohnleichnam - Geschlossen", -1),
                }), 500, 80, "2015-06-04", false));
        expected.add(new Menu(Type.CLASSIC, "Menü Classic 2",
                Arrays.asList(new Meal[] {
                    new Meal("Klare Gemüsesuppe mit Frittaten", -1),
                    new Meal("Gebackenes Putenschnitzel mit Reis und Preiselbeeren", -1),
                    new Meal("dazu Salat", -1),
                }), 500, 80, "2015-06-05", false));
        
        ParserTestUtil.assertMenuListEquals(expected, actual);
    }
}