package ru.nsu.ccfit.pleshkov.lab2.factory;

import java.io.IOException;

class Supplier<T extends IDTraceable> implements Runnable {
    private Storage<T> storage;
    final private Class typeClass;

    private FactoryLogger logger;
    int getSleepTime() {
        return sleepTime;
    }

    void setSleepTime(int sleepTime) {
        this.sleepTime = sleepTime;
    }

    private int sleepTime;

    public void run() {
        try {
            while(!Thread.interrupted()) {
                Thread.sleep(sleepTime);
                T detail = null;
                    detail = (T) typeClass.newInstance();
                storage.enqueue(detail);
            }
        } catch (IllegalAccessException | InstantiationException e) {
            logger.error("Failed to cast: " + typeClass);
        }
        catch (InterruptedException e) {
            logger.log("Stopping " + Thread.currentThread().getName());
        }
    }

    Supplier(Storage<T> storage, Class typeClass, int sleepTime) {
        this.typeClass = typeClass;
        this.storage = storage;
        this.sleepTime = sleepTime;
        try {
            logger = FactoryLogger.getLogger();
        } catch (IOException e) {

        }
    }
}
