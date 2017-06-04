package ru.nsu.ccfit.pleshkov.lab3;

interface MessageObserver<T extends Message> {
    void update(T message);
}
