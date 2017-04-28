package ru.nsu.ccfit.pleshkov.lab2.factory;

public class Worker implements Runnable {
    private Storage<Engine> engineStorage;
    private Storage<Body> bodyStorage;
    private Storage<Accessory> accessoryStorage;
    private CarStorage carStorage;

    public void run() {
        try {
            Car car = new Car(engineStorage.dequeue(),bodyStorage.dequeue(),accessoryStorage.dequeue());
            carStorage.enqueue(car);
        } catch (BadDetailException e) {
            System.err.println(Thread.currentThread().getName() + e.getMessage());
        } catch (InterruptedException e) {

        }
    }

    Worker(Storage<Accessory> accessories, Storage<Body> bodies,Storage<Engine> engines, CarStorage carStorage) {
        this.engineStorage = engines;
        this.bodyStorage = bodies;
        this.accessoryStorage = accessories;
        this.carStorage = carStorage;
    }
}
