package me.nithanim.mensaparser;

public class MensaParseException extends RuntimeException {
    public MensaParseException() {
    }
    
    public MensaParseException(String message) {
        super(message);
    }

    public MensaParseException(Throwable cause) {
        super(cause);
    }
}
