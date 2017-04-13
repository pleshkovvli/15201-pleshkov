package ru.nsu.ccfit.pleshkov.lab2.factory;

public class StorageObserver implements Observer {
    public int getCount() {
        return count;
    }

    private int count;

    @Override
    public void update(int newData) {
        count = newData;
    }

    StorageObserver(Storage storage) {
        storage.addObserver(this);
        count = storage.getCount();
    }
}
