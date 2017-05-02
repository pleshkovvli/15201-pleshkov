package ru.nsu.ccfit.pleshkov.lab2.factory;

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
                logger.simpleLog(Thread.currentThread().getName() + e.getMessage());
            }
        } catch (InterruptedException e) {

        }
    }

    Worker(Storage<Accessory> accessories, Storage<Body> bodies,Storage<Engine> engines, CarStorage carStorage, FactoryLogger logger) {
        this.engineStorage = engines;
        this.bodyStorage = bodies;
        this.accessoryStorage = accessories;
        this.carStorage = carStorage;
        this.logger = logger;
    }
}
