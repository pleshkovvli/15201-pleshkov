package ru.nsu.ccfit.pleshkov.lab2.factory;

public class ConfigData {
    public ConfigData(int accessoryStorageCapacity, int bodiesStorageCapacity, int enginesStorageCapacity, int carStorageCapacity, int accessorySuppliersNumber, int numberOfWorkers, int dealersNumber, boolean toLog) {
        this.accessoryStorageCapacity = accessoryStorageCapacity;
        this.bodiesStorageCapacity = bodiesStorageCapacity;
        this.enginesStorageCapacity = enginesStorageCapacity;
        this.carStorageCapacity = carStorageCapacity;
        this.accessorySuppliersNumber = accessorySuppliersNumber;
        this.numberOfWorkers = numberOfWorkers;
        this.dealersNumber = dealersNumber;
        this.toLog = toLog;
    }

    public int getAccessoryStorageCapacity() {
        return accessoryStorageCapacity;
    }

    public int getBodiesStorageCapacity() {
        return bodiesStorageCapacity;
    }

    public int getEnginesStorageCapacity() {
        return enginesStorageCapacity;
    }

    public int getCarStorageCapacity() {
        return carStorageCapacity;
    }

    public int getAccessorySuppliersNumber() {
        return accessorySuppliersNumber;
    }

    public int getNumberOfWorkers() {
        return numberOfWorkers;
    }

    public int getDealersNumber() {
        return dealersNumber;
    }

    public boolean isToLog() {
        return toLog;
    }

    public void setAccessoryStorageCapacity(int accessoryStorageCapacity) {
        this.accessoryStorageCapacity = accessoryStorageCapacity;
    }

    public void setBodiesStorageCapacity(int bodiesStorageCapacity) {
        this.bodiesStorageCapacity = bodiesStorageCapacity;
    }

    public void setEnginesStorageCapacity(int enginesStorageCapacity) {
        this.enginesStorageCapacity = enginesStorageCapacity;
    }

    public void setCarStorageCapacity(int carStorageCapacity) {
        this.carStorageCapacity = carStorageCapacity;
    }

    public void setAccessorySuppliersNumber(int accessorySuppliersNumber) {
        this.accessorySuppliersNumber = accessorySuppliersNumber;
    }

    public void setNumberOfWorkers(int numberOfWorkers) {
        this.numberOfWorkers = numberOfWorkers;
    }

    public void setDealersNumber(int dealersNumber) {
        this.dealersNumber = dealersNumber;
    }

    public void setToLog(boolean toLog) {
        this.toLog = toLog;
    }

    private int accessoryStorageCapacity;
    private int bodiesStorageCapacity;
    private int enginesStorageCapacity;
    private int carStorageCapacity;
    private int accessorySuppliersNumber;
    private int numberOfWorkers;
    private int dealersNumber;
    private boolean toLog;
}
