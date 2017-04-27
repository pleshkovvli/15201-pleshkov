package ru.nsu.ccfit.pleshkov.lab2.factory;

public class Supplier<T extends IDTraceable> implements Runnable {
    private Storage<T> storage;
    final private Class typeClass;
    private Class[] argsClasses;

    public int getSleepTime() {
        return sleepTime;
    }

    public void setSleepTime(int sleepTime) {
        this.sleepTime = sleepTime;
    }

    private int sleepTime;

    {
        argsClasses = new Class[1];
        argsClasses[0] = Integer.class;
    }

    public void run() {
        try {
            while(true) {
                Thread.sleep(sleepTime);
                T detail = null;
                try {
                    detail = (T) typeClass.newInstance();
                } catch (Exception e) {
                    System.out.println(typeClass + " "  + argsClasses[0]);
                    e.printStackTrace();
                }
                storage.enqueue(detail);
            }
        } catch (InterruptedException e) {

        }
    }

    Supplier(Storage<T> storage, Class typeClass, int sleepTime) {
        this.typeClass = typeClass;
        this.storage = storage;
        this.sleepTime = sleepTime;
    }
}
