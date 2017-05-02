package ru.nsu.ccfit.pleshkov.lab2.factory;

public class FormStartObjects {
    public FormStartObjects(Observer accessoriesSleepObserver, Observer enginesSleepObserver, Observer bodiesSleepObserver, int numberOfDealers, int numberOfAccessoriesSuppliers, int accessoriesStorageCapacity, int bodiesStorageCapacity, int enginesStorageCapacity, Observer loggingObserver, boolean toLog) {
        this.accessoriesSleepObserver = accessoriesSleepObserver;
        this.enginesSleepObserver = enginesSleepObserver;
        this.bodiesSleepObserver = bodiesSleepObserver;
        this.numberOfDealers = numberOfDealers;
        this.numberOfAccessoriesSuppliers = numberOfAccessoriesSuppliers;
        this.accessoriesStorageCapacity = accessoriesStorageCapacity;
        this.bodiesStorageCapacity = bodiesStorageCapacity;
        this.enginesStorageCapacity = enginesStorageCapacity;
        this.loggingObserver = loggingObserver;
        this.toLog = toLog;
    }

    public Observer getAccessoriesSleepObserver() {
        return accessoriesSleepObserver;
    }

    public Observer getEnginesSleepObserver() {
        return enginesSleepObserver;
    }

    public Observer getBodiesSleepObserver() {
        return bodiesSleepObserver;
    }

    public int getNumberOfDealers() {
        return numberOfDealers;
    }

    public int getNumberOfAccessoriesSuppliers() {
        return numberOfAccessoriesSuppliers;
    }

    public int getAccessoriesStorageCapacity() {
        return accessoriesStorageCapacity;
    }

    public int getBodiesStorageCapacity() {
        return bodiesStorageCapacity;
    }

    public int getEnginesStorageCapacity() {
        return enginesStorageCapacity;
    }

    public Observer getLoggingObserver() {
        return loggingObserver;
    }

    public boolean isToLog() {
        return toLog;
    }

    final private Observer accessoriesSleepObserver;
    final private Observer enginesSleepObserver;
    final private Observer bodiesSleepObserver;
    final private int numberOfDealers;
    final private int numberOfAccessoriesSuppliers;
    final private int accessoriesStorageCapacity;
    final private int bodiesStorageCapacity;
    final private int enginesStorageCapacity;
    final private Observer loggingObserver;
    final private boolean toLog;
}
