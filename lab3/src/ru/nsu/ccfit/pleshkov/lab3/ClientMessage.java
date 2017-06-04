package ru.nsu.ccfit.pleshkov.lab3;

interface ClientMessage extends Message {
    void process(ClientMessagesProcessor handler);
}
