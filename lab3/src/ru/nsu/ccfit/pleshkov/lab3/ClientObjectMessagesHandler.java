package ru.nsu.ccfit.pleshkov.lab3;

import java.io.*;
import java.net.Socket;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

class ClientObjectMessagesHandler extends MessagesHandler {
    BlockingQueue<Message> getQueue() {
        return queue;
    }

    ClientObjectMessagesHandler(Socket socket) throws IOException {
        super(socket);
        messagesReader = new ObjectInputStream(socket.getInputStream());
        messagesWriter = new ObjectOutputStream(socket.getOutputStream());
    }

    int getSessionID() {
        return sessionID;
    }

    void setSessionID(int sessionID) {
        this.sessionID = sessionID;
    }

    private int sessionID;

    private BlockingQueue<Message> queue = new ArrayBlockingQueue<>(100);

    private ObjectInputStream messagesReader;
    private ObjectOutputStream messagesWriter;
    @Override
    protected void endWriting() {
        try {
            messagesWriter.close();
            if(!getSocket().isClosed()) {
                getSocket().close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
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
    protected void initWriting() throws IOException, InterruptedException {
        Message message = queue.take();
        Client.setLogin(message.getMessage());
        Client.getGui().getButton().setLogin(false);
        setSessionID(message.getSessionID());
        Client.getGui().startMessages();
        writeMessage(new Message(Client.getLogin(),MessageType.LOGIN));
    }

    @Override
    protected void initReading() throws IOException, InterruptedException, FailedReadException {
        try {
            Message message =  (Message) messagesReader.readObject();
            if(message.getType() != MessageType.SUCCESS) {
                throw new FailedReadException();
            }
            sessionID = Integer.valueOf(message.getMessage());
        } catch (ClassNotFoundException e)  {
            throw new FailedReadException(e);
        }
    }

    @Override
    protected void endReading() {
        try {
            messagesReader.close();
            Client.getGui().getButton().setLogin(true);
            if(!getSocket().isClosed()) {
                getSocket().close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected Message readMessage() throws IOException, FailedReadException {
        try {
            return (Message) messagesReader.readObject();
        } catch (ClassNotFoundException e)  {
            throw new FailedReadException(e);
        }
    }

    @Override
    protected void writeMessage(Message message) throws IOException {
        messagesWriter.writeObject(message);
        if(message.getType() == MessageType.LOGOUT) {
            Client.getGui().getButton().setLogin(true);
            endIt();
        }
    }

    @Override
    protected Message getMessage() throws IOException, InterruptedException {
        return queue.take();

    }

    @Override
    protected void handleMessage(Message message) throws IOException {
        if(message.getType() == MessageType.LIST) {
            Client.getGui().updateText("LIST: " + message.getMessage());
        }
        if(message.getType() == MessageType.MESSAGE) {
            Client.getGui().updateText(message.getSender() + ": " + message.getMessage());
        }
        if(message.getType() == MessageType.SUCCESS) {
            if(!message.getMessage().isEmpty()) {
                Client.getGui().getButton().setLogin(false);
            }
        }
    }
}