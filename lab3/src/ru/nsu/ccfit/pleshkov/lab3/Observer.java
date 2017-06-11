package ru.nsu.ccfit.pleshkov.lab3;

public interface Observer<T> {
    void update(T message);
}
