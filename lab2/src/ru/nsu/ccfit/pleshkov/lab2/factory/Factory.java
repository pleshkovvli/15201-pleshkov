package ru.nsu.ccfit.pleshkov.lab2.factory;

import ru.nsu.ccfit.pleshkov.lab2.pool.ThreadPool;

public class Factory {
    final private Worker worker;
    private ThreadPool pool;

    public void stop() {
        pool.stopPool();
    }

    public void doJob(int number) throws InterruptedException {
        for(int i = 0; i < number; i++) {
            pool.addTask(worker);
        }
    }

    Factory(Storage<Accessory> accessories, Storage<Body> bodies, Storage<Engine> engines,
            CarStorage carStorage, int numberOfThreads) {
        worker = new Worker(accessories,bodies,engines,carStorage);
        pool = new ThreadPool(numberOfThreads);
    }
}
