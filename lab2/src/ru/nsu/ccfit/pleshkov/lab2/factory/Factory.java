package ru.nsu.ccfit.pleshkov.lab2.factory;

import ru.nsu.ccfit.pleshkov.lab2.pool.ThreadPool;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

class Factory {
    final private Worker worker;

    Storage<Runnable> getQueue() {
        return queue;
    }

    final private Storage<Runnable> queue;

    ObservableThreadPool getPool() {
        return pool;
    }

    private ObservableThreadPool pool;

    void stop() {
        pool.stopPool();
    }

    void doJob(int number) throws InterruptedException {
        for(int i = 0; i < number; i++) {
            pool.addTask(worker);
        }
    }

    Factory(Storage<Accessory> accessories, Storage<Body> bodies, Storage<Engine> engines,
            CarStorage carStorage, int numberOfThreads, int capacity) {
        queue = new Storage<>(capacity);
        worker = new Worker(accessories,bodies,engines,carStorage);
        pool = new ObservableThreadPool(numberOfThreads,queue);
        pool.start();
    }

    class ObservableThreadPool extends ThreadPool implements Observable {

        ObservableThreadPool(int numberOfThreads, Storage<Runnable> queue) {
            super(numberOfThreads,queue);
        }

        final private AtomicInteger waiters = new AtomicInteger(0);;

        private ArrayList<Observer> observers = new ArrayList<>();

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
                observer.update(waiters.get());
            }
        }
        @Override
        public void preDeque() {
            waiters.getAndIncrement();
            notifyObservers();
        }

        @Override
        public void postDeque() {
            waiters.getAndDecrement();
            notifyObservers();
        }
    };
}
