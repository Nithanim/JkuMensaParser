package me.nithanim.mensaparser.khg;

import me.nithanim.mensaparser.HtmlSourceFactoryBase;

public class KhgHtmlSourceFactory extends HtmlSourceFactoryBase {
    @Override
    protected String getUrl() {
        return "http://www.khg-linz.at/?page_id=379";
    }
}
