package ru.nsu.ccfit.pleshkov.lab2.utils;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class BlockingQueue<T> {
    private List<T> details = new ArrayList<T>();
    final private int max;
    final private Object lock = new Object();

    public T dequeue() throws InterruptedException {
        synchronized (lock) {
            while (details.isEmpty()) {
                lock.wait();
            }
            T detail = details.get(0);
            details.remove(0);
            lock.notifyAll();
            return detail;
        }
    }

    public void enqueue(T detail) throws InterruptedException {
        if(detail==null) {
            return;
        }
        synchronized (lock) {
            while(details.size()==max) {
                lock.wait();
            }
            details.add(detail);
            lock.notifyAll();
        }
    }

    public int getCount() {
        return details.size();
    }

    public int getCapacity() {
        return max;
    }

    public BlockingQueue(int capacity) {
        this.max = capacity;
    }
}
