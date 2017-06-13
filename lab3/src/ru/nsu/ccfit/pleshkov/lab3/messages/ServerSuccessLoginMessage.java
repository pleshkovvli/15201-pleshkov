package ru.nsu.ccfit.pleshkov.lab3.messages;

import ru.nsu.ccfit.pleshkov.lab3.base.ServerMessagesProcessor;

public class ServerSuccessLoginMessage implements ServerMessage {
    public int getSessionID() {
        return sessionID;
    }

    public ServerSuccessLoginMessage(int sessionID) {

        this.sessionID = sessionID;
    }

    final private int sessionID;

    @Override
    public void process(ServerMessagesProcessor handler) {
        handler.process(this);
    }

}
