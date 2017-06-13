package ru.nsu.ccfit.pleshkov.lab3.client;

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

    Config(String name,int port, InetAddress address, String type) {
        this.port = port;
        this.address = address;
        this.type = type;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    final private String name;

    final private int port;
    final private InetAddress address;
    final private String type;
}
