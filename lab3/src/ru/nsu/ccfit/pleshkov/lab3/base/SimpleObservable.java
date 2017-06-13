package ru.nsu.ccfit.pleshkov.lab3.base;

public interface SimpleObservable {
    void addSimpleObserver(SimpleObserver observer);
    void notifySimpleObservers();
    void removeSimpleObserver(SimpleObserver observer);
}
