package ru.nsu.ccfit.pleshkov.lab3;

interface SimpleObservable {
    void addSimpleObserver(SimpleObserver observer);
    void notifySimpleObservers();
    void removeSimpleObserver(SimpleObserver observer);
}
