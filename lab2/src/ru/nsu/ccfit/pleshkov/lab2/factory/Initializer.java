package ru.nsu.ccfit.pleshkov.lab2.factory;

import ru.nsu.ccfit.pleshkov.lab2.Form;

import javax.swing.*;
import java.io.*;
import java.nio.file.Path;
import java.util.ArrayList;

public class Initializer {
    final static public int INIT_SLEEP_TIME = 100;
    final static public int MAX_SLEEP_TIME = 2000;
    final static private int NUMBER_OF_THREADS = 3;

    private static ConfigData data;

    static int getAccessoryStorageCapacity() {
        return data.getAccessoryStorageCapacity();
    }

    static int getBodiesStorageCapacity() {
        return data.getBodiesStorageCapacity();
    }

    static int getEnginesStorageCapacity() {
        return data.getEnginesStorageCapacity();
    }

    static int getCarStorageCapacity() {
        return data.getCarStorageCapacity();
    }

    static int getAccessorySuppliersNumber() {
        return data.getAccessorySuppliersNumber();
    }

    static int getNumberOfWorkers() {
        return data.getNumberOfWorkers();
    }

    static int getDealersNumber() {
        return data.getDealersNumber();
    }

    static boolean isToLog() {
        return data.isToLog();
    }

    static private ArrayList<Thread> threads;
    static private Factory factory;
    static private FactoryLogger logger;

    public static void startFactory(Path configFilePath) throws BadParseException {
        data = ConfigParser.parseConfig(configFilePath);

        Storage<Accessory> accessoryStorage = new Storage<>(getAccessoryStorageCapacity());
        Storage<Body> bodyStorage = new Storage<>(getBodiesStorageCapacity());
        Storage<Engine> engineStorage = new Storage<>(getEnginesStorageCapacity());
        CarStorage carStorage = new CarStorage(getCarStorageCapacity());

        Supplier<Engine> engineSupplier = new Supplier<Engine>(engineStorage, Engine.class,INIT_SLEEP_TIME);
        Supplier<Body> bodiesSupplier = new Supplier<Body>(bodyStorage, Body.class,INIT_SLEEP_TIME);

        try {
            logger = FactoryLogger.getLogger();
            logger.setToLog(isToLog());
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }

        threads = new ArrayList<Thread>(getDealersNumber() + getAccessorySuppliersNumber() + NUMBER_OF_THREADS);
        factory = new Factory(accessoryStorage,bodyStorage,engineStorage,carStorage,getNumberOfWorkers(),getCarStorageCapacity());
        threads.add(new Thread(bodiesSupplier, "bodiesSupplier"));
        threads.add(new Thread(engineSupplier, "engineSupplier"));
        threads.add(new Thread(new CarStorageController(carStorage,factory), "CarStorageController"));

        Supplier[] accessorySuppliers = new Supplier[getAccessorySuppliersNumber()];
        Dealer[] dealers = new Dealer[getDealersNumber()];

        for(int i = 0 ; i < dealers.length; i++) {
            dealers[i] = new Dealer(carStorage,i,INIT_SLEEP_TIME);
            threads.add(new Thread(dealers[i], "Dealer#" + String.valueOf(i)));
        }
        for(int i = 0 ; i < accessorySuppliers.length; i++) {
            accessorySuppliers[i] = new Supplier<Accessory>(accessoryStorage, Accessory.class,INIT_SLEEP_TIME);
            threads.add(new Thread(accessorySuppliers[i],"accessorySupplier#" + String.valueOf(i)));
        }
        Form form = new Form(new FormStartObjects((int newData) -> {
            for(Supplier supplier : accessorySuppliers) {
                SwingUtilities.invokeLater(() -> supplier.setSleepTime(newData));
            }}, (int newData) -> SwingUtilities.invokeLater(() -> engineSupplier.setSleepTime(newData)),
                (int newData) -> SwingUtilities.invokeLater(() -> bodiesSupplier.setSleepTime(newData)),
                (int newData) -> {
                    for(Dealer dealer : dealers) {
                        SwingUtilities.invokeLater(() -> dealer.setSleepTime(newData));
                    }},
                getDealersNumber(), getAccessorySuppliersNumber(),
                getAccessoryStorageCapacity(), getBodiesStorageCapacity(), getEnginesStorageCapacity(),
                getCarStorageCapacity(), getNumberOfWorkers(), getCarStorageCapacity(),
                (int newData) -> SwingUtilities.invokeLater(() -> {if(logger != null ) logger.setToLog(!logger.isToLog());}),
                isToLog()));

        new CountObserver(bodyStorage) {
            @Override
            protected void specificJob() {
                form.updateNumberOfBodies(getCount());
            }
        };
        new CountObserver(engineStorage) {
            @Override
            protected void specificJob() {
                form.updateNumberOfEngines(getCount());
            }
        };
        new CountObserver(accessoryStorage) {
            @Override
            protected void specificJob() {
                form.updateNumberOfAccessories(getCount());
            }
        };
        new CountObserver(carStorage) {
            @Override
            protected void specificJob() {
                form.updateNumberOfCars(getCount());
            }
        };
        new CountObserver(factory.getPool()) {
            @Override
            protected void specificJob() {
                form.updateNumberOfWorkers(getCount());
            }
        };
        new CountObserver(factory.getQueue()) {
            @Override
            protected void specificJob() {
                form.updateNumberOfTasks(getCount());
            }
        };
        CountObserver[] profitObservers = new CountObserver[dealers.length];
        for(int i = 0; i < dealers.length; i++) {
            profitObservers[i] = new CountObserver(dealers[i]) {
                @Override
                protected void specificJob () {
                    int sum = 0;
                    for(int j = 0; j < profitObservers.length; j++) {
                        sum += profitObservers[j].getCount();
                    }
                    form.updateProfit(sum);
                }
            } ;
        }

        for(Thread t : threads) {
            t.start();
        }
        if(logger != null) {
            logger.log("Started");
        }
    }



    public static void finish() {
        factory.stop();
        for(Thread t : threads) {
            t.interrupt();
        }
        for(Thread t : threads) {
            try {
                t.join();
            } catch (InterruptedException e) {
                logger.error("Failed to join thread " + t.getName());
            }
        }
        if(logger != null) {
            logger.log("Finished");
        }
    }
}
