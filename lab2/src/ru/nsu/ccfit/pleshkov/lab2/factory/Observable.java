package ru.nsu.ccfit.pleshkov.lab2.factory;

public interface Observable {
    void addObserver(Observer observer);
    void notifyObservers();
}
