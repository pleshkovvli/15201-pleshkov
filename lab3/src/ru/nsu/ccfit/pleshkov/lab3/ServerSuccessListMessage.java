package ru.nsu.ccfit.pleshkov.lab3;

import java.util.ArrayList;

class ServerSuccessListMessage extends ServerMessage {
    ServerSuccessListMessage(ArrayList<User> listusers) {
        this.listusers = listusers;
    }

    public ArrayList<User> getListusers() {
        return listusers;
    }

    final private ArrayList<User> listusers;
}
