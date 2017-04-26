package ru.nsu.ccfit.pleshkov.lab2.factory;

import ru.nsu.ccfit.pleshkov.lab2.Form;
import ru.nsu.ccfit.pleshkov.lab2.controller.Controller;

public class Main {

    public static void main(String[] args) {
        Thread[] threads = new Thread[5];
        initialize(threads);
        startJob(threads);
        //try {
        //    while (true) {
        //        Thread.sleep(1000);
        //        printer.printProfit();
        //    }
        //} catch (InterruptedException e) {
        //    e.printStackTrace();
        //}
    }

    private static void initialize(Thread[] threads) {
        Storage<Accessory> accessoryStorage = new Storage<>(34);
        Storage<Body> bodyStorage = new Storage<>(14);
        Storage<Engine> engineStorage = new Storage<>(9);
        CarStorage carStorage = new CarStorage(112);
        Supplier<Engine> engineSupplier = new Supplier<Engine>(engineStorage, Engine.class,5);
        Supplier<Body> bodiesSupplier = new Supplier<Body>(bodyStorage, Body.class,40);
        Supplier<Accessory> accessorySupplier = new Supplier<Accessory>(accessoryStorage, Accessory.class,40);
        threads[0] = new Thread(bodiesSupplier);
        threads[1] = new Thread(accessorySupplier);
        threads[2] = new Thread(engineSupplier);
        Factory factory = new Factory(accessoryStorage,bodyStorage,engineStorage,carStorage,4,30);
        threads[3] = new Thread(new CarStorageController(carStorage,factory));
        Dealer dealer = new Dealer(carStorage,0);
        threads[4] = new Thread(dealer);
        Form form = new Form();
        Controller controller = new Controller(accessoryStorage,bodyStorage,engineStorage,dealer,
                engineSupplier,bodiesSupplier,accessorySupplier,form, threads, factory);
        form.setController(controller);
        //return new InfoPrinter(dealer,accessoryStorage,bodyStorage,engineStorage,carStorage);
    }

    private static void startJob(Thread[] threads) {
        for(Thread thread : threads) {
            thread.start();
        }
    }
}
