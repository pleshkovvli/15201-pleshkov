package ru.nsu.ccfit.pleshkov.lab2.factory;

class Car extends IDTraceable {
    final private static IDGenerator generator = new IDGenerator();

    long setID() {
        return generator.getID();
    }

    final private Engine engine;
    final private Body body;
    final private Accessory accessory;

    Car(Engine engine, Body body, Accessory accessory) throws BadDetailException {
        super();
        if(engine==null || body==null || accessory==null) {
            String message = " failed to create car: ";
            if(engine==null) {
                message = message + "null engine ";
            }
            if(body==null) {
                message = message + "null body ";
            }
            if(accessory==null) {
                message = message + "null accessory ";
            }
            throw new BadDetailException(message);

        }
        this.engine = engine;
        this.body = body;
        this.accessory = accessory;
    }

    long getBodyID() {
        return body.getID();
    }


    long getEngineID() {
        return engine.getID();
    }

    long getAccessoryID() {
        return accessory.getID();
    }
}
