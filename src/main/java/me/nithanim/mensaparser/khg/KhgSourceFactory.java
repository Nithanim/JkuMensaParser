package me.nithanim.mensaparser.khg;

import me.nithanim.mensaparser.SourceFactoryBase;

public class KhgSourceFactory extends SourceFactoryBase {
    @Override
    protected String getUrl() {
        return "http://www.khg-linz.at/institution/8075/essen/menueplan";
    }
}
