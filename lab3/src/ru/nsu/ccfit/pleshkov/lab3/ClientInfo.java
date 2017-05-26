package ru.nsu.ccfit.pleshkov.lab3;

public class ClientInfo {
    private String name;
    private ServerObjectMessagesHandler handler;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ServerObjectMessagesHandler getHandler() {
        return handler;
    }

    public void setHandler(ServerObjectMessagesHandler handler) {
        this.handler = handler;
        handler.begin("W","R");
    }

    public ClientInfo(String name, ServerObjectMessagesHandler handler) {
        this.name = name;
        this.handler = handler;
    }
}
