package ru.nsu.ccfit.pleshkov.lab3;

import ru.nsu.ccfit.pleshkov.lab3.messages.Message;

public interface MessageObserver<T extends Message> {
    void update(T message);
}
