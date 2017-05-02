package ru.nsu.ccfit.pleshkov.lab2.factory;

class Accessory extends IDTraceable {
    final private static IDGenerator generator = new IDGenerator();

    long setID() {
        return generator.getID();
    }
}
