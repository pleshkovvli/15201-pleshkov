package ru.nsu.ccfit.pleshkov.lab3.messages;

import ru.nsu.ccfit.pleshkov.lab3.base.ServerMessagesProcessor;

public class ServerErrorMessage implements ServerMessage {
    public ServerErrorMessage(String reason) {
        this.reason = reason;
    }

    public String getReason() {
        return reason;
    }

    final private String reason;

    @Override
    public void process(ServerMessagesProcessor handler) {
        handler.process(this);
    }

}
