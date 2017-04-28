package ru.nsu.ccfit.pleshkov.lab2.factory;

import ru.nsu.ccfit.pleshkov.lab2.Form;
import ru.nsu.ccfit.pleshkov.lab2.controller.Controller;

import java.io.*;
import java.nio.file.Path;

public class Initializer {
    final static private int INIT_SLEEP_TIME = 100;
    final static private int NUMBER_OF_THREADS = 3;

    final static private String accessoryStorageCapacityString = "accessoryStorageCapacity";
    final static private String bodiesStorageCapacityString = "bodiesStorageCapacity";
    final static private String enginesStorageCapacityString = "enginesStorageCapacity";
    final static private String carStorageCapacityString = "carStorageCapacity";
    final static private String accessorySuppliersNumberString = "accessorySuppliersNumber";
    final static private String numberOfWorkersString = "numberOfWorkers";
    final static private String dealersNumberString = "dealersNumber";
    final static private String toLogString = "toLog";

    static private int accessoryStorageCapacity;
    static private int bodiesStorageCapacity;
    static private int enginesStorageCapacity;
    static private int carStorageCapacity;
    static private int accessorySuppliersNumber;
    static private int numberOfWorkers;
    static private int dealersNumber;
    static private boolean toLog;

    public static void startFactory(Path configFilePath) throws BadParseException {
        Initializer.parseConfig(configFilePath);

        Storage<Accessory> accessoryStorage = new Storage<>(accessoryStorageCapacity);
        Storage<Body> bodyStorage = new Storage<>(bodiesStorageCapacity);
        Storage<Engine> engineStorage = new Storage<>(enginesStorageCapacity);
        CarStorage carStorage = new CarStorage(carStorageCapacity);

        Supplier<Engine> engineSupplier = new Supplier<Engine>(engineStorage, Engine.class,INIT_SLEEP_TIME);
        Supplier<Body> bodiesSupplier = new Supplier<Body>(bodyStorage, Body.class,INIT_SLEEP_TIME);

        Thread[] threads = new Thread[dealersNumber + accessorySuppliersNumber + NUMBER_OF_THREADS];
        Factory factory = new Factory(accessoryStorage,bodyStorage,engineStorage,carStorage,numberOfWorkers);
        threads[0] = new Thread(bodiesSupplier, "bodiesSupplier");
        threads[1] = new Thread(engineSupplier, "engineSupplier");
        threads[2] = new Thread(new CarStorageController(carStorage,factory), "CarStorageController");

        FactoryLogger logger = null;
        try {
            logger = new FactoryLogger();
            logger.setToLog(toLog);
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }

        Supplier<Accessory>[] accessorySuppliers = new Supplier[accessorySuppliersNumber];
        Dealer[] dealers = new Dealer[dealersNumber];

        for(int i = 0 ; i < dealers.length; i++) {
            dealers[i] = new Dealer(carStorage,logger,i);
            threads[i + NUMBER_OF_THREADS] = new Thread(dealers[i], "Dealer#" + String.valueOf(i));
        }
        for(int i = 0 ; i < accessorySuppliers.length; i++) {
            accessorySuppliers[i] = new Supplier<Accessory>(accessoryStorage, Accessory.class,INIT_SLEEP_TIME);
            threads[i + dealersNumber + NUMBER_OF_THREADS] = new Thread(accessorySuppliers[i],"accessorySupplier#" + String.valueOf(i));
        }
        Form form = new Form();
        Controller controller = new Controller(accessoryStorage,bodyStorage,engineStorage,dealers,
                engineSupplier,bodiesSupplier,accessorySuppliers,form, threads, factory,logger);
        form.setController(controller);
        for(Thread t : threads) {
            t.start();
        }
    }

    private static void parseConfig(Path configFilePath) throws BadParseException {
        try(BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(configFilePath.toFile())))) {
            String line = reader.readLine();
            if(line.substring(0,accessoryStorageCapacityString.length()).equals(accessoryStorageCapacityString)) {
                accessoryStorageCapacity = Integer.parseInt(line.substring(accessoryStorageCapacityString.length()+1));
            } else {
                throw new BadParseException("Bad parse at: " + line);
            }
            line = reader.readLine();
            if(line.substring(0,bodiesStorageCapacityString.length()).equals(bodiesStorageCapacityString)) {
                bodiesStorageCapacity = Integer.parseInt(line.substring(bodiesStorageCapacityString.length()+1));
            } else {
                throw new BadParseException("Bad parse at: " + line);
            }
            line = reader.readLine();
            if(line.substring(0,enginesStorageCapacityString.length()).equals(enginesStorageCapacityString)) {
                enginesStorageCapacity = Integer.parseInt(line.substring(enginesStorageCapacityString.length()+1));
            } else {
                throw new BadParseException("Bad parse at: " + line);
            }
            line = reader.readLine();
            if(line.substring(0,carStorageCapacityString.length()).equals(carStorageCapacityString)) {
                carStorageCapacity = Integer.parseInt(line.substring(carStorageCapacityString.length()+1));
            } else {
                throw new BadParseException("Bad parse at: " + line);
            }
            line = reader.readLine();
            if(line.substring(0,accessorySuppliersNumberString.length()).equals(accessorySuppliersNumberString)) {
                accessorySuppliersNumber = Integer.parseInt(line.substring(accessorySuppliersNumberString.length()+1));
            } else {
                throw new BadParseException("Bad parse at: " + line);
            }
            line = reader.readLine();
            if(line.substring(0,numberOfWorkersString.length()).equals(numberOfWorkersString)) {
                numberOfWorkers = Integer.parseInt(line.substring(numberOfWorkersString.length()+1));
            } else {
                throw new BadParseException("Bad parse at: " + line);
            }
            line = reader.readLine();
            if(line.substring(0,dealersNumberString.length()).equals(dealersNumberString)) {
                dealersNumber = Integer.parseInt(line.substring(dealersNumberString.length()+1));
            } else {
                throw new BadParseException("Bad parse at: " + line);
            }
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
}
