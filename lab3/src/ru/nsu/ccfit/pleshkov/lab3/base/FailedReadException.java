package ru.nsu.ccfit.pleshkov.lab3.base;

public class FailedReadException extends Exception {
    public FailedReadException() {}

    public FailedReadException(String message) {
        super(message);
    }

    public FailedReadException(Exception e) {
        super(e);
    }
}
