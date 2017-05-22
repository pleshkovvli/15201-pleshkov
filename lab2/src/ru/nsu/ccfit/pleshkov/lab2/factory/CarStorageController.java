package ru.nsu.ccfit.pleshkov.lab2.factory;

import java.io.IOException;

class CarStorageController implements Runnable {
    private CarStorage carStorage;
    private Factory factory;

    private FactoryLogger logger;

    public void run() {
        try {
            while (!Thread.interrupted()) {
                carStorage.control();
                if(carStorage.getCount() <= (carStorage.getCapacity()/2)) {
                    factory.doJob(carStorage.getCapacity()/4 + 1);

                }
            }
        } catch (InterruptedException e) {
            if(logger != null) {
                logger.log("Stopping car storage controller");
            }
        }
    }

    CarStorageController(CarStorage carStorage, Factory factory) {
        this.carStorage = carStorage;
        this.factory = factory;
        try {
            logger = FactoryLogger.getLogger();
        } catch (IOException e) {

        }
    }
}
