package ru.nsu.ccfit.pleshkov.lab3;

public class ServerChatMessage extends ServerMessage {
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
}
