package ru.nsu.ccfit.pleshkov.lab3.server;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Server {
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
        final Ports ports;
        if(args.length > 0) {
            try {
                ports = ServerConfigParser.parse(args[0]);
            } catch (BadParsingException e) {
                logger.warn("Failed to parse config; exiting");
                return;
            }
        } else {
            ports = new Ports(2000,3000);
        }
	    Thread xml = new Thread(() -> {
            try(ServerSocket serverSocket = new ServerSocket(ports.getXml())) {
                int i = 0;
                serverSocket.setSoTimeout(TIMEOUT);
                while(!Thread.interrupted()) {
                    Socket socket;
                    try {
                        socket = serverSocket.accept();
                        ServerMessagesHandler handler = new ServerXMLMessagesHandler(socket);
                        handler.begin("ServerXMLWriter#" + String.valueOf(i),
                                "ServerXMLReader#" + String.valueOf(i));
                        ++i;
                    } catch (SocketTimeoutException e) {
                        if(serverSocket.isClosed()) {
                            break;
                        }
                    } catch (IOException e) {
                        logger.warn(e.getMessage());
                    }
                }
            } catch (IOException e) {
                logger.warn(e.getMessage());
            }
        });
        xml.start();
        logger.info("Started XML on " + ports.getXml());
        Thread objects = new Thread(() -> {
            try(ServerSocket serverSocket = new ServerSocket(ports.getObjects())) {
                serverSocket.setSoTimeout(TIMEOUT);
                int i = 0;
                while(!Thread.interrupted()) {
                    Socket socket;
                    try {
                        socket = serverSocket.accept();
                        ServerMessagesHandler handler = new ServerObjectMessagesHandler(socket);
                        handler.begin("ServerObjectsWriter#" + String.valueOf(i),
                                "ServerObjectsReader#" + String.valueOf(i));
                        ++i;
                    } catch (SocketTimeoutException e) {
                        if(serverSocket.isClosed()) {
                            break;
                        }
                    } catch (IOException e) {
                        logger.warn(e.getMessage());
                    }
                }
            } catch (IOException e) {
                logger.warn(e.getMessage());
            }
        });
        objects.start();
        logger.info("Started objects on " + ports.getObjects());
        new Thread(() -> {
            try(BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
                while(true) {
                    String line = reader.readLine();
                    if(line.trim().equals("exit")) {
                        System.out.println("ok");
                        xml.interrupt();
                        objects.interrupt();
                        ServerMessagesHandler.finish();
                        break;
                    } else {
                        System.out.println("Enter \"exit\" to finish");
                    }
                }
            } catch (IOException e) {
                logger.warn(e.getMessage());
            }
        }).start();
        logger.info("Started finishing thread; enter \"exit\" to finish");
    }
}
