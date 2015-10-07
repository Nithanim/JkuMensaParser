package me.nithanim.mensaparser.raab;

import me.nithanim.mensaparser.SourceFactoryBase;

public class RaabSourceFactory extends SourceFactoryBase {
    @Override
    protected String getUrl() {
        return "http://www.sommerhaus-hotel.at/de/linz";
    }
}
