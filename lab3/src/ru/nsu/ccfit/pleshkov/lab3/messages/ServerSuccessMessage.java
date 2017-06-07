package ru.nsu.ccfit.pleshkov.lab3.messages;

import ru.nsu.ccfit.pleshkov.lab3.ServerMessagesProcessor;

public class ServerSuccessMessage implements ServerMessage {
    @Override
    public void process(ServerMessagesProcessor handler) {
        handler.process(this);
    }
}
