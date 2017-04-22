package ru.nsu.ccfit.pleshkov.lab1;

public class CountException extends Lab1Exception {

    CountException() {
        super("Bad count: ");
    }

    CountException(String s) {
        super(s);
    }

    CountException(Exception e) {
        super(e);
    }
}
