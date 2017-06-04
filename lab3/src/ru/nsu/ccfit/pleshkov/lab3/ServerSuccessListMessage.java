package ru.nsu.ccfit.pleshkov.lab3;

import java.util.ArrayList;

class ServerSuccessListMessage implements ServerMessage {
    ServerSuccessListMessage(ArrayList<User> listusers) {
        this.listusers = listusers;
    }

    ArrayList<User> getListusers() {
        return listusers;
    }

    final private ArrayList<User> listusers;

    @Override
    public void process(ServerMessagesProcessor handler) {
        handler.process(this);
    }
}
