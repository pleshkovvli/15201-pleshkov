package ru.nsu.ccfit.pleshkov.lab3.messages;

import ru.nsu.ccfit.pleshkov.lab3.ServerMessagesProcessor;
import ru.nsu.ccfit.pleshkov.lab3.User;

import java.util.ArrayList;

public class ServerSuccessListMessage implements ServerMessage {
    public ServerSuccessListMessage(ArrayList<User> listusers) {
        this.listusers = listusers;
    }

    public ArrayList<User> getListusers() {
        return listusers;
    }

    final private ArrayList<User> listusers;

    @Override
    public void process(ServerMessagesProcessor handler) {
        handler.process(this);
    }
}
