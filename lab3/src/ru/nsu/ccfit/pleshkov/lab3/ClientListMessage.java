package ru.nsu.ccfit.pleshkov.lab3;

class ClientListMessage implements ClientMessage {
    int getSessionID() {
        return sessionID;
    }

    final private int sessionID;

    ClientListMessage(int sessionID) {
        this.sessionID = sessionID;
    }

    @Override
    public void process(ClientMessagesProcessor handler) {
        handler.process(this);
    }
}
