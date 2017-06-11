package ru.nsu.ccfit.pleshkov.lab3;

public interface Observable<T> {
    void addObserver(Observer<T> observer);
    void notifyObservers();
    void removeObserver(Observer<T> observer);
}
