package ru.nsu.ccfit.pleshkov.lab3.messages;

import ru.nsu.ccfit.pleshkov.lab3.base.ServerMessagesProcessor;

public class ServerSuccessMessage implements ServerMessage {
    @Override
    public void process(ServerMessagesProcessor handler) {
        handler.process(this);
    }
}
