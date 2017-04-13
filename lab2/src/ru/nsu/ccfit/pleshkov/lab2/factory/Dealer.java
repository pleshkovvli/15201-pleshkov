package ru.nsu.ccfit.pleshkov.lab2.factory;

public class Dealer implements Runnable {
    private CarStorage storage;
    private long profit = 0;
    private int sleepTime;

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
                }

            } catch (InterruptedException e) {

            }
        }
    }

    Dealer(CarStorage storage, int sleepTime) {
        this.storage = storage;
        this.sleepTime = sleepTime;
    }

    long getProfit() {
        return profit;
    }

}
