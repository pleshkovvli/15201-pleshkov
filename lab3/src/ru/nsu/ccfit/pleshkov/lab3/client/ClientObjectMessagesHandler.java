package ru.nsu.ccfit.pleshkov.lab3.client;

import ru.nsu.ccfit.pleshkov.lab3.messages.ClientMessage;
import ru.nsu.ccfit.pleshkov.lab3.FailedReadException;
import ru.nsu.ccfit.pleshkov.lab3.messages.ServerMessage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

class ClientObjectMessagesHandler extends ClientMessagesHandler {

    ClientObjectMessagesHandler(Socket socket, String clientName, Client client) throws IOException {
        super(socket,clientName,client);
        messagesReader = new ObjectInputStream(socket.getInputStream());
        messagesWriter = new ObjectOutputStream(socket.getOutputStream());
    }

    private ObjectInputStream messagesReader;
    private ObjectOutputStream messagesWriter;

    @Override
    protected void endWriting() {

    }

    @Override
    protected void endReading() {

    }

    @Override
    protected void close() {
        try {
            messagesWriter.close();
            messagesReader.close();
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
