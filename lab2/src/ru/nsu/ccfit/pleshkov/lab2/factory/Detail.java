package ru.nsu.ccfit.pleshkov.lab2.factory;

abstract public class Detail {
    final private long id;
    final static private IDGenerator generator = new IDGenerator();

    Detail() {
        this.id = generator.getID();
    }

    long getID() {
        return id;
    }
}
