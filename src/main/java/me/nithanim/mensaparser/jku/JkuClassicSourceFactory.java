package me.nithanim.mensaparser.jku;

import me.nithanim.mensaparser.SourceFactoryBase;
import org.jsoup.Connection;
import org.jsoup.parser.Parser;

public class JkuClassicSourceFactory extends SourceFactoryBase {
    @Override
    protected String getUrl() {
        return "http://menu.mensen.at/index/rss/locid/1";
    }

    @Override
    protected void setConnectionProperties(Connection con) {
        con.parser(Parser.xmlParser());
    }
}
