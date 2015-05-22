package me.nithanim.mensaparser.jku;

import java.io.IOException;
import java.text.ParseException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import me.nithanim.mensaparser.MensaParseException;
import me.nithanim.mensaapi.Meal;
import me.nithanim.mensaapi.Menu;
import me.nithanim.mensaapi.Type;
import me.nithanim.mensaparser.SourceFactory;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;
import org.jsoup.select.NodeVisitor;

public class JkuChoiceFactory {
    private static final Pattern PATTERN = Pattern.compile("^([\\D]+)(\\d{1,2},\\d\\d) [A-Za-z]+$");
    
    private final SourceFactory sourceFactory;

    public JkuChoiceFactory(SourceFactory sourceFactory) {
        this.sourceFactory = sourceFactory;
    }
    
    public List<Menu> newJkuChoice() throws IOException {
        Document doc = sourceFactory.getAsHtml();
        Elements offers = doc.select("html body div#wrapper div#menu");
        
        for(Element offer : offers) {
            if(offer.select(">h2").first().text().startsWith("Choice")) {
                Element e = offer.select(">.menu-item > table > tbody > tr > td").first();
                return parseChoice(e);
            }
        }
        throw new MensaParseException("Unable to find choice menus!");
    }
    
    private static List<Menu> parseChoice(Element e) {
        Elements subtags = e.select("p");
        
        String date = handleWhitespacesAndTrim(subtags.remove(0).text());
        try {
            date = Util.parseTimeChoice(date);
        } catch(ParseException ex) {
            throw new MensaParseException("Unable to parse date \"" + date + "\" for Choice!");
        }
        
        replaceAllImages(e);
        List<Node> nodes = getEssentialNodes(subtags);
        handleWhitespaceAndTrim(nodes);
        List<List<String>> togetherNodes = findNodesTogether(nodes);

        List<Menu> menus = new LinkedList<Menu>();
        for(List<String> l : togetherNodes) {
            List<Meal> meals = new LinkedList<Meal>();
            Iterator<String> it = l.iterator();
            String subtype = it.next();
            while(it.hasNext()) {
                String raw = it.next();
                Matcher matcher = PATTERN.matcher(raw);

                String desc;
                int price;
                if(matcher.matches()) {
                    desc = matcher.group(1).trim();
                    String priceRaw = matcher.group(2).trim();
                    price = Integer.parseInt(priceRaw.replace(",", ""));
                } else {
                    if(raw.matches("-+")) { //subtype closed
                        continue;
                    } else {
                        //throw new MensaParseException("Unable to parse " + menuElement.toString());
                        desc = raw;
                        price = -2;
                    }
                }
                meals.add(new Meal(desc, price));
            }
            
            if(!meals.isEmpty()) {
                menus.add(new Menu(Type.CHOICE, subtype, meals, -1, 0, date, false)); //TODO Veggie
            }
        }
        return menus;
    }

    private static void replaceAllImages(Element elem) {
        Elements imgs = elem.select("img");
        for(Element img : imgs) {
            StringBuilder sb = new StringBuilder();
            if(img.previousSibling() instanceof TextNode) {
                TextNode s = (TextNode)img.previousSibling();
                sb.append(s.text());
                sb.append(' ');
                s.remove();
            }
            //String imgalt = img.attr("alt");
            //sb.append(imgalt.startsWith("Veg") ? "(vegetarisch)" : "(Fisch)"); ignore for now
            if(img.nextSibling() instanceof TextNode) {
                TextNode s = (TextNode)img.nextSibling();
                sb.append(' ');
                sb.append(s.text());
                s.remove();
            }
            
            img.replaceWith(new TextNode(sb.toString(), ""));
        }
    }
    
    private static List<List<String>> findNodesTogether(List<Node> nodes) {
        List<List<String>> result = new LinkedList<List<String>>();
        
        ListIterator<Node> it = nodes.listIterator();
        while(it.hasNext()) {
            List<String> a = new LinkedList<String>();
            mergeTitle(it);
            Node title = it.next();
            a.add(((Element)title).text());
            while(it.hasNext()) {
                Node next = it.next();
                if(next instanceof TextNode) {
                    a.add(((TextNode)next).text());
                } else {
                    it.previous();
                    break;
                }
            }
            result.add(a);
        }
        return result;
    }
    
    private static void handleWhitespaceAndTrim(List<Node> nodes) {
        for(Node node : nodes) {
            if(node instanceof TextNode) {
                TextNode tn = (TextNode)node;
                tn.text(handleWhitespacesAndTrim(tn.text()));
            } if(node instanceof Element && !((Element)node).tagName().equals("img")) {
                Element e = (Element) node;
                e.text(handleWhitespacesAndTrim(e.text()));
            }
        }
    }
    
    private static String handleWhitespacesAndTrim(String s) {
        return s.replace("\u00a0" /*&nbsp*/, " ").trim();
    }
    
    /**
     * Searches for &lt;strong&gt;s (which are assumes to be titles) and
     * merges all directly following into a single &lt;strong&gt;s. <br/>
     * This is a workaround for "&lt;strong&gt;P&lt;/strong&gt;&lt;strong&gt;izza/Pasta&lt;/strong&gt;"
     * 
     * @param it 
     */
    private static void mergeTitle(ListIterator<Node> it) {
        Element base = (Element)it.next();
        while(it.hasNext()) {
            Node next = it.next();
            if(next instanceof Element && ((Element)next).tagName().equals("strong")) {
                base.text(base.text() + ((Element)next).text());
                it.remove();
            } else {
                it.previous();
                break;
            }
        }
        it.previous();
    }
    
    /**
     * Flattens out the hierarchy of the nodes to only alternate between &lt;strong&gt;,
     * &lt;img&gt; and Text as a simple list to work around messed up hierarchy.
     * 
     * @param elems The root to start flattening out
     * @return 
     */
    private static List<Node> getEssentialNodes(Elements elems) {
        final List<Node> nodes = new LinkedList<Node>();
        elems.traverse(new NodeVisitor() {
            @Override
            public void head(Node node, int depth) {
                if(node instanceof TextNode) {
                    if(!((Element)node.parent()).tagName().equals("strong")) {
                        if(!isEmpty(node)) {
                            nodes.add(node);
                        }
                    }
                } else if(node instanceof Element) {
                    Element e = (Element)node;
                    if(e.tagName().equals("strong") || e.tagName().equals("img")) {
                        if(!isEmpty(node)) {
                            nodes.add(node);
                        }
                    }
                }
            }

            @Override
            public void tail(Node node, int depth) {
            }
        });
        return nodes;
    }
    
    private static boolean isEmpty(Node node) {
        if(node instanceof TextNode) {
            return ((TextNode)node).text().replace("\u00a0", "").trim().length() == 0;
        } else {
            return ((Element)node).text().replace("\u00a0", "").trim().length() == 0;
        }
    }
}
