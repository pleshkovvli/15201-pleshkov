package ru.nsu.ccfit.pleshkov.lab1;

public class Lab1RuntimeException extends RuntimeException {
    private String message;

    @Override
    public String getMessage() {
        return message;
    }

    Lab1RuntimeException(String s) {
        message = s;
    }
}
