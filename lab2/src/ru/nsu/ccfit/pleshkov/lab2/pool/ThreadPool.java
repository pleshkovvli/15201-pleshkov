package ru.nsu.ccfit.pleshkov.lab2.pool;

import ru.nsu.ccfit.pleshkov.lab2.utils.BlockingQueue;

abstract public class ThreadPool {
    private Thread[] pool;
    private BlockingQueue<Runnable> queue;

    abstract public void preDeque();
    abstract public void postDeque();

    public void stopPool() {
        for(Thread t : pool) {
            t.interrupt();
        }
    }

    public void addTask(Runnable task) throws InterruptedException {
        queue.enqueue(task);
    }

    protected ThreadPool(int threads, int queueCapacity) {
        queue = new BlockingQueue<>(queueCapacity);
        pool = new Thread[threads];
    }
    public void start() {
        for(int i = 0; i < pool.length; i++) {
            pool[i] = new Thread(new PoolThreadRunnable(),"Worker #" + (i+1));
            pool[i].start();
        }
    }

    protected ThreadPool(int threads, BlockingQueue<Runnable> queue) {
        this.queue = queue;
        pool = new Thread[threads];
    }

    private class PoolThreadRunnable implements Runnable {
        public void run() {
            try {
                while(!Thread.interrupted()) {
                    preDeque();
                    Runnable runnable = queue.dequeue();
                    postDeque();
                    runnable.run();
                }
            } catch (InterruptedException e) {

            }
        }
    }
}
