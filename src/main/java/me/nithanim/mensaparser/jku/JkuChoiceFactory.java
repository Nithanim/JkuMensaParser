package me.nithanim.mensaparser.jku;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import me.nithanim.mensaparser.MensaParseException;
import me.nithanim.mensaapi.Meal;
import me.nithanim.mensaapi.Menu;
import me.nithanim.mensaapi.Type;
import me.nithanim.mensaparser.MensaFactory;
import me.nithanim.mensaparser.SourceFactory;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;

public class JkuChoiceFactory implements MensaFactory {
    private static final Pattern MEAL_PATTERN = Pattern.compile("^([\\D]+)(\\d{1,2},\\d\\d) [A-Za-z]+$");

    private final SourceFactory sourceFactory;

    public JkuChoiceFactory(SourceFactory sourceFactory) {
        this.sourceFactory = sourceFactory;
    }

    @Override
    public List<Menu> newMensa() throws IOException {
        Document doc = sourceFactory.getAsHtml();
        Elements offers = doc.select("html body div#wrapper div#menu");

        for (Element offer : offers) {
            if (offer.select(">h2").first().text().startsWith("Choice")) {
                Element e = offer.select(">.menu-item > table > tbody > tr > td").first();
                return parseChoice(e);
            }
        }
        throw new MensaParseException("Unable to find choice menus!");
    }

    private static List<Menu> parseChoice(Element e) {
        ArrayList<Menu> menus = new ArrayList<Menu>();
        Elements rawMenus = e.select("p");

        String date = handleWhitespaces(rawMenus.remove(0).text());
        date = JkuUtil.parseTimeChoice(date);

        ensureParent(e, rawMenus);
        deleteEmpty(rawMenus);
        for (Element rawmenu : rawMenus) {
            mergeTitle(rawmenu);

            if (rawmenu.textNodes().isEmpty()) { //ignore "menus" without text
                continue; //ignore
            }

            Element subType = rawmenu.child(0);
            subType.remove();
            handleWhitespaces(subType);

            List<Meal> meals = new ArrayList<Meal>(3);
            List<List<Node>> rawMeals = splitMealsOfMenu(rawmenu);

            boolean vegetarian = false;
            for (List<Node> rawMeal : rawMeals) {
                handleWhitespaces(rawMeal);
                deleteEmpty(rawMeal);
                removePointlessBrTags(rawMeal);
                if (rawMeal.isEmpty()) {
                    continue;
                }

                String mealText = mergeText(rawMeal);
                //TODO extract vegetarian from image in rawMeal or from text

                Matcher matcher = MEAL_PATTERN.matcher(mealText);
                int price;
                if (matcher.matches()) {
                    mealText = matcher.group(1).trim();
                    String priceRaw = matcher.group(2).trim();
                    price = Integer.parseInt(priceRaw.replace(",", ""));
                } else {
                    if (mealText.matches("-+")) { //subtype closed
                        continue;
                    } else {
                        //throw new MensaParseException("Unable to parse " + menuElement.toString());
                        price = -2;
                    }
                }
                meals.add(new Meal(mealText, price));
            }
            menus.add(new Menu(Type.CHOICE, subType.text(), meals, -1, 0, date, vegetarian));
        }

        return menus;
    }

    private static String mergeText(List<Node> nodes) {
        ListIterator<Node> it = nodes.listIterator();
        if (it.hasNext()) {
            TextNode base = (TextNode) it.next();
            base.text(handleWhitespaces(base.text()));
            while (it.hasNext()) {
                Node next = it.next();
                if (next instanceof TextNode) {
                    base.text(
                        base.text()
                        + " "
                        + handleWhitespaces(((TextNode) next).text()));
                    next.remove();
                    it.remove();
                }
            }
            return base.text();
        }
        return null;
    }

    private static List<List<Node>> splitMealsOfMenu(Element menu) {
        List<List<Node>> meals = new ArrayList<List<Node>>();
        Iterator<Node> collector = menu.childNodes().iterator();
        ArrayList<Node> currentMeal = new ArrayList<Node>();
        while (collector.hasNext()) {
            Node next = collector.next();
            if (next instanceof Element && ((Element) next).tagName().equals("br")) {
                meals.add(currentMeal);
                if (collector.hasNext()) {
                    currentMeal = new ArrayList<Node>();
                }
            } else {
                currentMeal.add(next);
            }
        }
        meals.add(currentMeal);

        Iterator<List<Node>> cleaner = meals.iterator();
        while (cleaner.hasNext()) {
            if (cleaner.next().isEmpty()) {
                cleaner.remove();
            }
        }
        return meals;
    }

    private static void ensureParent(Element root, Elements elements) {
        Element last = null;
        for (Element p : elements) {
            if (p.parent() != root) {
                p.remove();
                if (last != null) {
                    root.after(last);
                } else {
                    root.before(root.child(0));
                }
            }
            last = p;
        }
    }

    private static void moveImagesToEnd(Element root) {
        Elements images = root.select("img");

        for (Element image : images) {
            image.remove();
        }
        for (Element image : images) {
            root.appendChild(image);
        }
    }

    /**
     * Searches for &lt;strong&gt;s (which are assumed to be titles) and merges
     * all directly following into a single &lt;strong&gt;s while deleting
     * everyting before. <br/>
     * This is a workaround for
     * "&lt;strong&gt;P&lt;/strong&gt;&lt;strong&gt;izza/Pasta&lt;/strong&gt;"
     *
     * @param it
     */
    private static void mergeTitle(Element menu) {
        ArrayList<Node> nodes = new ArrayList<Node>(menu.childNodes());

        ListIterator<Node> it = nodes.listIterator();
        //find base
        Element base = null;
        while (it.hasNext()) {
            Node node = it.next();
            if (node instanceof Element && ((Element) node).tagName().equals("strong")) {
                Element next = (Element) node;
                if (next.tagName().equals("strong")) {
                    base = next;
                    break;
                }
            } else {
                //remove all nodes before title completely
                node.remove();
                it.remove();
            }
        }
        if (base == null) {
            throw new MensaParseException("Unable to merge title", menu);
        }
        //merge following
        while (it.hasNext()) {
            Node next = it.next();
            if (next instanceof Element && ((Element) next).tagName().equals("strong")) {
                base.text(base.text() + ((Element) next).text());
                next.remove();
                it.remove();
            } else {
                break;
            }
        }
    }

    private static void removePointlessBrTags(List<Node> nodes) {
        ListIterator<Node> it = nodes.listIterator(nodes.size());
        while (it.hasPrevious()) {
            Node prev = it.previous();
            if (prev instanceof Element && ((Element) prev).tagName().equals("br")) {
                prev.remove();
                it.remove();
            } else {
                return; //only remove <br/> at end of list
            }
        }
    }

    private static void deleteEmpty(Collection<Node> nodes) {
        Iterator<Node> it = nodes.iterator();
        while (it.hasNext()) {
            Node node = it.next();
            if ((node instanceof TextNode && ((TextNode) node).isBlank()) || isEmpty(node)) {
                node.remove();
                it.remove();
            }
        }
    }

    private static void deleteEmpty(Elements elements) {
        Iterator<Element> it = elements.iterator();
        while (it.hasNext()) {
            Element e = it.next();
            if (isEmpty(e)) {
                e.remove();
                it.remove();
            }
        }
    }

    private static void handleWhitespaces(Collection<Node> nodes) {
        for (Node node : nodes) {
            handleWhitespaces(node);
        }
    }

    private static void handleWhitespaces(Node node) {
        if (isTextbasedNode(node)) {
            if (node instanceof TextNode) {
                TextNode tn = (TextNode) node;
                tn.text(handleWhitespaces(tn.text()));
            } else if (node instanceof Element) {
                Element e = (Element) node;
                e.text(handleWhitespaces(e.text()));
            }
        }
    }

    private static boolean isTextbasedNode(Node node) {
        return !(node instanceof Element && ((Element) node).tagName().equals("img"));
    }

    private static String handleWhitespaces(String s) {
        return s.replace("\u00a0" /*&nbsp*/, " ").trim();
    }

    private static boolean isEmpty(Node node) {
        if (node instanceof TextNode) {
            return ((TextNode) node).text().replace("\u00a0", "").trim().length() == 0;
        } else {
            return ((Element) node).text().replace("\u00a0", "").trim().length() == 0;
        }
    }
}
