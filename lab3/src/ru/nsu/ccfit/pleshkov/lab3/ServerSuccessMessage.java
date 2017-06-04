package ru.nsu.ccfit.pleshkov.lab3;

class ServerSuccessMessage implements ServerMessage {
    @Override
    public void process(ServerMessagesProcessor handler) {
        handler.process(this);
    }
}
