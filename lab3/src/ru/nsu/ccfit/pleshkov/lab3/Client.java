package ru.nsu.ccfit.pleshkov.lab3;

import javax.swing.*;
import java.io.*;
import java.net.InetAddress;
import java.net.Socket;

public class Client {
    private static Object lock = new Object();

    public static String getLogin() {
        return login;
    }

    public static void setLogin(String login) {
        Client.login = login;
    }

    private static String login = "login";

    public static ClientGUI getGui() {
        return gui;
    }

    static ClientGUI gui;

    public static void main(String[] args) throws InterruptedException {
        try(Socket socket = new Socket(InetAddress.getLocalHost(),Server.PORT)) {
            while(!Thread.interrupted()) {
                ClientObjectMessagesHandler handler = new ClientObjectMessagesHandler(socket);
                gui = new ClientGUI((String message) -> SwingUtilities.invokeLater(() ->
                        handler.getQueue().add(new Message(message,MessageType.MESSAGE,login))));
                handler.begin("Writer","Reader");
                Thread.sleep(100000000);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
