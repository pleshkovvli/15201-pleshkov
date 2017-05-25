package ru.nsu.ccfit.pleshkov.lab2.factory;

import ru.nsu.ccfit.pleshkov.lab2.pool.ThreadPool;

import java.util.ArrayList;

class Factory implements Observable {
    final private Worker worker;
    private ThreadPool pool;

    private ArrayList<Observer> observers = new ArrayList<>();

    @Override
    public void removeObserver(Observer observer) {
        if(observers.contains(observer)) {
            observers.remove(observer);
            pool.removeObserver(observer);
        }
    }

    @Override
    public void addObserver(Observer observer) {
        observers.add(observer);
        pool.addObserver(observer);
    }

    @Override
    public void notifyObservers() {

    }

    void stop() {
        pool.stopPool();
    }

    void doJob(int number) throws InterruptedException {
        for(int i = 0; i < number; i++) {
            pool.addTask(worker);
        }
    }

    Factory(Storage<Accessory> accessories, Storage<Body> bodies, Storage<Engine> engines,
            CarStorage carStorage, int numberOfThreads) {
        worker = new Worker(accessories,bodies,engines,carStorage);
        pool = new ThreadPool(numberOfThreads);
    }
}
