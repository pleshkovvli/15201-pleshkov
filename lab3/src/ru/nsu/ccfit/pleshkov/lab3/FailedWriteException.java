package ru.nsu.ccfit.pleshkov.lab3;

public class FailedWriteException extends Exception {
    public FailedWriteException() {
    }

    public FailedWriteException(Exception e) {
        super(e);
    }
}
