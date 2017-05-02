package ru.nsu.ccfit.pleshkov.lab2.factory;

import ru.nsu.ccfit.pleshkov.lab2.utils.BlockingQueue;

import java.util.ArrayList;

class Storage<T> extends BlockingQueue<T> implements Observable {
    private ArrayList<Observer> observers = new ArrayList<>();

    Storage(int capacity) {
        super(capacity);
    }
    @Override
    public T dequeue() throws InterruptedException {
        T item = super.dequeue();
        notifyObservers();
        return item;
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
            observer.update(getCount());
        }
    }
}
