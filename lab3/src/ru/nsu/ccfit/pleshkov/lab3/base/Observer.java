package ru.nsu.ccfit.pleshkov.lab3.base;

public interface Observer<T> {
    void update(T message);
}
