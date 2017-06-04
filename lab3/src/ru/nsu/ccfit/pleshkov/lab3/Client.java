package ru.nsu.ccfit.pleshkov.lab3;

import java.util.ArrayList;

class Client implements ServerMessagesProcessor {

    ClientGUI getGui() {
        return gui;
    }

    void setGui(ClientGUI gui) {
        this.gui = gui;
    }

    private ClientGUI gui;

    ClientMessagesHandler getHandler() {
        return handler;
    }

    void setHandler(ClientMessagesHandler handler) {
        this.handler = handler;
    }

    private ClientMessagesHandler handler;

    @Override
    public void process(ServerChatMessage message) {
        gui.getMessage(message.getMessage(),message.getName());
    }

    @Override
    public void process(ServerSuccessMessage message) {

    }

    @Override
    public void process(ServerSuccessListMessage message) {
        ArrayList<User> list = message.getListusers();
        StringBuilder builder = new StringBuilder();
        for(User user : list) {
            builder.append(user.getName());
            builder.append("$");
            builder.append(user.getType());
            builder.append("\n");
        }
        gui.getList(builder.toString());
    }

    @Override
    public void process(ServerSuccessLoginMessage message) {
        handler.setSessionID(message.getSessionID());
        gui.startMessages();
    }

    @Override
    public void process(ServerUserloginMessage message) {
        gui.getUserlogin(message.getName(),message.getType());
    }

    @Override
    public void process(ServerUserlogoutMessage message) {
        gui.getUserlogout(message.getName());
    }

    @Override
    public void process(ServerErrorMessage errorMessage) {
        gui.getError(errorMessage.getReason());
    }

}
