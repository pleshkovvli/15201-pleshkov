package ru.nsu.ccfit.pleshkov.lab2.pool;

import ru.nsu.ccfit.pleshkov.lab2.factory.Observable;
import ru.nsu.ccfit.pleshkov.lab2.factory.Observer;
import ru.nsu.ccfit.pleshkov.lab2.utils.BlockingQueue;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class ThreadPool implements Observable {
    private Thread[] pool;
    private BlockingQueue<Runnable> queue = new BlockingQueue<>(100);

    private AtomicInteger waiters = new AtomicInteger(0);

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

    public void stopPool() {
        for(Thread t : pool) {
            t.interrupt();
        }
    }

    public void addTask(Runnable task) throws InterruptedException {
        queue.enqueue(task);
    }

    public ThreadPool(int threads) {
        pool = new Thread[threads];
        for(int i = 0; i < threads; i++) {
            pool[i] = new Thread(new PoolThreadRunnable(),"Worker #" + (i+1));
            pool[i].start();
        }
    }

    private class PoolThreadRunnable implements Runnable {
        public void run() {
            try {
                while(!Thread.interrupted()) {
                    waiters.getAndIncrement();
                    notifyObservers();
                    Runnable runnable = queue.dequeue();
                    waiters.getAndDecrement();
                    notifyObservers();
                    runnable.run();
                }
            } catch (InterruptedException e) {

            }
        }
    }
}
