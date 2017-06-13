package ru.nsu.ccfit.pleshkov.lab3.server;

class BadParsingException extends Exception {
    BadParsingException(String message) {
        super(message);
    }

    BadParsingException(Exception e) {
        super(e);
    }
}
