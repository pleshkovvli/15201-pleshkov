package ru.nsu.ccfit.pleshkov.lab3.messages;

import ru.nsu.ccfit.pleshkov.lab3.base.ServerMessagesProcessor;

public class ServerUserloginMessage implements ServerMessage {
    public ServerUserloginMessage(String name, String type) {
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    final private String name;

    public String getType() {
        return type;
    }

    final private String type;

    @Override
    public void process(ServerMessagesProcessor handler) {
        handler.process(this);
    }
}
