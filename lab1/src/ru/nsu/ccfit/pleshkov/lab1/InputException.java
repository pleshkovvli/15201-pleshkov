package ru.nsu.ccfit.pleshkov.lab1;

import java.nio.file.InvalidPathException;

public class InputException extends Lab1Exception {
    private String message;

    @Override
    public String getMessage() {
        return message;
    }

    InputException(Exception e) {
        super(e);
    }

    InputException(InvalidPathException e) {
        message = e.getReason()  + " is not a path";
    }

    InputException(String s) {
        message = s;
    }
}
