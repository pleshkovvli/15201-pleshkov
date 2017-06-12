package ru.nsu.ccfit.pleshkov.lab3.messages;

import ru.nsu.ccfit.pleshkov.lab3.base.ServerMessagesProcessor;

public interface ServerMessage extends Message {
    void process(ServerMessagesProcessor handler);
}