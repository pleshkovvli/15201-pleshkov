package ru.nsu.ccfit.pleshkov.lab1;

public class FileWalkerRuntimeException extends RuntimeException {
    FileWalkerRuntimeException(String s) {
        super(s);
    }

    FileWalkerRuntimeException(String s, Exception e) {
        super(s,e);
    }
}
