package ru.nsu.ccfit.pleshkov.lab3;

class ServerUserloginMessage extends ServerMessage {
    ServerUserloginMessage(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    final private String name;
}
