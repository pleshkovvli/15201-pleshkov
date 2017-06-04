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
    public static Logger getLogger() {
        return logger;
    }

    static private Logger logger;

    private ClientInfo client;

    static {
        PropertyConfigurator.configure("log4j.properties");
        if(!Files.exists(Paths.get(LOG_FILE_NAME))) {
            try {
                Files.createFile(Paths.get(LOG_FILE_NAME));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        logger = Logger.getLogger("ServerLogger");
    }

    ServerMessagesHandler(Socket socket) {
        super(socket);
    }

    @Override
    protected void endReading() {
        if(client != null) {
            client.setOnline(false);
        }
        logger.info(Thread.currentThread().toString() + " finished reading");
    }

    @Override
    protected void endWriting() {
        if(client != null) {
            client.setOnline(false);
        }
        logger.info(Thread.currentThread().toString() + " finished writing");
    }

    @Override
    protected void fin() {
        logger.info("Started threads " + getWriter().toString() + ", " + getReader().toString());
    }

    @Override
    protected ServerMessage getMessage() throws IOException, InterruptedException {
        return queue.take();

    }

    @Override
    public void process(ClientChatMessage message) {
        if(client == null) {
            client = clients.get(message.getSessionID());
            if(client == null) {
                ServerErrorMessage errorMessage = new ServerErrorMessage("You haven't logged in yet");
                queue.add(errorMessage);
                return;
            }
            client.setOnline(true);
        }
        ServerChatMessage response = new ServerChatMessage(message.getMessage(),client.getName());
        for(ClientInfo client : clients.values()) {
            if((client.getHandler() != this) && client.isOnline()) {
                client.getHandler().queue.add(response);
            }
        }
    }

    @Override
    public void process(ClientLoginMessage message) {
        if(client != null) {
            queue.add(new ServerErrorMessage("Already logged in"));
            return;
        }
        String name = message.getUserName();
        for(ClientInfo client : clients.values()) {
            if((client != this.client) && (client.getName().equals(name))) {
                queue.add(new ServerErrorMessage("Name already had been taken"));
                return;
            }
        }
        client = new ClientInfo(name,message.getClientName(),this);
        int id = sessionID.getAndIncrement();
        client.setSessionID(id);
        client.setOnline(true);
        clients.put(id,client);
        queue.add(new ServerSuccessLoginMessage(id));
        for(ClientInfo client : clients.values()) {
            if((client.getHandler() != this) && client.isOnline()) {
                client.getHandler().queue.add(new ServerUserloginMessage(message.getUserName(),message.getClientName()));
            }
        }
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
                return;
            }
        } else {
            queue.add(new ServerErrorMessage("You haven't logged in yet"));
            return;
        }
        queue.add(new ServerSuccessMessage());
        for(ClientInfo client : clients.values()) {
            if((client.getHandler() != this) && client.isOnline()) {
                client.getHandler().queue.add(new ServerUserlogoutMessage(name));
            }
        }
        endIt();
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
    }

    protected void handleMessage(ClientMessage message) throws IOException {
        message.process(this);
    }
}
