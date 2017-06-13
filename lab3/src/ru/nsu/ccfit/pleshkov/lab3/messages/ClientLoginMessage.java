package ru.nsu.ccfit.pleshkov.lab3.messages;

import ru.nsu.ccfit.pleshkov.lab3.base.ClientMessagesProcessor;

public class ClientLoginMessage implements ClientMessage {
    public ClientLoginMessage(String userName, String clientName) {
        this.userName = userName;
        this.clientName = clientName;
    }

    public String getUserName() {
        return userName;
    }

    public String getClientName() {
        return clientName;
    }

    final private String userName;
    final private String clientName;

    @Override
    public void process(ClientMessagesProcessor handler) {
        handler.process(this);
    }
}
