package ru.nsu.ccfit.pleshkov.lab2.factory;

class BadParseException extends Exception {
    BadParseException(String s, Exception e) {
        super(s,e);
    }
    BadParseException(String s) {
        super(s);
    }
}
