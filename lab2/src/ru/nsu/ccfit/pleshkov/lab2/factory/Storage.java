package ru.nsu.ccfit.pleshkov.lab2.factory;

import ru.nsu.ccfit.pleshkov.lab2.utils.BlockingQueue;

import java.util.LinkedList;

public class Storage<T> extends BlockingQueue<T> implements Observable {
    private LinkedList<Observer> observers = new LinkedList<>();

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
