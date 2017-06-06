package ru.nsu.ccfit.pleshkov.lab3;

class ServerSuccessLoginMessage implements ServerMessage {
    int getSessionID() {
        return sessionID;
    }

    ServerSuccessLoginMessage(int sessionID) {

        this.sessionID = sessionID;
    }

    final private int sessionID;

    @Override
    public void process(ServerMessagesProcessor handler) {
        handler.process(this);
    }

}
