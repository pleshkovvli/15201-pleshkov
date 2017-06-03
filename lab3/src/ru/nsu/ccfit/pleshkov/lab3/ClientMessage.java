package ru.nsu.ccfit.pleshkov.lab3;

abstract class ClientMessage implements Message {
    @Override
    public void process(MessagesHandler handler) {
        handler.process(this);
    }
}
