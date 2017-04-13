package ru.nsu.ccfit.pleshkov.lab2.factory;

public class Main {

    public static void main(String[] args) {
        Supplier<Accessory>[] de = new Supplier[5];

        Thread[] threads = new Thread[5];
        InfoPrinter printer = initialize(threads);
        startJob(threads);
        try {
            while (true) {
                Thread.sleep(1000);
                printer.printProfit();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static InfoPrinter initialize(Thread[] threads) {
        Storage<Accessory> accessoryStorage = new Storage<>(1);
        Storage<Body> bodyStorage = new Storage<>(1);
        Storage<Engine> engineStorage = new Storage<>(1);
        CarStorage carStorage = new CarStorage(1);
        threads[0] = new Thread(new Supplier<Accessory>(accessoryStorage, Accessory.class,0));
        threads[1] = new Thread(new Supplier<Body>(bodyStorage, Body.class,0));
        threads[2] = new Thread(new Supplier<Engine>(engineStorage, Engine.class,0));
        Factory factory = new Factory(accessoryStorage,bodyStorage,engineStorage,carStorage,4,0);
        threads[3] = new Thread(new Controller(carStorage,factory));
        Dealer dealer = new Dealer(carStorage,0);
        threads[4] = new Thread(dealer);
        return new InfoPrinter(dealer,accessoryStorage,bodyStorage,engineStorage,carStorage);
    }

    private static void startJob(Thread[] threads) {
        for(Thread thread : threads) {
            thread.start();
        }
    }
}
