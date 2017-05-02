package ru.nsu.ccfit.pleshkov.lab2.factory;

class CarStorageController implements Runnable {
    private CarStorage carStorage;
    private Factory factory;

    public void run() {
        try {
            while (!Thread.interrupted()) {
                carStorage.control();
                if(carStorage.getCount() <= (carStorage.getCapacity()/2)) {
                    factory.doJob(carStorage.getCapacity()/4 + 1);

                }
            }
        } catch (InterruptedException e) {

        }
    }

    CarStorageController(CarStorage carStorage, Factory factory) {
        this.carStorage = carStorage;
        this.factory = factory;
    }
}
