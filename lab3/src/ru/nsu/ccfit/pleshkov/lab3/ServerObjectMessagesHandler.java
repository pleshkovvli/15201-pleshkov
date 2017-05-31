package ru.nsu.ccfit.pleshkov.lab3;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

class ServerObjectMessagesHandler extends MessagesHandler {
    private BlockingQueue<Message> queue = new ArrayBlockingQueue<Message>(100);


    final private static AtomicInteger sessionID = new AtomicInteger(0);

    static private Map<Integer,ClientInfo> clients = new TreeMap<>();

    private ObjectInputStream messagesReader;
    private ObjectOutputStream messagesWriter;
    final private Object lock = new Object();

    ClientInfo getClient() {
        return client;
    }

    public void setClient(ClientInfo client) {
        this.client = client;
    }

    private ClientInfo client;

    ServerObjectMessagesHandler(Socket socket) throws IOException {
        super(socket);
        messagesWriter = new ObjectOutputStream(socket.getOutputStream());
        messagesWriter.flush();
        messagesReader = new ObjectInputStream(socket.getInputStream());
    }

    @Override
    protected void initWriting() throws IOException, InterruptedException {
        /*synchronized (lock) {
            while(client.getName() == null) {
                lock.wait();
            }
        }
        writeMessage(new Message("",MessageType.SUCCESS));
        */
    }

    @Override
    protected void initReading() throws IOException, FailedReadException {
        /*synchronized (lock) {
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
        }*/
    }

    @Override
    protected void endWriting() {
        try {
            messagesReader.close();
            getSocket().close();
            if((client != null) && clients.containsKey(getClient().getSessionID())) {
                clients.remove(getClient().getSessionID());
            }
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
    protected void fin() {}

    @Override
    protected Message readMessage() throws IOException, FailedReadException {
        try {
            Message message =  (Message) messagesReader.readObject();
            return message;
        } catch (ClassNotFoundException e)  {
            throw new FailedReadException(e);
        }
    }

    @Override
    protected void writeMessage(Message message) throws IOException {
        messagesWriter.writeObject(message);
    }

    @Override
    protected Message getMessage() throws IOException, InterruptedException {
        return queue.take();

    }

    @Override
    protected void handleMessage(Message message) throws IOException {
        if(message.getType() == MessageType.MESSAGE) {
            if(client != null) {
                message.setSender(getClient().getName());
            } else {
                client = clients.get(message.getSessionID());
                if(client == null) {
                    message = new Message("You haven't logged in",MessageType.ERROR);
                    queue.add(message);
                    return;
                }
                client.setOnline(true);
            }
            for(ClientInfo client : clients.values()) {
                if(client.getHandler() != this && client.isOnline()) {
                    client.getHandler().queue.add(message);
                }
            }
        }
        if(message.getType() == MessageType.LOGIN) {
            if(client != null) {
                clients.remove(client.getSessionID());
            }
            client = new ClientInfo(message.getMessage(),this);
            int id = sessionID.getAndIncrement();
            client.setSessionID(id);
            client.setOnline(true);
            clients.put(id,client);
            queue.add(new Message(String.valueOf(id),MessageType.SUCCESS));
            return;
        }
        if(message.getType() == MessageType.LOGOUT) {
            clients.get(message.getSessionID()).setOnline(false);
            client = null;
            endIt();
            return;
        }
        if(message.getType() == MessageType.LIST) {
            System.out.println(message.getType());
            StringBuilder builder = new StringBuilder();
            for(ClientInfo client : clients.values()) {
                if(client.isOnline()) {
                    builder.append(client.getName());
                    builder.append("; ");
                }
            }
            message.setMessage(builder.toString().trim());
            queue.add(message);
        }
    }

};
