package me.nithanim.mensaparser;

import java.io.IOException;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public abstract class SourceFactoryBase implements SourceFactory {
    @Override
    public Document getAsHtml() throws IOException {
        Connection con = Jsoup.connect(getUrl());
        con.timeout(60 * 1000);
        con.userAgent("MensaApiParser");
        setConnectionProperties(con);
        return con.get();
    }
    
    protected void setConnectionProperties(Connection con) {
    }
    
    protected abstract String getUrl();
    
    @Override
    public String getAsJson() {
        throw new UnsupportedOperationException("Not supported.");
    }
}
