package ru.nsu.ccfit.pleshkov.lab3;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;

abstract public class MessagesHandler {
    private Socket socket;

    public MessagesHandler(Socket socket) throws IOException {
        this.socket = socket;
    }

    abstract protected Message readMessage() throws IOException, FailedReadException;
    abstract protected Message getMessage() throws IOException, FailedWriteException;
    abstract protected void writeMessage(Message message) throws IOException, FailedWriteException;
    abstract protected void handleMessage(Message message) throws IOException, FailedReadException;
    abstract protected void initWriting() throws IOException, FailedWriteException;
    abstract protected void endReading();
    abstract protected void initReading() throws IOException, FailedReadException;
    abstract protected void endWriting();

    public void begin(String readerName, String writerName) {
        new Thread(() -> {
            while (!Thread.interrupted()) {
                try {
                    initWriting();
                    break;
                } catch (SocketTimeoutException e) {
                    if (socket.isClosed()) {
                        break;
                    }
                } catch (FailedWriteException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    break;
                }
            }
            while (!Thread.interrupted()) {
                try {
                    writeMessage(getMessage());
                } catch (SocketTimeoutException e) {
                    if (socket.isClosed()) {
                        break;
                    }
                } catch (FailedWriteException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    break;
                }
            }
            endWriting();
        }, readerName).start();
        new Thread(() -> {
            while (!Thread.interrupted()) {
                try {
                    initReading();
                    break;
                } catch (SocketTimeoutException e) {
                    if (socket.isClosed()) {
                        break;
                    }
                } catch (FailedReadException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    break;
                }
            }
            while (!Thread.interrupted()) {
                try {
                    handleMessage(readMessage());
                } catch (SocketTimeoutException e) {
                    if (socket.isClosed()) {
                        break;
                    }
                } catch (FailedReadException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    break;
                }
            }
            endReading();
        }, writerName).start();
        try {
            socket.setSoTimeout(1000);
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }
}
