package ru.nsu.ccfit.pleshkov.lab3;

class FailedReadException extends Exception {
    FailedReadException() {}

    FailedReadException(Exception e) {
        super(e);
    }
}
