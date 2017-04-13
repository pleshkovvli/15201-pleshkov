package ru.nsu.ccfit.pleshkov.lab2.factory;

public class Car extends Detail {
    final private Engine engine;
    final private Body body;
    final private Accessory accessory;

    Car(Engine engine, Body body, Accessory accessory) {
        super();
        this.engine = engine;
        this.body = body;
        this.accessory = accessory;
    }
}
