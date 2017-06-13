package ru.nsu.ccfit.pleshkov.lab3.client;

import ru.nsu.ccfit.pleshkov.lab3.base.MessageObserver;
import ru.nsu.ccfit.pleshkov.lab3.base.Observer;
import ru.nsu.ccfit.pleshkov.lab3.base.ServerMessagesProcessor;
import ru.nsu.ccfit.pleshkov.lab3.messages.*;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicInteger;

class Client implements ServerMessagesProcessor, Observer<Config> {

    final static private String XML_CLIENT_NAME = "pleshkov.xml-client";
    final static private String OBJECTS_CLIENT_NAME = "pleshkov.objects-client";

    volatile private boolean loggingOut = false;
    volatile private boolean loggingIn = false;
    volatile private boolean gettingList = false;
    volatile private boolean messaging = false;
    volatile private boolean connected = false;
    volatile private boolean failedLogin = false;

    private int sessionID;

    private AtomicInteger unhandledMessages = new AtomicInteger(0);

    void setGui(ClientInterface gui) {
        this.gui = gui;
    }

    private ClientInterface gui;

    ClientMessagesHandler getHandler() {
        return handler;
    }

    void setHandler(ClientMessagesHandler handler) {
        loggingIn = false;
        loggingOut = false;
        gettingList = false;
        messaging = false;
        this.handler = handler;
    }

    private ClientMessagesHandler handler;

    void forceLogin(Config config) {
        gui.forceLogin(config);
    }

    @Override
    public void update(Config config) {
        if(failedLogin) {
            addLoginMessage(config.getName());
            failedLogin = false;
            return;
        }
        try {
            Socket socket = new Socket();
            socket.connect(new InetSocketAddress(config.getAddress(),config.getPort()),1000);
            if(config.getType().equals("xml")) {
                setHandler(new ClientXMLMessagesHandler(socket, XML_CLIENT_NAME,this));
            } else {
                setHandler(new ClientObjectMessagesHandler(socket,OBJECTS_CLIENT_NAME,this));
            }
            getHandler().addMessageObserver(new MessageObserver<ServerMessage>() {
                @Override
                public void update(ServerMessage message) {
                    message.process(Client.this);
                }
            });
            begin(config);
        } catch (IOException e) {
            gui.connectionError();
        }
    }

    void addChatMessage(String message)  {
        unhandledMessages.getAndIncrement();
        if(messaging) {
            handler.addChatMessage(message);
        }
    }

    private void addLoginMessage(String name) {
        if(!messaging && !loggingIn) {
            loggingIn = true;
            handler.addLoginMessage(name);
        }
    }

    void addLogoutMessage()  {
        if(messaging) {
            loggingOut = true;
            handler.addLogoutMessage();
        } else {
            while (unhandledMessages.get() > 0) {
                unhandledMessages.getAndDecrement();
                gui.declineMessage();
            }
            gui.showLogout();
            handler.endIt();
            connected = false;
        }
    }

    void addListMessage() {
        gettingList = true;
        handler.addListMessage();
    }

    void endIt() {
        if(handler != null) {
            handler.endIt();
        }
    }

    void showFailedRead() {
        if(connected) {
            gui.showError("Garbage was read");
        } else {
            gui.showLoginError("Garbage was read");
        }
    }


    private void begin(Config config) {
        handler.begin("Writer", "Reader");
        if(connected) {
            handler.setSessionID(sessionID);
            gui.connectionEstablished();
            addListMessage();
        } else {
            addLoginMessage(config.getName());
        }
    }
    
    void handleUnknownMessage() {
        gui.showError("Unknown type");
    }

    void showInterruption(String message) {
        gui.showError(message);
    }

    void showConnectionBreak() {
        if(connected) {
            messaging = false;
            gui.connectionBroken();
        } else {
            gui.connectionError();
        }
    }

    @Override
    public void process(ServerChatMessage message) {
        if(messaging) {
            gui.showMessage(message.getMessage(),message.getName());
        }
    }

    @Override
    public void process(ServerSuccessMessage message) {
        if(loggingOut) {
            loggingOut = false;
            messaging = false;
            gui.showLogout();
            handler.endIt();
            connected = false;
        } else if (unhandledMessages.get() > 0) {
            unhandledMessages.getAndDecrement();
            gui.approveMessage();
        }
    }

    @Override
    public void process(ServerSuccessListMessage message) {
        messaging = true;
        gui.showList(message.getListusers());
    }

    @Override
    public void process(ServerSuccessLoginMessage message) {
        connected = true;
        if(loggingIn) {
            sessionID = message.getSessionID();
            handler.setSessionID(sessionID);
            loggingIn = false;
            messaging = true;
            gui.startMessaging();
        }
    }

    @Override
    public void process(ServerUserloginMessage message) {
        gui.showUserlogin(message.getName(),message.getType());
    }

    @Override
    public void process(ServerUserlogoutMessage message) {
        gui.showUserlogout(message.getName());
    }

    @Override
    public void process(ServerErrorMessage errorMessage) {
        if (loggingIn) {
            loggingIn = false;
            failedLogin = true;
            gui.showLoginError(errorMessage.getReason());
            return;
        }
        if (loggingOut) {
            loggingOut = false;
            gui.showError(errorMessage.getReason());
            return;
        }
        if (gettingList) {
            gettingList = false;
            gui.showError(errorMessage.getReason());
            return;
        }
        if (unhandledMessages.get() > 0) {
            unhandledMessages.getAndDecrement();
            gui.declineMessage();
            gui.showError(errorMessage.getReason());
        }
    }

}
