package ru.nsu.ccfit.pleshkov.lab2.factory;

import java.util.LinkedList;

public class Dealer implements Runnable, Observable {
    private CarStorage storage;
    private int profit = 0;
    private FactoryLogger logger;
    private int number;

    private LinkedList<Observer> observers = new LinkedList<>();

    public void run() {
        while(true) {
            try {
                Car car = storage.dequeue();
                if(car!=null) {
                    profit++;
                    notifyObservers();
                }
                try {
                    logger.log(number, car);
                } catch (Exception e) {

                }
            } catch (InterruptedException e) {
                break;
            }
        }
    }

    Dealer(CarStorage storage, FactoryLogger logger, int number) {
        this.storage = storage;
        this.logger = logger;
        this.number = number;
    }

    private int getProfit() {
        return profit;
    }

    @Override
    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    @Override
    public void notifyObservers() {
        for(Observer observer : observers) {
            observer.update(getProfit());
        }
    }
}
