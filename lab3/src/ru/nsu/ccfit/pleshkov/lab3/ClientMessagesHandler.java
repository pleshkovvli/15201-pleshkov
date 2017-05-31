package ru.nsu.ccfit.pleshkov.lab3;

import java.io.*;
import java.net.Socket;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

abstract class ClientMessagesHandler extends MessagesHandler {
    BlockingQueue<Message> getQueue() {
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

    private BlockingQueue<Message> queue = new ArrayBlockingQueue<>(100);

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
    protected void initWriting() throws IOException, InterruptedException {
        Message message = queue.take();
        Client.setLogin(message.getMessage());
        setSessionID(message.getSessionID());
        Client.getGui().startMessages();
        writeMessage(new Message(Client.getLogin(),MessageType.LOGIN));
    }

    @Override
    protected Message getMessage() throws IOException, InterruptedException {
        return queue.take();

    }

    @Override
    protected void handleMessage(Message message) throws IOException {
        String mes = message.getMessage();
        if(message.getType() == MessageType.LIST) {
            Client.getGui().updateText("LIST: " + mes);
        }
        if(message.getType() == MessageType.MESSAGE) {
            Client.getGui().updateText(message.getSender() + ": " + mes);
        }
        if(message.getType() == MessageType.USERLOGIN) {
            Client.getGui().updateText(mes + " joined the chat");
        }
        if(message.getType() == MessageType.USERLOGOUT) {
            Client.getGui().updateText(mes + " left the chat");
        }
        if(message.getType() == MessageType.RELOGIN) {
            Client.getGui().updateText(mes.substring(0,mes.indexOf('>')) + " relogged in as "
                    + mes.substring(mes.indexOf('>') + 1));
        }
    }
}