package ru.nsu.ccfit.pleshkov.lab3.messages;

import ru.nsu.ccfit.pleshkov.lab3.ClientMessagesProcessor;

public class ClientChatMessage implements ClientMessage {
    final private String message;

    public ClientChatMessage(String message, int sessionID) {
        this.message = message;
        this.sessionID = sessionID;
    }

    public String getMessage() {
        return message;
    }

    public int getSessionID() {
        return sessionID;
    }

    final private int sessionID;

    @Override
    public void process(ClientMessagesProcessor handler) {
        handler.process(this);
    }
}
