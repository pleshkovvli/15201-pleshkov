package ru.nsu.ccfit.pleshkov.lab3;

public class ClientLogoutMessage extends ClientMessage {
    public int getSessionID() {
        return sessionID;
    }

    public ClientLogoutMessage(int sessionID) {

        this.sessionID = sessionID;
    }

    final private int sessionID;
}
