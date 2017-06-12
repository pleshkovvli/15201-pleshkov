package ru.nsu.ccfit.pleshkov.lab3.messages;

import ru.nsu.ccfit.pleshkov.lab3.base.ServerMessagesProcessor;

public class ServerChatMessage implements ServerMessage {
    final private String message;

    public ServerChatMessage(String message, String name) {
        this.message = message;
        this.name = name;
    }

    public String getMessage() {
        return message;
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
