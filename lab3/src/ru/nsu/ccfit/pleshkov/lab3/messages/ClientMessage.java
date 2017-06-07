package ru.nsu.ccfit.pleshkov.lab3.messages;

import ru.nsu.ccfit.pleshkov.lab3.ClientMessagesProcessor;

public interface ClientMessage extends Message {
    void process(ClientMessagesProcessor handler);
}
