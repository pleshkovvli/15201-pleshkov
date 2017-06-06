package ru.nsu.ccfit.pleshkov.lab3;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Server {
    final private static int XML_PORT = 2000;
    final private static int OBJECTS_PORT = 3000;
    final static String LOGGER_NAME = "Server";
    final static private int TIMEOUT = 1500;

    final private static String LOG_FILE_NAME = "server.log";
    private static Logger logger;

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

    public static void main(String[] args) {
	    new Thread(() -> {
            try(ServerSocket serverSocket = new ServerSocket(XML_PORT)) {
                int i = 0;
                while(!Thread.interrupted()) {
                    Socket socket = serverSocket.accept();
                    ServerMessagesHandler handler = new ServerXMLMessagesHandler(socket);
                    handler.begin("ServerXMLWriter#" + String.valueOf(i),
                            "ServerXMLReader#" + String.valueOf(i),TIMEOUT);
                    ++i;
                }
            } catch (IOException e) {
                logger.warn(e.getMessage());
            }
        }).start();
        new Thread(() -> {
            try(ServerSocket serverSocket = new ServerSocket(OBJECTS_PORT)) {
                int i = 0;
                while(!Thread.interrupted()) {
                    Socket socket = serverSocket.accept();
                    ServerMessagesHandler handler = new ServerObjectMessagesHandler(socket);
                    handler.begin("ServerObjectsWriter#" + String.valueOf(i),
                            "ServerObjectsReader#" + String.valueOf(i),TIMEOUT);
                    ++i;
                }
            } catch (IOException e) {
                logger.warn(e.getMessage());
            }
        }).start();
    }
}
