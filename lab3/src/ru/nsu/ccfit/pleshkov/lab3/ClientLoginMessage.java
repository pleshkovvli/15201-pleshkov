package ru.nsu.ccfit.pleshkov.lab3;

class ClientLoginMessage implements ClientMessage {
    ClientLoginMessage(String userName, String clientName) {
        this.userName = userName;
        this.clientName = clientName;
    }

    String getUserName() {
        return userName;
    }

    String getClientName() {
        return clientName;
    }

    final private String userName;
    final private String clientName;

    @Override
    public void process(ClientMessagesProcessor handler) {
        handler.process(this);
    }
}
