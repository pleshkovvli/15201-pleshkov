package ru.nsu.ccfit.pleshkov.lab2.factory;

import ru.nsu.ccfit.pleshkov.lab2.pool.ThreadPool;

class Factory {
    final private Worker worker;
    private ThreadPool pool;

    void stop() {
        pool.stopPool();
    }

    void doJob(int number) throws InterruptedException {
        for(int i = 0; i < number; i++) {
            pool.addTask(worker);
        }
    }

    Factory(Storage<Accessory> accessories, Storage<Body> bodies, Storage<Engine> engines,
            CarStorage carStorage, int numberOfThreads, FactoryLogger logger) {
        worker = new Worker(accessories,bodies,engines,carStorage, logger);
        pool = new ThreadPool(numberOfThreads);
    }
}
