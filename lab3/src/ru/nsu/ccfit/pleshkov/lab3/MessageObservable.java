package ru.nsu.ccfit.pleshkov.lab3;

interface MessageObservable<T extends Message> {
    void addMessageObserver(MessageObserver<T> observer);
    void notifyMessageObservers(T message);
    void removeMessageObserver(MessageObserver<T> observer);
}
