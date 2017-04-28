package ru.nsu.ccfit.pleshkov.lab2.factory;

public class BadParseException extends Exception {
    public BadParseException(String s, Exception e) {
        super(s,e);
    }
    public BadParseException(String s) {
        super(s);
    }
}
