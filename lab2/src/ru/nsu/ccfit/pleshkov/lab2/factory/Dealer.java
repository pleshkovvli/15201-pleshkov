package ru.nsu.ccfit.pleshkov.lab2.factory;

import java.io.IOException;
import java.util.LinkedList;

class Dealer implements Runnable, Observable {
    private CarStorage storage;
    private int profit = 0;
    private int number;

    public void setLogger(FactoryLogger logger) {
        this.logger = logger;
    }

    private FactoryLogger logger = null;

    private LinkedList<Observer> observers = new LinkedList<>();

    public void run() {
        try {
        while(!Thread.interrupted()) {
                Car car = storage.dequeue();
                if(car!=null) {
                    profit++;
                    notifyObservers();
                }
                if(logger != null) {
                    logger.log(number, car);
                }
            }
        } catch (InterruptedException e) {
            logger.log(Thread.currentThread().getName() + " was interrupted");
        }
    }

    Dealer(CarStorage storage, FactoryLogger logger, int number)  {
        this.logger = logger;
        this.storage = storage;
        this.number = number;
    }

    Dealer(CarStorage storage, int number)  {
        this.storage = storage;
        this.number = number;
        try {
            this.logger = FactoryLogger.getLogger();
        } catch (IOException e) {
            System.err.println(Thread.currentThread().getName() + " failed to get logger");
        }
    }

    private int getProfit() {
        return profit;
    }

    @Override
    public void removeObserver(Observer observer) {
        if(observers.contains(observer)) {
            observers.remove(observer);
        }
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
