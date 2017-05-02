package ru.nsu.ccfit.pleshkov.lab2.factory;

import ru.nsu.ccfit.pleshkov.lab2.Form;

import javax.swing.*;
import java.io.*;
import java.nio.file.Path;

public class Initializer {
    final static public int INIT_SLEEP_TIME = 100;
    final static public int MAX_SLEEP_TIME = 2000;
    final static private int NUMBER_OF_THREADS = 3;

    final static private String accessoryStorageCapacityString = "accessoryStorageCapacity";
    final static private String bodiesStorageCapacityString = "bodiesStorageCapacity";
    final static private String enginesStorageCapacityString = "enginesStorageCapacity";
    final static private String carStorageCapacityString = "carStorageCapacity";
    final static private String accessorySuppliersNumberString = "accessorySuppliersNumber";
    final static private String numberOfWorkersString = "numberOfWorkers";
    final static private String dealersNumberString = "dealersNumber";
    final static private String toLogString = "toLog";

    public static int getAccessoryStorageCapacity() {
        return accessoryStorageCapacity;
    }

    public static int getBodiesStorageCapacity() {
        return bodiesStorageCapacity;
    }

    public static int getEnginesStorageCapacity() {
        return enginesStorageCapacity;
    }

    public static int getCarStorageCapacity() {
        return carStorageCapacity;
    }

    public static int getAccessorySuppliersNumber() {
        return accessorySuppliersNumber;
    }

    public static int getNumberOfWorkers() {
        return numberOfWorkers;
    }

    public static int getDealersNumber() {
        return dealersNumber;
    }

    public static boolean isToLog() {
        return toLog;
    }

    static private int accessoryStorageCapacity;
    static private int bodiesStorageCapacity;
    static private int enginesStorageCapacity;
    static private int carStorageCapacity;
    static private int accessorySuppliersNumber;
    static private int numberOfWorkers;
    static private int dealersNumber;
    static private boolean toLog;

    static private Thread[] threads;
    static private Factory factory;
    static private FactoryLogger logger;

    public static void startFactory(Path configFilePath) throws BadParseException {
        Initializer.parseConfig(configFilePath);

        Storage<Accessory> accessoryStorage = new Storage<>(accessoryStorageCapacity);
        Storage<Body> bodyStorage = new Storage<>(bodiesStorageCapacity);
        Storage<Engine> engineStorage = new Storage<>(enginesStorageCapacity);
        CarStorage carStorage = new CarStorage(carStorageCapacity);

        Supplier<Engine> engineSupplier = new Supplier<Engine>(engineStorage, Engine.class,INIT_SLEEP_TIME);
        Supplier<Body> bodiesSupplier = new Supplier<Body>(bodyStorage, Body.class,INIT_SLEEP_TIME);

        try {
            logger = new FactoryLogger();
            logger.setToLog(toLog);
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }

        threads = new Thread[dealersNumber + accessorySuppliersNumber + NUMBER_OF_THREADS];
        factory = new Factory(accessoryStorage,bodyStorage,engineStorage,carStorage,numberOfWorkers,logger);
        threads[0] = new Thread(bodiesSupplier, "bodiesSupplier");
        threads[1] = new Thread(engineSupplier, "engineSupplier");
        threads[2] = new Thread(new CarStorageController(carStorage,factory), "CarStorageController");

        Supplier[] accessorySuppliers = new Supplier[accessorySuppliersNumber];
        Dealer[] dealers = new Dealer[dealersNumber];

        for(int i = 0 ; i < dealers.length; i++) {
            dealers[i] = new Dealer(carStorage,logger,i);
            threads[i + NUMBER_OF_THREADS] = new Thread(dealers[i], "Dealer#" + String.valueOf(i));
        }
        for(int i = 0 ; i < accessorySuppliers.length; i++) {
            accessorySuppliers[i] = new Supplier<Accessory>(accessoryStorage, Accessory.class,INIT_SLEEP_TIME);
            threads[i + dealersNumber + NUMBER_OF_THREADS] = new Thread(accessorySuppliers[i],"accessorySupplier#" + String.valueOf(i));
        }
        Form form = new Form(new FormStartObjects((int newData) -> {
            for(Supplier supplier : accessorySuppliers) {
                SwingUtilities.invokeLater(() -> supplier.setSleepTime(newData));
            }}, (int newData) -> SwingUtilities.invokeLater(() -> engineSupplier.setSleepTime(newData)),
                (int newData) -> SwingUtilities.invokeLater(() -> bodiesSupplier.setSleepTime(newData)),
                dealersNumber, accessorySuppliersNumber,
                accessoryStorageCapacity, bodiesStorageCapacity, enginesStorageCapacity,
                (int newData) -> SwingUtilities.invokeLater(() -> logger.setToLog(!logger.isToLog())), toLog));

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
        logger.simpleLog("Started");
    }

    private static void parseConfig(Path configFilePath) throws BadParseException {
        try(BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(configFilePath.toFile())))) {
            String line = reader.readLine();
            accessoryStorageCapacity = parseLine(line, accessoryStorageCapacityString);
            line = reader.readLine();
            bodiesStorageCapacity = parseLine(line, bodiesStorageCapacityString);
            line = reader.readLine();
            enginesStorageCapacity = parseLine(line, enginesStorageCapacityString);
            line = reader.readLine();
            carStorageCapacity = parseLine(line, carStorageCapacityString);
            line = reader.readLine();
            accessorySuppliersNumber = parseLine(line, accessorySuppliersNumberString);
            line = reader.readLine();
            numberOfWorkers = parseLine(line, numberOfWorkersString);
            line = reader.readLine();
            dealersNumber = parseLine(line, dealersNumberString);
            line = reader.readLine();
            if(line.substring(0,toLogString.length()).equals(toLogString)) {
                if(line.substring(toLogString.length()+1).equals("true")) {
                    toLog = true;
                } else if(line.substring(toLogString.length()+1).equals("false")) {
                    toLog = false;
                }
            } else {
                throw new BadParseException("Bad parse at: " + line);
            }
        } catch (IOException e) {
            throw new BadParseException("",e);
        }
    }

    private static int parseLine(String line, String template) throws BadParseException {
        if(line.substring(0,template.length()).equals(template)) {
            int tmp;
            try {
                tmp = Integer.parseInt(line.substring(template.length() + 1));
            } catch (NumberFormatException e) {
                throw new BadParseException("Bad parse at: " + line);
            }
            if(tmp <= 0) {
                throw new BadParseException("Bad parse at: " + line);
            }
            return tmp;
        } else {
            throw new BadParseException("Bad parse at: " + line);
        }
    }

    public static void finish() {
        factory.stop();
        for(Thread t : threads) {
            t.interrupt();
        }
        logger.simpleLog("Finished");
    }
}
