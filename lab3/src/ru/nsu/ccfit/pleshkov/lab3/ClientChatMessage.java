package ru.nsu.ccfit.pleshkov.lab3;

class ClientChatMessage implements ClientMessage {
    final private String message;

    ClientChatMessage(String message, int sessionID) {
        this.message = message;
        this.sessionID = sessionID;
    }

    public String getMessage() {
        return message;
    }

    int getSessionID() {
        return sessionID;
    }

    final private int sessionID;

    @Override
    public void process(ClientMessagesProcessor handler) {
        handler.process(this);
    }
}
