package ru.nsu.ccfit.pleshkov.lab3.client;

import ru.nsu.ccfit.pleshkov.lab3.*;
import ru.nsu.ccfit.pleshkov.lab3.messages.*;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

abstract class ClientMessagesHandler extends MessagesHandler<ServerMessage, ClientMessage>
implements MessageObservable<ServerMessage> {

    private ArrayList<MessageObserver<ServerMessage>> observers = new ArrayList<>();

    final private String clientName;

    @Override
    public void notifyMessageObservers(ServerMessage message) {
        for(MessageObserver<ServerMessage> observer : observers) {
            observer.update(message);
        }
    }

    final private Client client;

    @Override
    public void removeMessageObserver(MessageObserver<ServerMessage> observer) {
        observers.remove(observer);
    }

    @Override
    public void addMessageObserver(MessageObserver<ServerMessage> observer) {
        observers.add(observer);
    }

    ClientMessagesHandler(Socket socket, String clientName, Client client) throws IOException {
        super(socket);
        this.clientName = clientName;
        this.client = client;
    }

    void setSessionID(int sessionID) {
        this.sessionID = sessionID;
    }

    private int sessionID;

    private BlockingQueue<ClientMessage> queue = new ArrayBlockingQueue<>(100);

    void addChatMessage(String message)  {
        try {
            queue.put(new ClientChatMessage(message,sessionID));
        } catch (InterruptedException e) {
            client.showInterruption("Failed to put message in queue");
        }
    }

    @Override
    public void endIt() {
        super.endIt();
        close();
    }

        @Override
    protected void handleUnknownMessage() {
        client.handleUnknownMessage();
    }

    void addLoginMessage(String name)  {
        queue.add(new ClientLoginMessage(name,clientName));
    }

    void addLogoutMessage()  {
        try {
            queue.put(new ClientLogoutMessage(sessionID));
        } catch (InterruptedException e) {
            client.showInterruption("Failed to put logout request in queue");
        }
    }

    @Override
    protected void handleFailedRead() {
        client.showFailedRead();
    }

    void addListMessage() {
        queue.add(new ClientListMessage(sessionID));
    }

    @Override
    protected void handleConnectionBreak() {
        client.showConnectionBreak();
        endIt();
    }


    @Override
    protected void handleInterruption() {

    }

    @Override
    protected void fin() {

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

