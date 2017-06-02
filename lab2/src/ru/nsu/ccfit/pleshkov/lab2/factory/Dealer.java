package ru.nsu.ccfit.pleshkov.lab2.factory;

import java.io.IOException;
import java.util.LinkedList;

class Dealer implements Runnable, Observable {
    private CarStorage storage;
    private int profit = 0;
    private int number;

    int getSleepTime() {
        return sleepTime;
    }

    void setSleepTime(int sleepTime) {
        this.sleepTime = sleepTime;
    }

    private int sleepTime;


    public void setLogger(FactoryLogger logger) {
        this.logger = logger;
    }

    private FactoryLogger logger = null;

    private LinkedList<Observer> observers = new LinkedList<>();

    public void run() {
        try {
        while(!Thread.interrupted()) {
                Thread.sleep(sleepTime);
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

    Dealer(CarStorage storage, int number, int sleepTime)  {
        this.storage = storage;
        this.number = number;
        this.sleepTime = sleepTime;
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
