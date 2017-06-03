package ru.nsu.ccfit.pleshkov.lab3;

public interface MessageObservable {
    void addObserver(MessageObserver observer);
    void notifyObservers();
    void removeObserver(MessageObserver observer);
}
