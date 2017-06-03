package ru.nsu.ccfit.pleshkov.lab3;

public class ServerUserlogoutMessage extends ServerMessage {
    public ServerUserlogoutMessage(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    final private String name;
}
