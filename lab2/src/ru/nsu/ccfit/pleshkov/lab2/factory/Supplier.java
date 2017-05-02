package ru.nsu.ccfit.pleshkov.lab2.factory;

class Supplier<T extends IDTraceable> implements Runnable {
    private Storage<T> storage;
    final private Class typeClass;

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
                try {
                    detail = (T) typeClass.newInstance();
                } catch (Exception e) {
                    System.err.println("Failed to cast: " + typeClass);
                }
                storage.enqueue(detail);
            }
        } catch (InterruptedException e) {

        }
    }

    Supplier(Storage<T> storage, Class typeClass, int sleepTime) {
        this.typeClass = typeClass;
        this.storage = storage;
        this.sleepTime = sleepTime;
    }
}
