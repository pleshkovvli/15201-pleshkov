package ru.nsu.ccfit.pleshkov.lab3;

class ServerUserlogoutMessage implements ServerMessage {
    ServerUserlogoutMessage(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    final private String name;

    @Override
    public void process(ServerMessagesProcessor handler) {
        handler.process(this);
    }
}
