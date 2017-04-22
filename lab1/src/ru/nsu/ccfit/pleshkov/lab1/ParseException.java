package ru.nsu.ccfit.pleshkov.lab1;

public class ParseException extends Lab1Exception {
    ParseException() {
        super("Bad parse: ");
    }

    ParseException(String s) {
        super(s);
    }

}
