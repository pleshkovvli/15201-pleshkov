package ru.nsu.ccfit.pleshkov.lab3;

abstract class ServerMessage implements Message {
    @Override
    public void process(MessagesHandler handler) {
        handler.process(this);
    }
}