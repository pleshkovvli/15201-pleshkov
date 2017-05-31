package ru.nsu.ccfit.pleshkov.lab3;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class Server {
    final static int PORT = 2000;
    final static private int TIMEOUT = 1500;
    static String serverName = "server";

    public static void main(String[] args) {
	    try(ServerSocket serverSocket = new ServerSocket(PORT)) {
            while(!Thread.interrupted()) {
                Socket socket = serverSocket.accept();
                ServerObjectMessagesHandler handler = new ServerObjectMessagesHandler(socket);
                handler.begin("w","r");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
