package ru.nsu.ccfit.pleshkov.lab3.base;

import ru.nsu.ccfit.pleshkov.lab3.messages.Message;

public interface MessageObservable<T extends Message> {
    void addMessageObserver(MessageObserver<T> observer);
    void notifyMessageObservers(T message);
    void removeMessageObserver(MessageObserver<T> observer);
}
