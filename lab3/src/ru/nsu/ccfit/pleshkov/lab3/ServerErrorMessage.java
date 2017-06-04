package ru.nsu.ccfit.pleshkov.lab3;

class ServerErrorMessage implements ServerMessage {
    ServerErrorMessage(String reason) {
        this.reason = reason;
    }

    String getReason() {
        return reason;
    }

    final private String reason;

    @Override
    public void process(ServerMessagesProcessor handler) {
        handler.process(this);
    }

}
