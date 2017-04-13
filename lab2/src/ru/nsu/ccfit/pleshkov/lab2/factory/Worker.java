package ru.nsu.ccfit.pleshkov.lab2.factory;

public class Worker implements Runnable {
    private Storage<Engine> engineStorage;
    private Storage<Body> bodyStorage;
    private Storage<Accessory> accessoryStorage;
    private CarStorage carStorage;

    public int getSleepTime() {
        return sleepTime;
    }

    public void setSleepTime(int sleepTime) {
        this.sleepTime = sleepTime;
    }

    private int sleepTime;

    public void run() {
        try {
            Thread.sleep(sleepTime);
            Car car = new Car(engineStorage.dequeue(),bodyStorage.dequeue(),accessoryStorage.dequeue());
            carStorage.enqueue(car);
        } catch (InterruptedException e) {

        }
    }

    Worker(Storage<Accessory> accessories, Storage<Body> bodies,Storage<Engine> engines,
           CarStorage carStorage, int sleepTime) {
        this.engineStorage = engines;
        this.bodyStorage = bodies;
        this.accessoryStorage = accessories;
        this.carStorage = carStorage;
        this.sleepTime = sleepTime;
    }
}
