package ru.nsu.ccfit.pleshkov.lab2.factory;

public class CountObserver<T extends Observable> implements Observer {
    public int getCount() {
        return count;
    }

    private int count;

    @Override
    public void update(int newData) {
        count = newData;
    }

    public CountObserver(T observable) {
        observable.addObserver(this);
    }
}
