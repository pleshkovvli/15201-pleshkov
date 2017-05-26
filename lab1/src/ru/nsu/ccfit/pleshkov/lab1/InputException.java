package ru.nsu.ccfit.pleshkov.lab1;

import java.nio.file.InvalidPathException;

public class InputException extends Lab1Exception {

    InputException(Exception e) {
        super(e);
    }

    InputException(InvalidPathException e) {
        super(e.getReason()  + " is not a path");
    }

    InputException(String s) {
        super(s);
    }
}
