package ru.nsu.ccfit.pleshkov.lab3;

class FailedReadException extends Exception {
    FailedReadException() {}

    FailedReadException(String message) {
        super(message);
    }

    FailedReadException(Exception e) {
        super(e);
    }
}
