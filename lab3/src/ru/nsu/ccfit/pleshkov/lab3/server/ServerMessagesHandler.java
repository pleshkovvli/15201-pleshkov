package ru.nsu.ccfit.pleshkov.lab3.server;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import ru.nsu.ccfit.pleshkov.lab3.base.ClientMessagesProcessor;
import ru.nsu.ccfit.pleshkov.lab3.base.MessagesHandler;
import ru.nsu.ccfit.pleshkov.lab3.base.User;
import ru.nsu.ccfit.pleshkov.lab3.messages.*;

import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

abstract class ServerMessagesHandler extends MessagesHandler<ClientMessage, ServerMessage>
implements ClientMessagesProcessor {
    static private BlockingQueue<ServerMessage> commonQueue = new ArrayBlockingQueue<>(500);

    static private boolean filled = false;
    volatile private boolean disconnected = false;

    static private ScheduledExecutorService timer = new ScheduledThreadPoolExecutor(1);

    private BlockingQueue<ServerMessage> queue = new ArrayBlockingQueue<>(100);

    final private static AtomicInteger sessionID = new AtomicInteger(1);

    static private Map<Integer,ClientInfo> clients = new TreeMap<>();

    final private static String LOG_FILE_NAME = "server.log";

    static Logger getLogger() {
        return logger;
    }

    static private Logger logger;

    private ClientInfo client;

    static final private Object queuesLock = new Object();

    static void finish() {
        timer.shutdown();
    }

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

    ServerMessagesHandler(Socket socket)  throws SocketException {
        super(socket);
    }

    @Override
    protected void handleInterruption() {
        logger.info("Thread was interrupted");
    }

    @Override
    protected void handleFailedRead() {
        logger.info("Garbage was read; closing connection");
    }

    @Override
    protected void handleConnectionBreak() {
        if(client != null && client.isOnline()) {
            disconnected = true;
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
        logger.info("Closing streams and socket");
    }

    @Override
    protected void endReading() {
        if(client != null) {
            client.setOnline(false);
        }
        logger.info("Finished reading");
        if(disconnected) {
            disconnected = false;
            timer.schedule(()-> {
                synchronized (queuesLock) {
                    if(!client.isOnline()) {
                        clients.remove(client.getSessionID());
                    }
                }
            },20, TimeUnit.SECONDS);
        }
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

    private void checkID(int id) throws WrongSessionIDException {
        if(client == null) {
            client = clients.get(id);
            if(client == null) {
                ServerErrorMessage errorMessage = new ServerErrorMessage("You haven't logged in yet");
                logger.warn("Message without login error");
                queue.add(errorMessage);
                throw new WrongSessionIDException();
            }
            ServerUserloginMessage response = new ServerUserloginMessage(client.getName(),client.getType());
            for(ClientInfo client : clients.values()) {
                if((client.getHandler() != this) && client.isOnline()) {
                    client.getHandler().queue.add(response);
                }
            }
            client.setOnline(true);
        } else if(client.getSessionID() != id) {
            ServerErrorMessage errorMessage = new ServerErrorMessage("Wrong session ID");
            logger.warn("Wrong session ID error");
            queue.add(errorMessage);
            throw new WrongSessionIDException();
        }
    }

    @Override
    public void process(ClientChatMessage message) {
        synchronized (queuesLock) {
            try {
                checkID(message.getSessionID());
                queue.put(new ServerSuccessMessage());
            } catch (WrongSessionIDException e) {
                return;
            } catch (InterruptedException e) {
                logger.warn("Thread was interrupted");
            }
            ServerChatMessage response = new ServerChatMessage(message.getMessage(),client.getName());
            try {
                if(filled) {
                    commonQueue.take();
                } else if(commonQueue.size() > 20) {
                    filled = true;
                }
                commonQueue.put(response);
            } catch (InterruptedException e) {
                logger.warn("Thread was interrupted");
            }
            for(ClientInfo client : clients.values()) {
                if((client.getHandler() != this) && client.isOnline()) {
                    try {
                        client.getHandler().queue.put(response);
                    } catch (InterruptedException e) {
                        logger.warn("Thread was interrupted");
                    }

                }
            }
        }
        logger.info("Handled with chat message from " + client.getName());
    }

    @Override
    public void process(ClientLoginMessage message) {
        synchronized (queuesLock) {
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
            int id = sessionID.getAndIncrement();
            client = new ClientInfo(name,message.getClientName(),this,id);
            client.setOnline(true);
            clients.put(id,client);
            queue.add(new ServerSuccessLoginMessage(id));
            for(ClientInfo client : clients.values()) {
                if((client.getHandler() != this) && client.isOnline()) {
                    try {
                        client.getHandler().queue.put(new ServerUserloginMessage(message.getUserName(),
                            message.getClientName()));
                    } catch (InterruptedException e) {
                        logger.warn("Thread was interrupted");
                    }
                }
            }
        }
        logger.info("Handled with login message " + message.getUserName() + " via " + message.getClientName());
    }


    @Override
    protected void handleUnknownMessage() {
        queue.add(new ServerErrorMessage("Unknown type"));
    }

    @Override
    public void process(ClientLogoutMessage message) {
        String name;
        synchronized (queuesLock) {
            if(client != null) {
                name = client.getName();
                if(clients.containsKey(message.getSessionID())) {
                    clients.remove(message.getSessionID());
                } else {
                    try {
                        queue.put(new ServerErrorMessage("Wrong sessionID"));
                    } catch (InterruptedException e) {
                        logger.warn("Thread was interrupted");
                    }
                logger.warn("Logout request with wrong id=" + String.valueOf(message.getSessionID()));
                return;
                }
            } else {
                try {
                    queue.put(new ServerErrorMessage("You haven't logged in yet"));
                } catch (InterruptedException e) {
                    logger.warn("Thread was interrupted");
                }
                logger.warn("Logout request without login");
                return;
            }

            try {
                queue.put(new ServerSuccessMessage());
            } catch (InterruptedException e) {
                logger.warn("Thread was interrupted");
            }
            client.setOnline(false);
            for(ClientInfo client : clients.values()) {
                if((client.getHandler() != this) && client.isOnline()) {
                    try {
                        client.getHandler().queue.put(new ServerUserlogoutMessage(name));
                    } catch (InterruptedException e) {
                        logger.warn("Thread was interrupted");
                    }
                }
            }
        }
        logger.info("Handled with logout message from " + name);
    }

    @Override
    public void process(ClientListMessage message) {
        synchronized (queuesLock) {
            try {
                checkID(message.getSessionID());
            } catch (WrongSessionIDException e) {
                return;
            }
            ArrayList<User> listusers = new ArrayList<>();
            for(ClientInfo client : clients.values()) {
                if(client.isOnline()) {
                    listusers.add(new User(client.getName(),client.getType()));
                }
            }
            queue.add(new ServerSuccessListMessage(listusers));
            for (ServerMessage oldMessage: commonQueue) {
                queue.add(oldMessage);
            }
        }
        logger.info("Got list message from " + client.getName());
    }

    protected void handleMessage(ClientMessage message) throws IOException {
        message.process(this);
    }
}
