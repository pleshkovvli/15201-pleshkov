package ru.nsu.ccfit.pleshkov.lab2.controller;

import ru.nsu.ccfit.pleshkov.lab2.Form;
import ru.nsu.ccfit.pleshkov.lab2.factory.*;

import javax.swing.*;

public class Controller {
    private CountObserver<Storage<Body>> bodyCountObserver;
    private CountObserver<Storage<Engine>> engineCountObserver;
    private CountObserver<Storage<Accessory>> accessoryCountObserver;
    private CountObserver<Dealer> profitObserver;
    private Supplier<Engine> engineSupplier;
    private Supplier<Body> bodiesSupplier;
    private Supplier<Accessory> accessoriesSupplier;
    private Thread[] threads;
    private Factory factory;

    public static final int MAX_SLEEP_TIME = 2000;

    private Form form;

    public Controller(Storage<Accessory> accessoryStorage, Storage<Body> bodyStorage, Storage<Engine> engineStorage,
                     Dealer dealer, Supplier<Engine> engineSupplier,Supplier<Body> bodiesSupplier,
                      Supplier<Accessory> accessorySupplier, Form form, Thread[] threads, Factory factory) {
        this.factory = factory;
        this.threads = threads;
        this.engineSupplier = engineSupplier;
        this.accessoriesSupplier = accessorySupplier;
        this.bodiesSupplier = bodiesSupplier;
        bodyCountObserver = new SpecificCountObserver<Storage<Body>>(bodyStorage) {
            @Override
            protected void specificJob() {
                form.updateNumberOfBodies(getCount());
            }
        };
        engineCountObserver = new SpecificCountObserver<Storage<Engine>>(engineStorage) {
            @Override
            protected void specificJob() {
                form.updateNumberOfEngines(getCount());
            }
        };
        accessoryCountObserver = new SpecificCountObserver<Storage<Accessory>>(accessoryStorage) {
            @Override
            protected void specificJob() {
                form.updateNumberOfAccessories(getCount());
            }
        };
        profitObserver = new SpecificCountObserver<Dealer>(dealer) {
            @Override
            protected void specificJob() {
                form.updateProfit(getCount());
            }
        };
        this.form = form;
        form.setEnginesSleep(engineSupplier.getSleepTime());
        form.setAccessoriesSleep(engineSupplier.getSleepTime());
        form.setBodiesSleep(engineSupplier.getSleepTime());
    }

    public void changeEnginesSleepTime(int sleep) {
        if((sleep < 0) || (sleep > MAX_SLEEP_TIME)) {
            form.setEnginesSleep(engineSupplier.getSleepTime());
        } else {
            engineSupplier.setSleepTime(sleep);
        }
    }

    public void changeBodiesSleepTime(int sleep) {
        if((sleep < 0) || (sleep > MAX_SLEEP_TIME)) {
            form.setBodiesSleep(bodiesSupplier.getSleepTime());
        } else {
            bodiesSupplier.setSleepTime(sleep);
        }
    }

    public void changeAccessoriesSleepTime(int sleep) {
        if((sleep < 0) || (sleep > MAX_SLEEP_TIME)) {
            form.setAccessoriesSleep(accessoriesSupplier.getSleepTime());
        } else {
            accessoriesSupplier.setSleepTime(sleep);
        }
    }

    abstract private class SpecificCountObserver<T extends Observable> extends CountObserver<T> {
        public SpecificCountObserver(T observable) {
            super(observable);
        }

        abstract protected void specificJob();

        @Override
        public void update(int newData) {
            super.update(newData);
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    specificJob();
                }
            });
        }
    };

    public void finish() {
        for(Thread thread : threads)  {
            thread.interrupt();
        }
        factory.stopPool();
        System.out.println('e');
    }
}
