package ru.nsu.ccfit.pleshkov.lab3;

import java.io.Serializable;

class Message implements Serializable {
    Message(String message, MessageType type, int sessionID) {
        this.message = message;
        this.type = type;
        this.sessionID = sessionID;
    }

    Message(String message, MessageType type, String sender) {
        this.message = message;
        this.type = type;
        this.sender = sender;
    }

    Message(String message, MessageType type) {
        this.message = message;
        this.type = type;
    }

    String getMessage() {
        return message;
    }

    void setMessage(String message) {
        this.message = message;
    }

    private String message;

    String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    private String sender;

    public MessageType getType() {
        return type;
    }

    final private MessageType type;

    public int getSessionID() {
        return sessionID;
    }

    public void setSessionID(int sessionID) {
        this.sessionID = sessionID;
    }

    private int sessionID;
}
