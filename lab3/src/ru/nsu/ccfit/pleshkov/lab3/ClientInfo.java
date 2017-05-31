package ru.nsu.ccfit.pleshkov.lab3;

class ClientInfo {
    private String name;
    private ServerObjectMessagesHandler handler;
    private int sessionID;
    private boolean isOnline;

    int getSessionID() {
        return sessionID;
    }

    void setSessionID(int sessionID) {
        this.sessionID = sessionID;
    }

    boolean isOnline() {
        return isOnline;
    }

    void setOnline(boolean online) {
        isOnline = online;
    }

    String getName() {
        return name;
    }

    void setName(String name) {
        this.name = name;
    }

    ServerObjectMessagesHandler getHandler() {
        return handler;
    }

    void setHandler(ServerObjectMessagesHandler handler) {
        this.handler = handler;
        handler.begin("W","R");
    }

    ClientInfo(String name, ServerObjectMessagesHandler handler) {
        this.name = name;
        this.handler = handler;
    }
}
