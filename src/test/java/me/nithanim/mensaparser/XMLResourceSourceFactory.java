package me.nithanim.mensaparser;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.URL;
import java.nio.charset.Charset;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;

public class XMLResourceSourceFactory extends ResourceSourceFactory {
    public XMLResourceSourceFactory(String folder) {
        super(folder);
    }
    
    @Override
    protected Document parseFile(File file) throws IOException {
        String xml = fileToString(file);
        return Jsoup.parse(xml, "", Parser.xmlParser());
    }

    private String fileToString(File file) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        BufferedInputStream in = new BufferedInputStream(new FileInputStream(file));
        try {
            byte[] buffer = new byte[1024];
            int read;
            while((read = in.read(buffer)) != -1) {
                out.write(buffer, 0, read);
            }
        } finally {
            in.close();
        }
        return new String(out.toByteArray(), Charset.forName("UTF-8"));
    }
}
