package ru.nsu.ccfit.pleshkov.lab2.factory;

abstract class IDTraceable {
    final private long id;

    IDTraceable() {
        this.id = setID();
    }

    abstract long setID();

    long getID() {
        return id;
    }
}
