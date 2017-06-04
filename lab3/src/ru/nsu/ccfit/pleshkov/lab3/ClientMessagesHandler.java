package ru.nsu.ccfit.pleshkov.lab3;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

abstract class ClientMessagesHandler extends MessagesHandler<ServerMessage, ClientMessage>
implements MessageObservable{
    private ArrayList<MessageObserver> observers = new ArrayList<>();

    final private String clientName;

    @Override
    public void notifyMessageObservers(Message message) {
        for(MessageObserver observer : observers) {
            observer.update(message);
        }
    }

    @Override
    public void removeMessageObserver(MessageObserver observer) {
        observers.remove(observer);
    }

    @Override
    public void addMessageObserver(MessageObserver observer) {
        observers.add(observer);
    }

    ClientMessagesHandler(Socket socket, String clientName) throws IOException {
        super(socket);
        this.clientName = clientName;
    }

    int getSessionID() {
        return sessionID;
    }

    void setSessionID(int sessionID) {
        this.sessionID = sessionID;
    }

    private int sessionID;

    private BlockingQueue<ClientMessage> queue = new ArrayBlockingQueue<>(100);

    void addChatMessage(String message)  {
        queue.add(new ClientChatMessage(message,sessionID));
    }

    void addLoginMessage(String name)  {
        queue.add(new ClientLoginMessage(name,clientName));
    }

    void addLogoutMessage()  {
        queue.add(new ClientLogoutMessage(sessionID));
    }

    void addListMessage() {
        queue.add(new ClientListMessage(sessionID));
    }


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

    @Override
    protected void handleMessage(ServerMessage message) throws IOException {
        notifyMessageObservers(message);
    }
}

