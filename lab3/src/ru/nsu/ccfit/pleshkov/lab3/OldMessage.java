package ru.nsu.ccfit.pleshkov.lab3;

import java.io.Serializable;

class OldMessage implements Serializable {
    OldMessage(String message, MessageType type, int sessionID) {
        this.message = message;
        this.type = type;
        this.sessionID = sessionID;
    }

    OldMessage(String message, MessageType type) {
        this.message = message;
        this.type = type;
    }

    OldMessage(String message, MessageType type, String sender) {
        this.message = message;
        this.type = type;
        this.sender = sender;
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

    void setSender(String sender) {
        this.sender = sender;
    }

    private String sender;

    MessageType getType() {
        return type;
    }

    final private MessageType type;

    int getSessionID() {
        return sessionID;
    }

    private int sessionID;
}
