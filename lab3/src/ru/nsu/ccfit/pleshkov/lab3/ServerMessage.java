package ru.nsu.ccfit.pleshkov.lab3;

interface ServerMessage extends Message {
    void process(ServerMessagesProcessor handler);
}