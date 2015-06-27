package me.nithanim.mensaparser;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Node;

public class MensaParseException extends RuntimeException {
    private Document document;
    private Node node;
    
    public MensaParseException() {
    }
    
    public MensaParseException(String message) {
        super(message);
    }
    
    public MensaParseException(String message, Node node) {
        super(message);
        this.node = node;
    }

    public MensaParseException(Throwable cause) {
        super(cause);
    }
    
    public MensaParseException(Throwable cause, Node node) {
        super(cause);
        this.node = node;
    }

    /**
     * Sets the {@link Document} on which the parse error occured.
     * 
     * @param document 
     */
    public void setDocument(Document document) {
        if(document != null) {
            throw new IllegalStateException("The document was already set!");
        }
        this.document = document;
    }

    /**
     * Returns the document on which the (parse) error occurred to be able
     * to fix the error after the original page has been updated..
     * 
     * @return The document on which the error occurred, null if not available.
     */
    public Document getDocument() {
        return document;
    }

    /**
     * Returns the Node (roughly) on which the error error occurred. 
     * 
     * @return Node on which the error occured, null if not available.
     */
    public Node getNode() {
        return node;
    }
}
