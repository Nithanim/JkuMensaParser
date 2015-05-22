package me.nithanim.mensaparser;

import java.io.IOException;
import org.jsoup.nodes.Document;

public interface SourceFactory {
    Document getAsHtml() throws IOException;
    String getAsJson() throws IOException;
}
