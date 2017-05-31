package ru.nsu.ccfit.pleshkov.lab3;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    final static int PORT = 2000;
    //final static private int TIMEOUT = 1500;

    public static void main(String[] args) {
	    try(ServerSocket serverSocket = new ServerSocket(PORT)) {
            int i = 0;
            while(!Thread.interrupted()) {
                Socket socket = serverSocket.accept();
                ServerObjectMessagesHandler handler = new ServerObjectMessagesHandler(socket);
                handler.begin("ServerWriter#" + String.valueOf(i),"ServerReader#" + String.valueOf(i));
                ++i;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
