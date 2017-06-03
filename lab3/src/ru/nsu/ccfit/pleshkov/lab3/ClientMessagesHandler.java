package ru.nsu.ccfit.pleshkov.lab3;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

abstract class ClientMessagesHandler extends MessagesHandler<ServerMessage, ClientMessage> {
    BlockingQueue<ClientMessage> getQueue() {
        return queue;
    }

    ClientMessagesHandler(Socket socket) throws IOException {
        super(socket);
    }

    int getSessionID() {
        return sessionID;
    }

    void setSessionID(int sessionID) {
        this.sessionID = sessionID;
    }

    private int sessionID;

    private BlockingQueue<ClientMessage> queue = new ArrayBlockingQueue<>(100);

    @Override
    protected void fin() {
        try {
            getReader().join();
            getWriter().join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected ClientMessage getMessage() throws IOException, InterruptedException {
        return queue.take();

    }

    protected void process(ServerChatMessage message) {
        Client.getGui().updateText(message.getName() + ": " + message.getMessage());
    }

    protected void process(ServerSuccessListMessage message) {
        ArrayList<User> list = message.getListusers();
        StringBuilder builder = new StringBuilder();
        for(User user : list) {
            builder.append(user.getName());
            builder.append(" via ");
            builder.append(user.getType());
            builder.append("\n");
        }
        Client.getGui().updateText(builder.toString());
    }

    protected void process(ServerSuccessLoginMessage message) {

    }

    protected void process(ServerUserloginMessage message) {
        Client.getGui().updateText(message.getName() + " joined the chat");
    }

    protected void process(ServerUserlogoutMessage message) {
        Client.getGui().updateText(message.getName() + " left the chat");
    }

    protected void process(ServerErrorMessage errorMessage) {

    }
    @Override
    protected void handleMessage(ServerMessage message) throws IOException {
        message.process(this);
    }
}