package ru.nsu.ccfit.pleshkov.lab3;

interface ClientMessagesProcessor {
    void process(ClientChatMessage message);
    void process(ClientListMessage message);
    void process(ClientLoginMessage message);
    void process(ClientLogoutMessage message);
}
