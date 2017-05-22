package ru.nsu.ccfit.pleshkov.lab2.pool;

import ru.nsu.ccfit.pleshkov.lab2.utils.BlockingQueue;

public class ThreadPool {
    private Thread[] pool;
    private BlockingQueue<Runnable> queue = new BlockingQueue<>(100);

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
                    Runnable runnable = queue.dequeue();
                    runnable.run();
                }
            } catch (InterruptedException e) {

            }
        }
    }
}
