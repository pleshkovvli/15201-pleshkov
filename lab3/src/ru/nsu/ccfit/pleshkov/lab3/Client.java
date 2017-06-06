package ru.nsu.ccfit.pleshkov.lab3;

import org.omg.CORBA.TIMEOUT;

import java.util.ArrayList;

class Client implements ServerMessagesProcessor {
    final static private int TIMEOUT = 1000;

    void setUnsetHandler(boolean unsetHandler) {
        this.unsetHandler = unsetHandler;
    }

    private boolean unsetHandler = true;

    Object getLock() {
        return lock;
    }

    final private Object lock = new Object();

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


    void addChatMessage(String message)  {
        handler.addChatMessage(message);
    }

    void addLoginMessage(String name)  {
        handler.addLoginMessage(name);
    }

    void addLogoutMessage()  {
        handler.addLogoutMessage();
    }

    void addListMessage() {
        handler.addListMessage();
    }

    void endIt() {
        handler.endIt();
    }

    void begin() {
        handler.begin("Writer", "Reader",TIMEOUT);
    }

    void waitEnd() {
        synchronized (lock) {
            try {
                while (unsetHandler) {
                    lock.wait();
                }
            } catch (InterruptedException e) {
                return;
            }
        }
        handler.waitEnd();
        unsetHandler = true;
    }

    @Override
    public void process(ServerChatMessage message) {
        gui.showMessage(message.getMessage(),message.getName());
    }

    @Override
    public void process(ServerSuccessMessage message) {
        gui.showSuccess();
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
        gui.showList(builder.toString());
    }

    @Override
    public void process(ServerSuccessLoginMessage message) {
        handler.setSessionID(message.getSessionID());
        gui.startMessages();
    }

    @Override
    public void process(ServerUserloginMessage message) {
        gui.showUserlogin(message.getName(),message.getType());
    }

    @Override
    public void process(ServerUserlogoutMessage message) {
        gui.showUserlogout(message.getName());
    }

    @Override
    public void process(ServerErrorMessage errorMessage) {
        gui.showError(errorMessage.getReason());
    }

}
