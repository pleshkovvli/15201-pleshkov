package ru.nsu.ccfit.pleshkov.lab2.factory;

public class IDGenerator {
    private long currentID = 1;

    public long getID() {
        return currentID++;
    }
}
