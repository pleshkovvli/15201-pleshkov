package ru.nsu.ccfit.pleshkov.lab3;

import java.io.Serializable;

public class Message implements Serializable {
    public Message(String message, MessageType type, String sender) {
        this.message = message;
        this.type = type;
        this.sender = sender;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    private String message;

    public String getSender() {
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
}
