package me.nithanim.mensaparser.jku;

import me.nithanim.mensaparser.SourceFactoryBase;

public class JkuChoiceSourceFactory extends SourceFactoryBase {
    @Override
    protected String getUrl() {
        return "http://menu.mensen.at/index/print/locid/1";
    }
}
