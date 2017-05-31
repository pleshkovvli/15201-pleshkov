package ru.nsu.ccfit.pleshkov.lab3;

interface Observable {
    void addObserver(Observer observer);
    void notifyObservers();
    void removeObserver(Observer observer);
}
