package ru.nsu.ccfit.pleshkov.lab3.messages;

import ru.nsu.ccfit.pleshkov.lab3.ClientMessagesProcessor;

public class ClientLogoutMessage implements ClientMessage {
    public int getSessionID() {
        return sessionID;
    }

    public ClientLogoutMessage(int sessionID) {

        this.sessionID = sessionID;
    }

    final private int sessionID;

    @Override
    public void process(ClientMessagesProcessor handler) {
        handler.process(this);
    }
}
