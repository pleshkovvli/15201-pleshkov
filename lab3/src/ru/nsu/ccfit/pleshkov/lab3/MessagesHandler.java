package ru.nsu.ccfit.pleshkov.lab3;

import ru.nsu.ccfit.pleshkov.lab3.messages.Message;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;

public abstract class MessagesHandler<IN extends Message, OUT extends Message> {
    protected Socket getSocket() {
        return socket;
    }

    private Socket socket;

    public void endIt() {
        reader.interrupt();
        writer.interrupt();
    }

    private Thread writer;
    private Thread reader;

    public MessagesHandler(Socket socket) throws SocketException {
        this.socket = socket;
        socket.setSoTimeout(1000);
    }

    abstract protected IN readMessage() throws IOException, InterruptedException, FailedReadException, UnknownMessageException;
    abstract protected OUT getMessage() throws IOException, InterruptedException;
    abstract protected void writeMessage(OUT message) throws IOException, InterruptedException;
    abstract protected void handleMessage(IN message) throws IOException, InterruptedException;
    abstract protected void handleUnknownMessage();
    abstract protected void handleConnectionBreak();
    abstract protected void handleInterruption();
    abstract protected void handleFailedRead();
    abstract protected void endReading();
    abstract protected void endWriting();
    abstract protected void fin();
    abstract protected void close();

    public void begin(String readerName, String writerName, int timeout) {
        writer = new Thread(() -> {
            try {
                while (!Thread.interrupted()) {
                    try {
                        writeMessage(getMessage());
                    } catch (SocketTimeoutException e) {
                        if (socket.isClosed()) {
                            break;
                        }
                    }
                }
            } catch (IOException e) {
                handleConnectionBreak();
            } catch (InterruptedException e) {
                handleInterruption();
            }
            endIt();
            endWriting();
        }, writerName);
        writer.start();
        reader = new Thread(() -> {
            try {
                while (!Thread.interrupted()) {
                    try {
                        handleMessage(readMessage());
                    } catch (UnknownMessageException e) {
                        handleUnknownMessage();
                    } catch (SocketTimeoutException e) {
                        if (socket.isClosed()) {
                            break;
                        }
                    }
                }
            } catch (FailedReadException e) {
                handleFailedRead();
            } catch (IOException e) {
                handleConnectionBreak();
            }  catch (InterruptedException e) {
                handleInterruption();
            }
            endIt();
            endReading();
        }, readerName);
        reader.start();
        try {
            socket.setSoTimeout(timeout);
        } catch (SocketException e) {
            e.printStackTrace();
        }
        fin();
    }
}
