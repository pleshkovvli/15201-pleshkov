package ru.nsu.ccfit.pleshkov.lab2.factory;

import ru.nsu.ccfit.pleshkov.lab2.pool.ThreadPool;

public class Factory extends ThreadPool {
    private Storage<Engine> engineStorage;
    private Storage<Body> bodyStorage;
    private Storage<Accessory> accessoryStorage;
    private CarStorage carStorage;
    private int sleepTime;

    public void doJob(int number) throws InterruptedException {
        for(int i = 0; i < number; i++) {
            addTask(new Worker(accessoryStorage,bodyStorage,engineStorage,carStorage,sleepTime));
        }
    }

    Factory(Storage<Accessory> accessories, Storage<Body> bodies, Storage<Engine> engines,
            CarStorage carStorage, int numberOfThreads, int sleepTime) {
        super(numberOfThreads);
        this.engineStorage = engines;
        this.bodyStorage = bodies;
        this.accessoryStorage = accessories;
        this.carStorage = carStorage;
        this.sleepTime = sleepTime;
    }
}
