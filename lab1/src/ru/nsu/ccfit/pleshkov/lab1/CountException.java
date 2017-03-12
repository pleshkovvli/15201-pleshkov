package ru.nsu.ccfit.pleshkov.lab1;

public class CountException extends Lab1Exception {
    private String message;

    @Override
    public String getMessage() {
        return message;
    }

    CountException() {
        message = "Bad count: ";
    }

    CountException(String s) {
        message = s;
    }

    CountException(Exception e) {
        super(e);
    }
}
