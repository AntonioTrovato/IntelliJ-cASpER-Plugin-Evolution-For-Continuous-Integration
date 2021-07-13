package it.unisa.casper.originalParser;

public class ParsingException extends Exception {

    public ParsingException() {
        super("Error occurred Parsing Exception");
    }

    public ParsingException(String message) {
        super(message);
    }
}
