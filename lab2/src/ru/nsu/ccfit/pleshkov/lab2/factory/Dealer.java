package ru.nsu.ccfit.pleshkov.lab2.factory;

import java.util.LinkedList;

public class Dealer implements Runnable, Observable {
    private CarStorage storage;
    private int profit = 0;
    private int sleepTime;

    private LinkedList<Observer> observers = new LinkedList<>();

    public int getSleepTime() {
        return sleepTime;
    }

    public void setSleepTime(int sleepTime) {
        this.sleepTime = sleepTime;
    }

    public void run() {
        while(true) {
            try {
                Thread.sleep(sleepTime);
                Car car = storage.dequeue();
                if(car!=null) {
                    profit++;
                    notifyObservers();
                }

            } catch (InterruptedException e) {
                break;
            }
        }
    }

    Dealer(CarStorage storage, int sleepTime) {
        this.storage = storage;
        this.sleepTime = sleepTime;
    }

    int getProfit() {
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
