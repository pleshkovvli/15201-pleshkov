package ru.nsu.ccfit.pleshkov.lab3;

public class ClientListMessage extends ClientMessage {
    public int getSessionID() {
        return sessionID;
    }

    final private int sessionID;

    public ClientListMessage(int sessionID) {
        this.sessionID = sessionID;
    }
}
