package ru.nsu.ccfit.pleshkov.lab3.messages;

import ru.nsu.ccfit.pleshkov.lab3.base.ServerMessagesProcessor;

public class ServerUserlogoutMessage implements ServerMessage {
    public ServerUserlogoutMessage(String name) {
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
