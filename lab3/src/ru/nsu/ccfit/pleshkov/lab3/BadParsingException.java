package ru.nsu.ccfit.pleshkov.lab3;

public class BadParsingException extends Exception {
    public BadParsingException(String message) {
        super(message);
    }

    public BadParsingException(Exception e) {
        super(e);
    }
}
