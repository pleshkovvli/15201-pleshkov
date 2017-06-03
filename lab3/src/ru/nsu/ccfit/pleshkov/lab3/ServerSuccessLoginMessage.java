package ru.nsu.ccfit.pleshkov.lab3;

public class ServerSuccessLoginMessage extends ServerMessage {
    public int getSessionID() {
        return sessionID;
    }

    public ServerSuccessLoginMessage(int sessionID) {

        this.sessionID = sessionID;
    }

    final private int sessionID;

}
