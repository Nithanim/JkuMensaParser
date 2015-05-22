package me.nithanim.mensaparser.jku;

import me.nithanim.mensaparser.HtmlSourceFactoryBase;

public class JkuChoiceHtmlSourceFactory extends HtmlSourceFactoryBase {
    @Override
    protected String getUrl() {
        return "http://menu.mensen.at/index/print/locid/1";
    }
}
