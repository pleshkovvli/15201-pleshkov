package ru.nsu.ccfit.pleshkov.lab3;

class ClientLoginMessage extends ClientMessage {
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
}
