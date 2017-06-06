package ru.nsu.ccfit.pleshkov.lab3;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import java.io.IOException;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

abstract class ServerMessagesHandler extends MessagesHandler<ClientMessage, ServerMessage>
implements ClientMessagesProcessor {
    private BlockingQueue<ServerMessage> queue = new ArrayBlockingQueue<>(100);

    final private static AtomicInteger sessionID = new AtomicInteger(1);

    static private Map<Integer,ClientInfo> clients = new TreeMap<>();

    final private static String LOG_FILE_NAME = "server.log";

    static Logger getLogger() {
        return logger;
    }

    static private Logger logger;

    private ClientInfo client;

    final private Object queuesLock = new Object();

    static {
        PropertyConfigurator.configure("log4j.properties");
        if(!Files.exists(Paths.get(LOG_FILE_NAME))) {
            try {
                Files.createFile(Paths.get(LOG_FILE_NAME));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        logger = Logger.getLogger(Server.LOGGER_NAME);
    }

    ServerMessagesHandler(Socket socket) {
        super(socket);
    }

    @Override
    protected void handleInterruption() {
        logger.info("Thread was interrupted");
    }

    @Override
    protected void handleConnectionBreak() {
        if(client.isOnline()) {
            logger.warn("Connection was broken");
            client.setOnline(false);
            synchronized (queuesLock) {
                queue.add(new ServerSuccessMessage());
                for(ClientInfo client : clients.values()) {
                    if((client.getHandler() != this) && client.isOnline()) {
                        client.getHandler().queue.add(new ServerUserlogoutMessage(this.client.getName()));
                    }
                }
            }
        }
    }

    public BlockingQueue<ServerMessage> getQueue() {
        return queue;
    }

    @Override
    protected void endReading() {
        if(client != null) {
            client.setOnline(false);
        }
        logger.info("Finished reading");
    }

    @Override
    protected void endWriting() {
        if(client != null) {
            client.setOnline(false);
        }
        logger.info("Finished writing");
    }

    @Override
    protected void fin() {
        logger.info("Finished initialization");
    }

    @Override
    protected ServerMessage getMessage() throws IOException, InterruptedException {
        ServerMessage message = queue.take();
        logger.info("Got message " + message.getClass().getSimpleName());
        return message;
    }

    @Override
    public void process(ClientChatMessage message) {
        if(client == null) {
            client = clients.get(message.getSessionID());
            if(client == null) {
                ServerErrorMessage errorMessage = new ServerErrorMessage("You haven't logged in yet");
                logger.warn("Message without login error");
                queue.add(errorMessage);
                return;
            }
            synchronized (queuesLock) {
                ServerUserloginMessage response = new ServerUserloginMessage(client.getName(),client.getType());
                for(ClientInfo client : clients.values()) {
                    if((client.getHandler() != this) && client.isOnline()) {
                        client.getHandler().queue.add(response);
                    }
                }
            }
            client.setOnline(true);
        }
        synchronized (queuesLock) {
            queue.add(new ServerSuccessMessage());
            ServerChatMessage response = new ServerChatMessage(message.getMessage(),client.getName());
            for(ClientInfo client : clients.values()) {
                if((client.getHandler() != this) && client.isOnline()) {
                    client.getHandler().queue.add(response);
                }
            }
        }
        logger.info("Handled with chat message from " + client.getName());
    }

    @Override
    public void process(ClientLoginMessage message) {
        if(client != null) {
            queue.add(new ServerErrorMessage("Already logged in"));
            logger.warn("Double login error");
            return;
        }
        String name = message.getUserName();
        for(ClientInfo client : clients.values()) {
            if((client != this.client) && (client.getName().equals(name))) {
                queue.add(new ServerErrorMessage("Name already had been taken"));
                logger.warn("Same login name error " + client.getName());
                return;
            }
        }
        client = new ClientInfo(name,message.getClientName(),this);
        int id = sessionID.getAndIncrement();
        client.setOnline(true);
        clients.put(id,client);
        synchronized (queuesLock) {
            queue.add(new ServerSuccessLoginMessage(id));
            for(ClientInfo client : clients.values()) {
                if((client.getHandler() != this) && client.isOnline()) {
                    client.getHandler().queue.add(new ServerUserloginMessage(message.getUserName(),message.getClientName()));
                }
            }
        }
        logger.info("Handled with login message " + message.getUserName() + " via " + message.getClientName());
    }

    @Override
    public void process(ClientLogoutMessage message) {
        String name;
        if(client != null) {
            name = client.getName();
            if(clients.containsKey(message.getSessionID())) {
                clients.remove(message.getSessionID());
            } else {
                queue.add(new ServerErrorMessage("Wrong sessionID"));
                logger.warn("Logout request with wrong id=" + String.valueOf(message.getSessionID()));
                return;
            }
        } else {
            queue.add(new ServerErrorMessage("You haven't logged in yet"));
            logger.warn("Logout request without login");
            return;
        }
        synchronized (queuesLock) {
            queue.add(new ServerSuccessMessage());
            for(ClientInfo client : clients.values()) {
                if((client.getHandler() != this) && client.isOnline()) {
                    client.getHandler().queue.add(new ServerUserlogoutMessage(name));
                }
            }
        }
        logger.info("Handled with logout message from " + name);
    }

    @Override
    public void process(ClientListMessage message) {
        ArrayList<User> listusers = new ArrayList<>();
        for(ClientInfo client : clients.values()) {
            if(client.isOnline()) {
                listusers.add(new User(client.getName(),client.getType()));
            }
        }
        queue.add(new ServerSuccessListMessage(listusers));
        logger.info("Got list message from " + client.getName());
    }

    protected void handleMessage(ClientMessage message) throws IOException {
        message.process(this);
    }
}
