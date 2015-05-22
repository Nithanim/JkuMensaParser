package me.nithanim.mensaparser.raab;

import me.nithanim.mensaparser.HtmlSourceFactoryBase;

public class RaabHtmlSourceFactory extends HtmlSourceFactoryBase {
    @Override
    protected String getUrl() {
        return "http://www.sommerhaus-hotel.at/de/restaurant_plan.php";
    }
}
