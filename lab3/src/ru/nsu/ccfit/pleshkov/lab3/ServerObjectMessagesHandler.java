package ru.nsu.ccfit.pleshkov.lab3;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class ServerObjectMessagesHandler extends MessagesHandler {
    BlockingQueue<Message> queue = new ArrayBlockingQueue<Message>(100);

    ObjectInputStream messagesReader;
    ObjectOutputStream messagesWriter;
    final private ArrayList<ClientInfo> clients;
    final private Object lock = new Object();

    public ClientInfo getClient() {
        return client;
    }

    public void setClient(ClientInfo client) {
        this.client = client;
    }

    private ClientInfo client;

    public ServerObjectMessagesHandler(Socket socket, ArrayList<ClientInfo> clients, ClientInfo client) throws IOException {
        super(socket);
        this.client = client;
        this.clients = clients;
        messagesWriter = new ObjectOutputStream(socket.getOutputStream());
        messagesWriter.flush();
        messagesReader = new ObjectInputStream(socket.getInputStream());
    }

    @Override
    protected void initWriting() throws IOException {
        synchronized (lock) {
            try {
                while(client.getName() == null) {
                    lock.wait();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
                return;
            }
        }
        writeMessage(new Message(null,MessageType.SUCCESS,Server.serverName));
    }

    @Override
    protected void initReading() throws IOException, FailedReadException {
        synchronized (lock) {
            try {
                Message message =  (Message) messagesReader.readObject();
                if(message.getType() != MessageType.LOGIN) {
                    throw new FailedReadException();
                } else {
                    client.setName(message.getMessage());
                    lock.notifyAll();
                }
            }   catch (ClassNotFoundException e)  {
                throw new FailedReadException(e);
            }
        }
    }

    @Override
    protected void endWriting() {
        try {
            messagesReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void endReading() {
        try {
            messagesWriter.close();
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
    }

    @Override
    protected Message getMessage() throws IOException, FailedWriteException {
        try {
            return queue.take();
        } catch (InterruptedException e) {
            throw new FailedWriteException(e);
        }
    }

    @Override
    protected void handleMessage(Message message) throws IOException {
        for(ClientInfo client : clients) {
            if(client.getHandler() != this) {
                client.getHandler().queue.add(message);
            }
        }
    }

};
