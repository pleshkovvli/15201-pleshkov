package ru.nsu.ccfit.pleshkov.lab2.factory;

public class InfoPrinter {
    private Dealer dealer;
    private StorageObserver accessoryObserver;
    private StorageObserver bodyObserver;
    private StorageObserver engineObserver;
    private StorageObserver carObserver;

    public void printProfit() {
        System.out.print(dealer.getProfit());
        System.out.print("$ ");
        System.out.print(accessoryObserver.getCount());
        System.out.print("a ");
        System.out.print(bodyObserver.getCount());
        System.out.print("b ");
        System.out.print(engineObserver.getCount());
        System.out.print("e ");
        System.out.print(carObserver.getCount());
        System.out.println("c ");
    }

    InfoPrinter(Dealer dealer, Storage<Accessory> accessories,
                Storage<Body> bodies, Storage<Engine> engines, CarStorage carStorage) {
        this.dealer = dealer;
        accessoryObserver = new StorageObserver(accessories);
        bodyObserver = new StorageObserver(bodies);
        engineObserver = new StorageObserver(engines);
        carObserver = new StorageObserver(carStorage);
    }
}
