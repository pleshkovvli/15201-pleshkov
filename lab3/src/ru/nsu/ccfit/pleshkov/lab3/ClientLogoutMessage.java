package ru.nsu.ccfit.pleshkov.lab3;

class ClientLogoutMessage implements ClientMessage {
    public int getSessionID() {
        return sessionID;
    }

    ClientLogoutMessage(int sessionID) {

        this.sessionID = sessionID;
    }

    final private int sessionID;

    @Override
    public void process(ClientMessagesProcessor handler) {
        handler.process(this);
    }
}
