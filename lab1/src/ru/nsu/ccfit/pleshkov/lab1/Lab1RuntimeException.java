package ru.nsu.ccfit.pleshkov.lab1;

public class Lab1RuntimeException extends RuntimeException {
    Lab1RuntimeException(Exception e) {
        super(e);
    }

    Lab1RuntimeException() {}
}
