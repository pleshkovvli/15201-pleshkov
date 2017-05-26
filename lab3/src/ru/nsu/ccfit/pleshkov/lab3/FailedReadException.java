package ru.nsu.ccfit.pleshkov.lab3;

public class FailedReadException extends Exception {
    public FailedReadException() {
    }

    FailedReadException(Exception e) {
        super(e);
    }
}
