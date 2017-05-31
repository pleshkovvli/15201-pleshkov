package ru.nsu.ccfit.pleshkov.lab3;

class ClientInfo {
    private String name;
    private ServerObjectMessagesHandler handler;

    public int getSessionID() {
        return sessionID;
    }

    private int sessionID;
    private boolean isOnline;

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

    ServerObjectMessagesHandler getHandler() {
        return handler;
    }

    ClientInfo(String name, ServerObjectMessagesHandler handler) {
        this.name = name;
        this.handler = handler;
    }
}
