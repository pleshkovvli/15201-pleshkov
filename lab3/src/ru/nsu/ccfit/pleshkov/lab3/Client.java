package ru.nsu.ccfit.pleshkov.lab3;

import javax.swing.*;
import java.io.*;
import java.net.InetAddress;
import java.net.Socket;

public class Client {

    static String getLogin() {
        return login;
    }

    static void setLogin(String login) {
        Client.login = login;
    }

    private static String login = "login";

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
                ClientObjectMessagesHandler handler = new ClientObjectMessagesHandler(socket);
                handler.setInit(init);
                if(!init) {
                    handler.setSessionID(sessionID);
                }
                gui.init((String message, MessageType type) -> SwingUtilities.invokeLater(() ->
                        handler.getQueue().add(new Message(message,type,handler.getSessionID()))));
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
