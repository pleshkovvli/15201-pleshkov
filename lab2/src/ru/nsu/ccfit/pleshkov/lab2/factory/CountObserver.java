package ru.nsu.ccfit.pleshkov.lab2.factory;

import javax.swing.*;

abstract class CountObserver implements Observer {
    int getCount() {
        return count;
    }

    private int count;

    abstract protected void specificJob();

    @Override
    public void update(int newData) {
        count = newData;
        SwingUtilities.invokeLater(this::specificJob);
    }

    CountObserver(Observable observable) {
        observable.addObserver(this);
    }
}
