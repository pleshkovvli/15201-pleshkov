package ru.nsu.ccfit.pleshkov.lab3;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    final private static int XML_PORT = 2000;
    final private static int OBJECTS_PORT = 3000;
    final static String LOGGER_NAME = "Server";
    //final static private int TIMEOUT = 1500;

    public static void main(String[] args) {
	    new Thread(() -> {
            try(ServerSocket serverSocket = new ServerSocket(XML_PORT)) {
                int i = 0;
                while(!Thread.interrupted()) {
                    Socket socket = serverSocket.accept();
                    ServerMessagesHandler handler = new ServerXMLMessagesHandler(socket);
                    handler.begin("ServerXMLWriter#" + String.valueOf(i),"ServerXMLReader#" + String.valueOf(i));
                    ++i;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
        new Thread(() -> {
            try(ServerSocket serverSocket = new ServerSocket(OBJECTS_PORT)) {
                int i = 0;
                while(!Thread.interrupted()) {
                    Socket socket = serverSocket.accept();
                    ServerMessagesHandler handler = new ServerObjectMessagesHandler(socket);
                    handler.begin("ServerObjectsWriter#" + String.valueOf(i),"ServerObjectsReader#" + String.valueOf(i));
                    ++i;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }
}
