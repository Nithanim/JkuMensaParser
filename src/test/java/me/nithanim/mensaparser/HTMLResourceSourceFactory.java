package me.nithanim.mensaparser;

import java.io.File;
import java.io.IOException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class HTMLResourceSourceFactory extends ResourceSourceFactory {
    public HTMLResourceSourceFactory(String folder) {
        super(folder);
    }
    
    @Override
    protected Document parseFile(File file) throws IOException {
        return Jsoup.parse(file, null);
    }
}
