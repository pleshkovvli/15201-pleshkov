package ru.nsu.ccfit.pleshkov.lab3;

import java.net.InetAddress;

class Config {
    int getPort() {
        return port;
    }

    InetAddress getAddress() {
        return address;
    }

    public String getType() {
        return type;
    }

    Config(int port, InetAddress address, String type) {
        this.port = port;
        this.address = address;
        this.type = type;
    }

    final private int port;
    final private InetAddress address;
    final private String type;
}
