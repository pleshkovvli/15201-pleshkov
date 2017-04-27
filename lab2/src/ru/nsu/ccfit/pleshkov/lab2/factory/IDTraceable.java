package ru.nsu.ccfit.pleshkov.lab2.factory;

abstract public class IDTraceable {
    final private long id;
    final static private IDGenerator generator = new IDGenerator();

    IDTraceable() {
        this.id = generator.getID();
    }

    long getID() {
        return id;
    }
}
