package ru.nsu.ccfit.pleshkov.lab3;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

class ClientObjectMessagesHandler extends ClientMessagesHandler {

    ClientObjectMessagesHandler(Socket socket) throws IOException {
        super(socket);
        messagesReader = new ObjectInputStream(socket.getInputStream());
        messagesWriter = new ObjectOutputStream(socket.getOutputStream());
    }


    private ObjectInputStream messagesReader;
    private ObjectOutputStream messagesWriter;

    @Override
    protected void endWriting() {
        try {
            messagesWriter.close();
            if(!getSocket().isClosed()) {
                getSocket().close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void endReading() {
        try {
            messagesReader.close();
            if(!getSocket().isClosed()) {
                getSocket().close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected ServerMessage readMessage() throws IOException, FailedReadException {
        try {
            return (ServerMessage) messagesReader.readObject();
        } catch (ClassNotFoundException e)  {
            throw new FailedReadException(e);
        }
    }

    @Override
    protected void writeMessage(ClientMessage message) throws IOException {
        messagesWriter.writeObject(message);

    }
}
