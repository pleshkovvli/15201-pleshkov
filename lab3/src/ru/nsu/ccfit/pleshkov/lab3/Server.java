package ru.nsu.ccfit.pleshkov.lab3;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.BlockingQueue;

public class Server {
    final static public int PORT = 2000;
    final static private int TIMEOUT = 1500;
    static private int numberOfThread = 1;
    static String serverName = "server";

    static private ArrayList<ClientInfo> clients = new ArrayList<>();

    public static void main(String[] args) {
	    try(ServerSocket serverSocket = new ServerSocket(PORT)) {
            while(!Thread.interrupted()) {
                Socket socket = serverSocket.accept();
                ClientInfo client = new ClientInfo(null,null);;
                ServerObjectMessagesHandler handler = new ServerObjectMessagesHandler(socket,clients,client);
                client.setHandler(handler);
                clients.add(client);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
