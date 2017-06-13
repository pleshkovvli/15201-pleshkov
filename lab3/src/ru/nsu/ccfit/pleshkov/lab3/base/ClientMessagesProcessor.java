package ru.nsu.ccfit.pleshkov.lab3.base;

import ru.nsu.ccfit.pleshkov.lab3.messages.ClientChatMessage;
import ru.nsu.ccfit.pleshkov.lab3.messages.ClientListMessage;
import ru.nsu.ccfit.pleshkov.lab3.messages.ClientLoginMessage;
import ru.nsu.ccfit.pleshkov.lab3.messages.ClientLogoutMessage;

public interface ClientMessagesProcessor {
    void process(ClientChatMessage message);
    void process(ClientListMessage message);
    void process(ClientLoginMessage message);
    void process(ClientLogoutMessage message);
}
