package ru.nsu.ccfit.pleshkov.lab3.messages;

import ru.nsu.ccfit.pleshkov.lab3.base.ClientMessagesProcessor;

public class ClientListMessage implements ClientMessage {
    public int getSessionID() {
        return sessionID;
    }

    final private int sessionID;

    public ClientListMessage(int sessionID) {
        this.sessionID = sessionID;
    }

    @Override
    public void process(ClientMessagesProcessor handler) {
        handler.process(this);
    }
}
