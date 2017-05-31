package ru.nsu.ccfit.pleshkov.lab3;

import java.io.IOException;
import java.net.Socket;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

abstract class ServerMessagesHandler extends MessagesHandler {
    private BlockingQueue<Message> queue = new ArrayBlockingQueue<>(100);

    final private static AtomicInteger sessionID = new AtomicInteger(1);

    static private Map<Integer,ClientInfo> clients = new TreeMap<>();

    private ClientInfo client;

    ServerMessagesHandler(Socket socket) {
        super(socket);
    }

    @Override
    protected void initWriting() throws IOException, InterruptedException {

    }

    @Override
    protected void initReading() throws IOException, FailedReadException {

    }

    @Override
    protected void endReading() {
        if(client != null) {
            client.setOnline(false);
        }
    }

    @Override
    protected void endWriting() {
        if(client != null) {
            client.setOnline(false);
        }
    }

    @Override
    protected void fin() {}

    @Override
    protected Message getMessage() throws IOException, InterruptedException {
        return queue.take();

    }

    @Override
    protected void handleMessage(Message message) throws IOException {
        if(message.getType() == MessageType.MESSAGE) {
            if(client != null) {
                message.setSender(client.getName());
            } else {
                client = clients.get(message.getSessionID());
                if(client == null) {
                    message = new Message("You haven't logged in yet",MessageType.ERROR);
                    queue.add(message);
                    return;
                }
                client.setOnline(true);
            }
            for(ClientInfo client : clients.values()) {
                if((client.getHandler() != this) && client.isOnline()) {
                    client.getHandler().queue.add(message);
                }
            }
            return;
        }
        if(message.getType() == MessageType.LOGIN) {
            String name = message.getMessage();
            if(client != null && name.equals(client.getName())) {
                return;
            }
            for(ClientInfo client : clients.values()) {
                if((client != this.client) && (client.getName().equals(name))) {
                    queue.add(new Message("Name is already taken",MessageType.ERROR));
                    return;
                }
            }
            if(client != null) {
                if(clients.containsKey(client.getSessionID())) {
                    clients.remove(client.getSessionID());
                } else {
                    queue.add(new Message("Wrong sessionID",MessageType.ERROR));
                    return;
                }
                message = new Message(client.getName() + ">" + name,MessageType.RELOGIN);
            }
            client = new ClientInfo(name,this);
            int id = sessionID.getAndIncrement();
            client.setSessionID(id);
            client.setOnline(true);
            clients.put(id,client);
            queue.add(new Message(String.valueOf(id),MessageType.SUCCESS));
            for(ClientInfo client : clients.values()) {
                if((client.getHandler() != this) && client.isOnline()) {
                    client.getHandler().queue.add(new Message(message.getMessage(), MessageType.USERLOGIN));
                }
            }
            return;
        }
        if(message.getType() == MessageType.LOGOUT) {
            if(client != null) {
                message.setMessage(client.getName());
                if(clients.containsKey(message.getSessionID())) {
                    clients.remove(message.getSessionID());
                } else {
                    queue.add(new Message("Wrong sessionID",MessageType.ERROR));
                    return;
                }
            } else {
                queue.add(new Message("You haven't logged in yet",MessageType.ERROR));
                return;
            }
            queue.add(new Message("",MessageType.SUCCESS));
            for(ClientInfo client : clients.values()) {
                if((client.getHandler() != this) && client.isOnline()) {
                    client.getHandler().queue.add(new Message(message.getMessage(),MessageType.USERLOGOUT));
                }
            }
            endIt();
            return;
        }
        if(message.getType() == MessageType.LIST) {
            StringBuilder builder = new StringBuilder();
            for(ClientInfo client : clients.values()) {
                if(client.isOnline()) {
                    builder.append(client.getName());
                    builder.append("$");
                }
            }
            message.setMessage(builder.toString().trim());
            queue.add(message);
        }
    }
}
