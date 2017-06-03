package ru.nsu.ccfit.pleshkov.lab3;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;

public class Client {

    final static private String CLIENT_NAME = "pleshkov.client";

    static String getLogin() {
        return login;
    }

    static void setLogin(String login) {
        Client.login = login;
    }

    private static String login;

    static ClientGUI getGui() {
        return gui;
    }

    private static ClientGUI gui;

    static private int sessionID;

    static private boolean init = true;

    public static void main(String[] args) throws InterruptedException {
        try {
            gui = new ClientGUI();
            while(true) {
                Socket socket = new Socket(InetAddress.getLocalHost(),Server.PORT);
                ClientMessagesHandler handler = new ClientXMLMessagesHandler(socket);
                gui.init((String name)  -> handler.getQueue().add(new ClientLoginMessage(name,CLIENT_NAME)),
                        (String message) -> handler.getQueue().add(new ClientChatMessage(message,sessionID)),
                        () -> handler.getQueue().add(new ClientLogoutMessage(sessionID)),
                        () -> handler.getQueue().add(new ClientListMessage(sessionID)));
                handler.begin("Writer", "Reader");
                if(init) {
                    sessionID = handler.getSessionID();
                }
                init = false;
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(0);
        }
    }
}
