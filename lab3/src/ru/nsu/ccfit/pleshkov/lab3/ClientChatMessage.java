package ru.nsu.ccfit.pleshkov.lab3;

public class ClientChatMessage extends ClientMessage {
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
}
