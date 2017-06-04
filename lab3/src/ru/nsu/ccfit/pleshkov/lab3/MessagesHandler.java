package ru.nsu.ccfit.pleshkov.lab3;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;

abstract class MessagesHandler<IN extends Message, OUT extends Message> {
    Socket getSocket() {
        return socket;
    }

    private Socket socket;

    void endIt() {
        reader.interrupt();
        writer.interrupt();
    }

    private Thread writer;

    Thread getWriter() {
        return writer;
    }

    Thread getReader() {
        return reader;
    }

    private Thread reader;

    MessagesHandler(Socket socket) {
        this.socket = socket;
    }

    abstract protected IN readMessage() throws IOException, InterruptedException, FailedReadException;
    abstract protected OUT getMessage() throws IOException, InterruptedException;
    abstract protected void writeMessage(OUT message) throws IOException, InterruptedException;
    abstract protected void handleMessage(IN message) throws IOException, InterruptedException;
    abstract protected void endReading();
    abstract protected void endWriting();
    abstract protected void fin();

    void begin(String readerName, String writerName) {
        writer = new Thread(() -> {
            while (!Thread.interrupted()) {
                try {
                    writeMessage(getMessage());
                } catch (SocketTimeoutException e) {
                    if (socket.isClosed()) {
                        e.printStackTrace();
                        break;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    break;
                } catch (InterruptedException e) {
                    break;
                }
            }
            endIt();
            endWriting();
        }, writerName);
        writer.start();
        reader = new Thread(() -> {
            while (!Thread.interrupted()) {
                try {
                    handleMessage(readMessage());
                } catch (SocketTimeoutException e) {
                    if (socket.isClosed()) {
                        e.printStackTrace();
                        break;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    break;
                } catch (FailedReadException e) {
                    e.printStackTrace();
                    break;
                } catch (InterruptedException e) {
                    break;
                }
            }
            endIt();
            endReading();
        }, readerName);
        reader.start();
        try {
            socket.setSoTimeout(1000);
        } catch (SocketException e) {
            e.printStackTrace();
        }
        fin();
    }
}
