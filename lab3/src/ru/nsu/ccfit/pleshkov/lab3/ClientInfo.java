package ru.nsu.ccfit.pleshkov.lab3;

class ClientInfo {
    private String name;

    String getType() {
        return type;
    }

    private String type;
    private ServerMessagesHandler handler;

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

    ServerMessagesHandler getHandler() {
        return handler;
    }

    ClientInfo(String name, String type, ServerMessagesHandler handler) {
        this.name = name;
        this.type = type;
        this.handler = handler;
    }
}
