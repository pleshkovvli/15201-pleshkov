package ru.nsu.ccfit.pleshkov.lab3.messages;

import ru.nsu.ccfit.pleshkov.lab3.base.ServerMessagesProcessor;
import ru.nsu.ccfit.pleshkov.lab3.base.User;

import java.util.ArrayList;
import java.util.List;

public class ServerSuccessListMessage implements ServerMessage {
    public ServerSuccessListMessage(ArrayList<User> listusers) {
        this.listusers = listusers;
    }

    public List<User> getListusers() {
        return listusers;
    }

    final private List<User> listusers;

    @Override
    public void process(ServerMessagesProcessor handler) {
        handler.process(this);
    }
}
