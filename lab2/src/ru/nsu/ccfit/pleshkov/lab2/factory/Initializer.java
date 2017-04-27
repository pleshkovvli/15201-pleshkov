package ru.nsu.ccfit.pleshkov.lab2.factory;

import ru.nsu.ccfit.pleshkov.lab2.Form;
import ru.nsu.ccfit.pleshkov.lab2.controller.Controller;

import java.io.*;
import java.nio.file.Path;

public class Initializer {
    final static private int DEFAULT_CAPACITY = 100;
    final static private int DEFAULT_NUMBER = 4;
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
        FactoryLogger logger = null;
        try {
            logger = new FactoryLogger();
        } catch (IOException ex) {

        }
        Storage<Accessory> accessoryStorage = new Storage<>(accessoryStorageCapacity);
        Storage<Body> bodyStorage = new Storage<>(bodiesStorageCapacity);
        Storage<Engine> engineStorage = new Storage<>(enginesStorageCapacity);
        CarStorage carStorage = new CarStorage(carStorageCapacity);
        Supplier<Engine> engineSupplier = new Supplier<Engine>(engineStorage, Engine.class,INIT_SLEEP_TIME);
        Supplier<Body> bodiesSupplier = new Supplier<Body>(bodyStorage, Body.class,INIT_SLEEP_TIME);
        Supplier<Accessory>[] accessorySuppliers = new Supplier[accessorySuppliersNumber];
        Dealer[] dealers = new Dealer[dealersNumber];
        Thread[] threads = new Thread[dealersNumber + accessorySuppliersNumber + NUMBER_OF_THREADS];
        Factory factory = new Factory(accessoryStorage,bodyStorage,engineStorage,carStorage,numberOfWorkers);
        threads[0] = new Thread(bodiesSupplier);
        threads[1] = new Thread(engineSupplier);
        threads[2] = new Thread(new CarStorageController(carStorage,factory));
        for(int i = 0 ; i<dealers.length; i++) {
            dealers[i] = new Dealer(carStorage,logger,i);
            threads[i + NUMBER_OF_THREADS] = new Thread(dealers[i]);
        }
        for(int i = 0 ; i<accessorySuppliers.length; i++) {
            accessorySuppliers[i] = new Supplier<Accessory>(accessoryStorage, Accessory.class,INIT_SLEEP_TIME);
            threads[i + dealersNumber + NUMBER_OF_THREADS] = new Thread(accessorySuppliers[i]);
        }
        Form form = new Form();
        Controller controller = new Controller(accessoryStorage,bodyStorage,engineStorage,dealers,
                engineSupplier,bodiesSupplier,accessorySuppliers,form, threads, factory);
        form.setController(controller);
        for(Thread t : threads) {
            t.start();
        }
    }

    private static void parseConfig(Path configFilePath) throws BadParseException {
        try(BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(configFilePath.toFile())))) {
            String s = reader.readLine();
            System.out.println(s);
            if(s.substring(0,accessoryStorageCapacityString.length()).equals(accessoryStorageCapacityString)) {
                accessoryStorageCapacity = Integer.parseInt(s.substring(accessoryStorageCapacityString.length()+1));
            } else {
                accessoryStorageCapacity = DEFAULT_CAPACITY;
            }
            s = reader.readLine();
            System.out.println(s);
            if(s.substring(0,bodiesStorageCapacityString.length()).equals(bodiesStorageCapacityString)) {
                bodiesStorageCapacity = Integer.parseInt(s.substring(bodiesStorageCapacityString.length()+1));
            } else {
                bodiesStorageCapacity = DEFAULT_CAPACITY;
            }
            s = reader.readLine();
            System.out.println(s);
            if(s.substring(0,enginesStorageCapacityString.length()).equals(enginesStorageCapacityString)) {
                enginesStorageCapacity = Integer.parseInt(s.substring(enginesStorageCapacityString.length()+1));
            } else {
                enginesStorageCapacity = DEFAULT_CAPACITY;
            }
            s = reader.readLine();
            System.out.println(s);
            if(s.substring(0,carStorageCapacityString.length()).equals(carStorageCapacityString)) {
                carStorageCapacity = Integer.parseInt(s.substring(carStorageCapacityString.length()+1));
            } else {
                carStorageCapacity = DEFAULT_CAPACITY;
            }
            s = reader.readLine();
            System.out.println(s);
            if(s.substring(0,accessorySuppliersNumberString.length()).equals(accessorySuppliersNumberString)) {
                accessorySuppliersNumber = Integer.parseInt(s.substring(accessorySuppliersNumberString.length()+1));
            } else {
                accessorySuppliersNumber = DEFAULT_NUMBER;
            }
            s = reader.readLine();
            System.out.println(s);
            if(s.substring(0,numberOfWorkersString.length()).equals(numberOfWorkersString)) {
                numberOfWorkers = Integer.parseInt(s.substring(numberOfWorkersString.length()+1));
            } else {
                numberOfWorkers = DEFAULT_NUMBER;
            }
            s = reader.readLine();
            System.out.println(s);
            if(s.substring(0,dealersNumberString.length()).equals(dealersNumberString)) {
                dealersNumber = Integer.parseInt(s.substring(dealersNumberString.length()+1));
            } else {
                dealersNumber = DEFAULT_NUMBER;
            }
            s = reader.readLine();
            System.out.println(s);
            if(s.substring(0,toLogString.length()).equals(toLogString)) {
                if(s.substring(toLogString.length()+1).equals("true")) {
                    toLog = true;
                } else {
                    toLog = false;
                }
            } else {
                toLog = false;
            }
        } catch (IOException e) {
            throw new BadParseException("",e);
        }
    }
}
