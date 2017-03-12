package ru.nsu.ccfit.pleshkov.lab1;

public class ParseException extends Lab1Exception {
    private String message;

    @Override
    public String getMessage() {
        return message;
    }

    ParseException() {
        message = "Bad parse: ";
    }

    ParseException(String s) {
        message = s;
    }

}
