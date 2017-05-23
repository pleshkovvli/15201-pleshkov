package ru.nsu.ccfit.pleshkov.lab2.factory;

import java.io.IOException;

class Worker implements Runnable {
    private Storage<Engine> engineStorage;
    private Storage<Body> bodyStorage;
    private Storage<Accessory> accessoryStorage;
    private CarStorage carStorage;
    private FactoryLogger logger;

    public void run() {
        try {
            Car car = new Car(engineStorage.dequeue(),bodyStorage.dequeue(),accessoryStorage.dequeue());
            carStorage.enqueue(car);
        } catch (BadDetailException e) {
            if(logger != null) {
                logger.log(Thread.currentThread().getName() + e.getMessage());
            }
        } catch (InterruptedException e) {
            if(logger != null) {
                logger.log(Thread.currentThread().getName() + " was interrupted");
            }
        }
    }

    Worker(Storage<Accessory> accessories, Storage<Body> bodies,Storage<Engine> engines, CarStorage carStorage) {
        this.engineStorage = engines;
        this.bodyStorage = bodies;
        this.accessoryStorage = accessories;
        this.carStorage = carStorage;
        try {
            this.logger = FactoryLogger.getLogger();
        } catch (IOException e) {
            System.err.println(Thread.currentThread().getName() + " failed to get logger");
        }
    }
}
