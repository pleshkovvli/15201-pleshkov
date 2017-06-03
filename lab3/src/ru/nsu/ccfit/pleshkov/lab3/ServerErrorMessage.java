package ru.nsu.ccfit.pleshkov.lab3;

public class ServerErrorMessage extends ServerMessage {
    public ServerErrorMessage(String reason) {
        this.reason = reason;
    }

    public String getReason() {
        return reason;
    }

    final private String reason;

}
