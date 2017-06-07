package ru.nsu.ccfit.pleshkov.lab3;

public interface Observable {
    void addObserver(Observer observer);
    void notifyObservers();
    void removeObserver(Observer observer);
}
