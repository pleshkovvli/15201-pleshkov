package ru.nsu.ccfit.pleshkov.lab3;

class ServerUserloginMessage implements ServerMessage {
    ServerUserloginMessage(String name, String type) {
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    final private String name;

    String getType() {
        return type;
    }

    final private String type;

    @Override
    public void process(ServerMessagesProcessor handler) {
        handler.process(this);
    }
}
