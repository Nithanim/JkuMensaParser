package me.nithanim.mensaparser;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import org.jsoup.nodes.Document;

public abstract class ResourceSourceFactory implements SourceFactory {
    private final String folder;
    private String file;

    public ResourceSourceFactory(String folder) {
        this.folder = folder;
    }

    public void setFile(String file) {
        this.file = file;
    }

    @Override
    public Document getAsHtml() throws IOException {
        URL url = getClass().getResource(getPath());
        File f = new File(url.getPath());
        return parseFile(f);
    }

    protected abstract Document parseFile(File file) throws IOException;

    @Override
    public String getAsJson() throws IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    private String getPath() {
        return "/" + folder + "/" + file;
    }
}
